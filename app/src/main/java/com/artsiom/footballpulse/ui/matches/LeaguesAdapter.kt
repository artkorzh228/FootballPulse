package com.artsiom.footballpulse.ui.matches
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artsiom.footballpulse.domain.model.League
import android.view.LayoutInflater
import android.widget.TextView
import com.artsiom.footballpulse.R
import com.artsiom.footballpulse.ui.matches.MatchesAdapter.MatchViewHolder


class LeaguesAdapter(private val onLeagueClick: (League) -> Unit) : RecyclerView.Adapter<LeaguesAdapter.LeaguesViewHolder>()  {
    class LeaguesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leagueName: TextView = view.findViewById(R.id.leagueName)
    }
    private var leagues: List<League> = emptyList()
    fun submitList(list: List<League>) {
        this.leagues = list
        android.util.Log.d("FootballPulse", "submitList called, size: ${list.size}")
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: LeaguesViewHolder,
        position: Int
    ) {
        val league = leagues[position]
        holder.leagueName.text = league.name
        holder.leagueName.setOnClickListener { onLeagueClick(league) }
        android.util.Log.d("FootballPulse", "onBind league: ${league.name}")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaguesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_league, parent, false)
        return LeaguesViewHolder(view)
    }

    override fun getItemCount(): Int = leagues.size
}