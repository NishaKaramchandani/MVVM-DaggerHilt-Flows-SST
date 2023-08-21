package com.example.baseproject.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.RowTeamBinding
import com.example.baseproject.view.data.TeamDetail
import com.example.baseproject.viewmodel.SORTORDER
import java.text.DecimalFormat

/**
 * Recyclerview adapter for team details
 */
class TeamDetailsAdapter : RecyclerView.Adapter<TeamDetailsAdapter.TeamViewHolder>() {

    inner class TeamViewHolder(private val binding: RowTeamBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(teamDetail: TeamDetail) {
            binding.teamnameTextview.text = teamDetail.name
            binding.winsTextview.text = teamDetail.wins.toString()
            binding.losesTextview.text = teamDetail.losses.toString()
            binding.drawsTextview.text = teamDetail.draws.toString()
            binding.winspercentageTextview.text = teamDetail.total.toString()
            binding.root.setOnClickListener {
                onItemClickListener?.let { it(teamDetail) }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<TeamDetail>() {
        override fun areItemsTheSame(oldItem: TeamDetail, newItem: TeamDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TeamDetail, newItem: TeamDetail): Boolean {
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
        val teamDetail = differ.currentList[position]
        holder.bind(teamDetail)
    }

    private var onItemClickListener: ((TeamDetail) -> Unit)? = null

    fun setOnItemClickListener(listener: (TeamDetail) -> Unit) {
        onItemClickListener = listener
    }

    fun sortByWins(sortorder: SORTORDER) {
        when (sortorder) {
            SORTORDER.ASCENDING -> differ.submitList(differ.currentList.sortedBy { it.wins })
            SORTORDER.DESCENDING -> differ.submitList(differ.currentList.sortedByDescending { it.wins })
        }
    }

    fun sortByLosses(sortorder: SORTORDER) {
        when (sortorder) {
            SORTORDER.ASCENDING -> differ.submitList(differ.currentList.sortedBy { it.losses })
            SORTORDER.DESCENDING -> differ.submitList(differ.currentList.sortedByDescending { it.losses })
        }
    }

    fun sortByDraws(sortorder: SORTORDER) {
        when (sortorder) {
            SORTORDER.ASCENDING -> differ.submitList(differ.currentList.sortedBy { it.draws }) {  }
            SORTORDER.DESCENDING -> differ.submitList(differ.currentList.sortedByDescending { it.draws })
        }
    }

    fun sortByTotal(sortorder: SORTORDER) {
        when (sortorder) {
            SORTORDER.ASCENDING -> differ.submitList(differ.currentList.sortedBy { it.total })
            SORTORDER.DESCENDING -> differ.submitList(differ.currentList.sortedByDescending { it.total })
        }
    }
}