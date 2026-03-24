package com.artsiom.footballpulse.ui.matches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artsiom.footballpulse.R
import com.artsiom.footballpulse.domain.model.Match
import java.text.SimpleDateFormat
import java.util.Locale

class MatchesAdapter : RecyclerView.Adapter<MatchesAdapter.MatchViewHolder>() {
    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeTeam: TextView = view.findViewById(R.id.homeTeam)
        val awayTeam: TextView = view.findViewById(R.id.awayTeam)
        val matchDate: TextView = view.findViewById(R.id.matchDate)
        val matchScore: TextView = view.findViewById(R.id.matchScore)
        val matchStatus: TextView = view.findViewById(R.id.matchStatus)
    }
    private var matches: List<Match> = emptyList()
    fun submitList(list: List<Match>) {
        this.matches = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: MatchViewHolder,
        position: Int
    ) {
        val match = matches[position]
        holder.homeTeam.text = match.homeTeam
        holder.awayTeam.text = match.awayTeam
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(match.date)
        holder.matchDate.text = if(date != null) outputFormat.format(date) else match.date
        holder.matchScore.text = if (match.homeScore != null && match.awayScore != null) {
            "${match.homeScore} - ${match.awayScore}"
        } else {
            "-"
        }
        holder.matchStatus.text = match.status
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun getItemCount(): Int = matches.size
}