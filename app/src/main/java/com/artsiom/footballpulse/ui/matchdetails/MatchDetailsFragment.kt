package com.artsiom.footballpulse.ui.matchdetails

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.artsiom.footballpulse.R
import com.artsiom.footballpulse.domain.model.Goal
import com.artsiom.footballpulse.domain.model.MatchDetail
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MatchDetailsFragment : Fragment() {

    companion object {
        private const val ARG_MATCH_ID = "match_id"

        fun newInstance(matchId: Int): MatchDetailsFragment {
            val fragment = MatchDetailsFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_MATCH_ID, matchId)
            }
            return fragment
        }

        private val INPUT_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        private val DATE_FORMAT = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)
        private val TIME_FORMAT = SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_match_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val matchId = requireArguments().getInt(ARG_MATCH_ID)
        val factory = MatchDetailsViewModelFactory(matchId)
        val viewModel = ViewModelProvider(this, factory)[MatchDetailsViewModel::class.java]

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val errorLayout = view.findViewById<View>(R.id.errorLayout)
        val errorText = view.findViewById<TextView>(R.id.errorText)
        val retryButton = view.findViewById<Button>(R.id.retryButton)
        val contentLayout = view.findViewById<View>(R.id.contentLayout)
        val btnBack = view.findViewById<TextView>(R.id.btnBack)

        val homeTeamName = view.findViewById<TextView>(R.id.homeTeamName)
        val awayTeamName = view.findViewById<TextView>(R.id.awayTeamName)
        val scoreOrTime = view.findViewById<TextView>(R.id.scoreOrTime)
        val matchDate = view.findViewById<TextView>(R.id.matchDate)
        val matchStatus = view.findViewById<TextView>(R.id.matchStatus)
        val homeLineupHeader = view.findViewById<TextView>(R.id.homeLineupHeader)
        val awayLineupHeader = view.findViewById<TextView>(R.id.awayLineupHeader)
        val lineupContainer = view.findViewById<LinearLayout>(R.id.lineupContainer)
        val benchCard = view.findViewById<View>(R.id.benchCard)

        // Substitutes card is not used with the goals strategy
        benchCard.visibility = View.GONE

        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        retryButton.setOnClickListener { viewModel.retry() }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is MatchDetailsUiState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            errorLayout.visibility = View.GONE
                            contentLayout.visibility = View.GONE
                        }
                        is MatchDetailsUiState.Error -> {
                            progressBar.visibility = View.GONE
                            errorLayout.visibility = View.VISIBLE
                            contentLayout.visibility = View.GONE
                            errorText.text = state.message
                        }
                        is MatchDetailsUiState.Success -> {
                            progressBar.visibility = View.GONE
                            errorLayout.visibility = View.GONE
                            contentLayout.visibility = View.VISIBLE
                            bindMatch(
                                state.match,
                                homeTeamName, awayTeamName, scoreOrTime,
                                matchDate, matchStatus,
                                homeLineupHeader, awayLineupHeader,
                                lineupContainer
                            )
                        }
                    }
                }
            }
        }
    }

    private fun bindMatch(
        match: MatchDetail,
        homeTeamName: TextView,
        awayTeamName: TextView,
        scoreOrTime: TextView,
        matchDate: TextView,
        matchStatus: TextView,
        homeGoalsHeader: TextView,
        awayGoalsHeader: TextView,
        goalsContainer: LinearLayout
    ) {
        homeTeamName.text = match.homeTeam
        awayTeamName.text = match.awayTeam

        val parsedDate = try { INPUT_FORMAT.parse(match.date) } catch (_: Exception) { null }

        scoreOrTime.text = when {
            match.homeScore != null && match.awayScore != null ->
                "${match.homeScore} - ${match.awayScore}"
            parsedDate != null -> TIME_FORMAT.format(parsedDate)
            else -> "- : -"
        }

        matchDate.text = if (parsedDate != null) DATE_FORMAT.format(parsedDate) else match.date

        matchStatus.text = match.status
        val context = requireContext()
        val density = context.resources.displayMetrics.density
        val bgDrawable = GradientDrawable().apply { cornerRadius = 4f * density }
        when (match.status.uppercase()) {
            "FINISHED" -> {
                bgDrawable.setColor(ContextCompat.getColor(context, R.color.color_status_finished_bg))
                matchStatus.setTextColor(ContextCompat.getColor(context, R.color.color_status_finished_text))
            }
            "IN_PLAY", "LIVE", "PAUSED" -> {
                bgDrawable.setColor(ContextCompat.getColor(context, R.color.color_status_live_bg))
                matchStatus.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            }
            else -> {
                bgDrawable.setColor(ContextCompat.getColor(context, R.color.color_status_scheduled_bg))
                matchStatus.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            }
        }
        matchStatus.background = bgDrawable

        homeGoalsHeader.text = match.homeTeam
        awayGoalsHeader.text = match.awayTeam
        populateGoalsRows(goalsContainer, match.goals, match.status)
    }

    private fun populateGoalsRows(container: LinearLayout, goals: List<Goal>, status: String) {
        container.removeAllViews()
        val context = requireContext()
        val density = context.resources.displayMetrics.density

        val statusUpper = status.uppercase()
        if (statusUpper == "SCHEDULED" || statusUpper == "TIMED") {
            addCenteredMessage(container, "Match not started yet")
            return
        }

        if (goals.isEmpty()) {
            addCenteredMessage(container, "No goals yet")
            return
        }

        val dividerColor = 0xFFE0E0E0.toInt()
        goals.forEachIndexed { index, goal ->
            val rowView = layoutInflater.inflate(R.layout.item_player_row, container, false)
            val homePlayerTv = rowView.findViewById<TextView>(R.id.homePlayer)
            val awayPlayerTv = rowView.findViewById<TextView>(R.id.awayPlayer)

            val minuteLabel = if (goal.injuryTime != null && goal.injuryTime > 0) {
                "${goal.minute}+${goal.injuryTime}'"
            } else {
                "${goal.minute}'"
            }
            val label = "$minuteLabel ${goal.scorerName}"

            if (goal.isHome) {
                homePlayerTv.text = label
                awayPlayerTv.text = ""
            } else {
                homePlayerTv.text = ""
                awayPlayerTv.text = label
            }

            if (index % 2 == 1) {
                rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_standings_row_odd))
            }

            container.addView(rowView)

            if (index < goals.size - 1) {
                val divider = View(context)
                divider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (0.5f * density).toInt().coerceAtLeast(1)
                )
                divider.setBackgroundColor(dividerColor)
                container.addView(divider)
            }
        }
    }

    private fun addCenteredMessage(container: LinearLayout, message: String) {
        val tv = TextView(requireContext())
        tv.text = message
        tv.gravity = Gravity.CENTER
        tv.textSize = 13f
        tv.setTextColor(0xFF757575.toInt())
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 24, 0, 24)
        tv.layoutParams = params
        container.addView(tv)
    }
}
