package com.artsiom.footballpulse.ui.matches

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.artsiom.footballpulse.R
import com.artsiom.footballpulse.domain.model.Match
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

sealed class MatchListItem {
    data class DateHeader(val dateLabel: String) : MatchListItem()
    data class MatchCard(val match: Match) : MatchListItem()
}

class MatchesAdapter(private val onMatchClick: (Match) -> Unit = {}) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATE_HEADER = 0
        private const val VIEW_TYPE_MATCH_CARD = 1

        private val INPUT_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        private val DATE_KEY_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        private val DATE_HEADER_FORMAT = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)
        private val TIME_FORMAT = SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    private var items: List<MatchListItem> = emptyList()

    fun submitList(matches: List<Match>) {
        val grouped = matches
            .groupBy { match ->
                val date = INPUT_FORMAT.parse(match.date)
                if (date != null) DATE_KEY_FORMAT.format(date) else "Unknown"
            }
            .toSortedMap()

        val flatList = mutableListOf<MatchListItem>()
        grouped.forEach { (_, matchesForDate) ->
            val firstDate = INPUT_FORMAT.parse(matchesForDate[0].date)
            val headerLabel = if (firstDate != null) DATE_HEADER_FORMAT.format(firstDate) else "Unknown Date"
            flatList.add(MatchListItem.DateHeader(headerLabel))
            matchesForDate.forEach { flatList.add(MatchListItem.MatchCard(it)) }
        }

        items = flatList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is MatchListItem.DateHeader -> VIEW_TYPE_DATE_HEADER
        is MatchListItem.MatchCard -> VIEW_TYPE_MATCH_CARD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_DATE_HEADER -> {
                val view = inflater.inflate(R.layout.item_date_header, parent, false)
                DateHeaderViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_match, parent, false)
                MatchCardViewHolder(view, onMatchClick)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is MatchListItem.DateHeader -> (holder as DateHeaderViewHolder).bind(item)
            is MatchListItem.MatchCard -> (holder as MatchCardViewHolder).bind(item.match)
        }
    }

    override fun getItemCount(): Int = items.size

    class DateHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: TextView = view.findViewById(R.id.dateHeaderText)

        fun bind(header: MatchListItem.DateHeader) {
            dateText.text = header.dateLabel
        }
    }

    class MatchCardViewHolder(view: View, private val onMatchClick: (Match) -> Unit) : RecyclerView.ViewHolder(view) {
        private val homeTeam: TextView = view.findViewById(R.id.homeTeam)
        private val awayTeam: TextView = view.findViewById(R.id.awayTeam)
        private val matchDate: TextView = view.findViewById(R.id.matchDate)
        private val matchScore: TextView = view.findViewById(R.id.matchScore)
        private val matchStatus: TextView = view.findViewById(R.id.matchStatus)

        fun bind(match: Match) {
            itemView.setOnClickListener { onMatchClick(match) }
            homeTeam.text = match.homeTeam
            awayTeam.text = match.awayTeam

            val date = INPUT_FORMAT.parse(match.date)
            matchDate.text = if (date != null) TIME_FORMAT.format(date) else match.date

            matchScore.text = if (match.homeScore != null && match.awayScore != null) {
                "${match.homeScore} - ${match.awayScore}"
            } else {
                "- : -"
            }

            matchStatus.text = match.status

            val context = itemView.context
            val density = context.resources.displayMetrics.density
            val bgDrawable = GradientDrawable().apply {
                cornerRadius = 4f * density
            }

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
        }
    }
}
