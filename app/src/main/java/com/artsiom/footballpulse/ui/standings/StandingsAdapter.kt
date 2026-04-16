package com.artsiom.footballpulse.ui.standings

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.artsiom.footballpulse.R
import com.artsiom.footballpulse.domain.model.Standing

class StandingsAdapter(
    private val onTeamClick: (teamId: Int) -> Unit = {}
) : RecyclerView.Adapter<StandingsAdapter.StandingViewHolder>() {

    private var standings: List<Standing> = emptyList()

    var mode: ViewMode = ViewMode.FULL
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun submitList(list: List<Standing>) {
        standings = list
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ROW = 1
        private const val CHAMPIONS_LEAGUE_SPOTS = 4
        private const val EUROPA_LEAGUE_POSITION = 5
        private const val RELEGATION_ZONE_SIZE = 3
        private const val FORM_WINDOW = 5
    }

    override fun getItemViewType(position: Int) = if (position == 0) TYPE_HEADER else TYPE_ROW

    // +1 for the sticky header row at position 0
    override fun getItemCount() = standings.size + 1

    inner class StandingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leftBorder: View = view.findViewById(R.id.leftBorder)
        val tvPosition: TextView = view.findViewById(R.id.tvPosition)
        val imgTeamCrest: ImageView = view.findViewById(R.id.imgTeamCrest)
        val tvTeamName: TextView = view.findViewById(R.id.tvTeamName)
        val tvMp: TextView = view.findViewById(R.id.tvMp)
        val tvWins: TextView = view.findViewById(R.id.tvWins)
        val tvDraws: TextView = view.findViewById(R.id.tvDraws)
        val tvLosses: TextView = view.findViewById(R.id.tvLosses)
        val tvGoals: TextView = view.findViewById(R.id.tvGoals)
        val tvGD: TextView = view.findViewById(R.id.tvGD)
        val formContainer: LinearLayout = view.findViewById(R.id.formContainer)
        val tvPoints: TextView = view.findViewById(R.id.tvPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_standing, parent, false)
        return StandingViewHolder(view)
    }

    override fun onBindViewHolder(holder: StandingViewHolder, position: Int) {
        if (position == 0) bindHeader(holder) else bindRow(holder, standings[position - 1])
    }

    /** Apply column visibility based on current mode. */
    private fun applyModeVisibility(holder: StandingViewHolder) {
        when (mode) {
            ViewMode.FULL -> {
                holder.tvMp.visibility = View.VISIBLE
                holder.tvWins.visibility = View.VISIBLE
                holder.tvDraws.visibility = View.VISIBLE
                holder.tvLosses.visibility = View.VISIBLE
                holder.tvGoals.visibility = View.VISIBLE
                holder.tvGD.visibility = View.GONE
                holder.formContainer.visibility = View.GONE
            }
            ViewMode.SHORT -> {
                holder.tvMp.visibility = View.VISIBLE
                holder.tvWins.visibility = View.GONE
                holder.tvDraws.visibility = View.GONE
                holder.tvLosses.visibility = View.GONE
                holder.tvGoals.visibility = View.GONE
                holder.tvGD.visibility = View.VISIBLE
                holder.formContainer.visibility = View.GONE
            }
            ViewMode.FORM -> {
                holder.tvMp.visibility = View.GONE
                holder.tvWins.visibility = View.GONE
                holder.tvDraws.visibility = View.GONE
                holder.tvLosses.visibility = View.GONE
                holder.tvGoals.visibility = View.GONE
                holder.tvGD.visibility = View.GONE
                holder.formContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun bindHeader(holder: StandingViewHolder) {
        val ctx = holder.itemView.context
        applyModeVisibility(holder)

        holder.itemView.setBackgroundColor(
            ContextCompat.getColor(ctx, R.color.color_standings_header_bg)
        )
        holder.leftBorder.setBackgroundColor(Color.TRANSPARENT)

        val headerColor = ContextCompat.getColor(ctx, R.color.color_date_header_text)

        holder.imgTeamCrest.visibility = View.INVISIBLE
        holder.tvPosition.apply { text = "#"; setTextColor(headerColor); setTypeface(null, Typeface.NORMAL) }
        holder.tvTeamName.apply { text = "Team"; setTextColor(headerColor); setTypeface(null, Typeface.NORMAL) }
        holder.tvMp.apply { text = "MP"; setTextColor(headerColor) }
        holder.tvWins.apply { text = "W"; setTextColor(headerColor) }
        holder.tvDraws.apply { text = "D"; setTextColor(headerColor) }
        holder.tvLosses.apply { text = "L"; setTextColor(headerColor) }
        holder.tvGoals.apply { text = "GF:GA"; setTextColor(headerColor) }
        holder.tvGD.apply { text = "GD"; setTextColor(headerColor) }
        holder.tvPoints.apply { text = "Pts"; setTextColor(headerColor); setTypeface(null, Typeface.NORMAL) }

        if (mode == ViewMode.FORM) {
            holder.formContainer.removeAllViews()
            val label = TextView(ctx).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "Form"
                textSize = 11f
                gravity = Gravity.CENTER
                setTextColor(headerColor)
            }
            holder.formContainer.addView(label)
        }
    }

    private fun bindRow(holder: StandingViewHolder, standing: Standing) {
        val ctx = holder.itemView.context
        applyModeVisibility(holder)
        holder.itemView.setOnClickListener { onTeamClick(standing.teamId) }

        // Alternating row background
        val rowBg = if (standing.position % 2 == 0) {
            ContextCompat.getColor(ctx, R.color.color_standings_row_odd)
        } else {
            ContextCompat.getColor(ctx, R.color.color_standings_row_even)
        }
        holder.itemView.setBackgroundColor(rowBg)

        // Left border by competition zone
        val borderColor = when {
            standing.position <= CHAMPIONS_LEAGUE_SPOTS ->
                ContextCompat.getColor(ctx, R.color.color_standings_cl_border)
            standing.position == EUROPA_LEAGUE_POSITION ->
                ContextCompat.getColor(ctx, R.color.color_standings_el_border)
            standing.position > standings.size - RELEGATION_ZONE_SIZE ->
                ContextCompat.getColor(ctx, R.color.color_standings_rel_border)
            else -> Color.TRANSPARENT
        }
        holder.leftBorder.setBackgroundColor(borderColor)

        val defaultColor = ContextCompat.getColor(ctx, R.color.color_text_primary)

        holder.tvPosition.apply { text = standing.position.toString(); setTextColor(defaultColor); setTypeface(null, Typeface.NORMAL) }
        holder.imgTeamCrest.visibility = View.VISIBLE
        holder.imgTeamCrest.load(standing.crest) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder_crest)
            error(R.drawable.ic_placeholder_crest)
        }
        holder.tvTeamName.apply { text = standing.teamName; setTextColor(defaultColor); setTypeface(null, Typeface.NORMAL) }
        holder.tvPoints.apply { text = standing.points.toString(); setTextColor(defaultColor); setTypeface(null, Typeface.BOLD) }

        when (mode) {
            ViewMode.FULL -> {
                holder.tvMp.apply { text = standing.playedGames.toString(); setTextColor(defaultColor) }
                holder.tvWins.apply { text = standing.won.toString(); setTextColor(defaultColor) }
                holder.tvDraws.apply { text = standing.draw.toString(); setTextColor(defaultColor) }
                holder.tvLosses.apply { text = standing.lost.toString(); setTextColor(defaultColor) }
                holder.tvGoals.apply {
                    text = "${standing.goalsFor}:${standing.goalsAgainst}"
                    setTextColor(defaultColor)
                }
            }
            ViewMode.SHORT -> {
                holder.tvMp.apply { text = standing.playedGames.toString(); setTextColor(defaultColor) }
                val gd = standing.goalDifference
                holder.tvGD.apply {
                    text = if (gd > 0) "+$gd" else gd.toString()
                    setTextColor(defaultColor)
                }
            }
            ViewMode.FORM -> {
                buildFormBadges(holder, standing)
            }
        }
    }

    private fun buildFormBadges(holder: StandingViewHolder, standing: Standing) {
        val ctx = holder.itemView.context
        holder.formContainer.removeAllViews()

        // football-data.org returns form as a run of characters e.g. "WDLWW" (no commas).
        // Defensively strip any commas so both "WDLWW" and "W,D,L,W,W" work.
        val results = standing.form
            ?.replace(",", "")
            ?.map { it.toString() }
            ?.filter { it in setOf("W", "D", "L") }
            ?.takeLast(FORM_WINDOW)
            ?: emptyList()

        // Pad to exactly FORM_WINDOW slots so the column width is always consistent
        val paddedResults = buildList {
            repeat(FORM_WINDOW - results.size) { add("") }
            addAll(results)
        }

        val density = ctx.resources.displayMetrics.density
        val badgeSize = (28 * density).toInt()
        val badgeGap = (3 * density).toInt()
        // 14dp = half of 28dp badge → fully circular. Named distinctly to avoid
        // shadowing GradientDrawable.cornerRadius inside the apply{} block below.
        val badgeCornerRadius = 14f * density

        paddedResults.forEachIndexed { index, result ->
            val params = LinearLayout.LayoutParams(badgeSize, badgeSize).apply {
                if (index < paddedResults.lastIndex) marginEnd = badgeGap
            }
            val badge = TextView(ctx).apply {
                layoutParams = params
                gravity = Gravity.CENTER
                textSize = 11f
                setTextColor(Color.WHITE)
                setTypeface(null, Typeface.BOLD)
            }

            if (result.isEmpty()) {
                badge.visibility = View.INVISIBLE
            } else {
                val bgColor = when (result) {
                    "W" -> ContextCompat.getColor(ctx, R.color.color_form_win_bg)
                    "D" -> ContextCompat.getColor(ctx, R.color.color_form_draw_bg)
                    else -> ContextCompat.getColor(ctx, R.color.color_form_loss_bg)
                }
                badge.background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    // Use badgeCornerRadius (outer local) — NOT GradientDrawable.cornerRadius,
                    // which would cause a self-assignment (0 = 0) and skip rounding entirely.
                    cornerRadius = badgeCornerRadius
                    setColor(bgColor)
                }
                badge.text = result
            }

            holder.formContainer.addView(badge)
        }
    }
}
