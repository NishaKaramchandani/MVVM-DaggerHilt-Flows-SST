package com.example.baseproject.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.RowTeamBinding
import com.example.baseproject.view.data.Team
import com.example.baseproject.viewmodel.SORTORDER
import com.example.baseproject.viewmodel.TeamListUiState
import kotlinx.coroutines.launch
import java.text.DecimalFormat

/**
 * Recyclerview adapter for teams
 */
class TeamsAdapter : RecyclerView.Adapter<TeamsAdapter.TeamViewHolder>() {

    inner class TeamViewHolder(private val binding: RowTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(team: Team) {
            binding.teamnameTextview.text = team.name
            binding.winsTextview.text = team.totalWins.toString()
            binding.losesTextview.text = team.totalLosses.toString()
            binding.drawsTextview.text = team.totalDraws.toString()
            binding.winspercentageTextview.text = team.winsPer.toString()

            binding.root.setOnClickListener {
                onItemClickListener?.let { it(team) }
            }
        }


    }

    private val differCallback = object : DiffUtil.ItemCallback<Team>() {
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = RowTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val country = differ.currentList[position]
        holder.bind(country)
    }

    private var onItemClickListener: ((Team) -> Unit)? = null

    fun setOnItemClickListener(listener: (Team) -> Unit) {
        onItemClickListener = listener
    }

    fun sortByWins(sortorder: SORTORDER) {
        when (sortorder) {
            SORTORDER.ASCENDING -> differ.submitList(differ.currentList.sortedBy { it.totalWins })
            SORTORDER.DESCENDING -> differ.submitList(differ.currentList.sortedByDescending { it.totalWins })
        }
    }

    fun sortByLosses(sortorder: SORTORDER) {
        when (sortorder) {
            SORTORDER.ASCENDING -> differ.submitList(differ.currentList.sortedBy { it.totalLosses })
            SORTORDER.DESCENDING -> differ.submitList(differ.currentList.sortedByDescending { it.totalLosses })
        }
    }

    fun sortByDraws(sortorder: SORTORDER) {
        when (sortorder) {
            SORTORDER.ASCENDING -> differ.submitList(differ.currentList.sortedBy { it.totalDraws }) {  }
            SORTORDER.DESCENDING -> differ.submitList(differ.currentList.sortedByDescending { it.totalDraws })
        }
    }

    fun sortByWinPer(sortorder: SORTORDER) {
        when (sortorder) {
            SORTORDER.ASCENDING -> differ.submitList(differ.currentList.sortedBy { it.winsPer })
            SORTORDER.DESCENDING -> differ.submitList(differ.currentList.sortedByDescending { it.winsPer })
        }
    }
}