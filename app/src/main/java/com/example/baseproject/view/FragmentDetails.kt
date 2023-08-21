package com.example.baseproject.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentDetailsBinding
import com.example.baseproject.utils.setAscendingIcon
import com.example.baseproject.utils.setDescendingIcon
import com.example.baseproject.view.adapter.TeamDetailsAdapter
import com.example.baseproject.view.data.TeamDetail
import com.example.baseproject.viewmodel.SORTORDER
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for team details
 */
@AndroidEntryPoint
class FragmentDetails : Fragment(), OnClickListener {

    private lateinit var binding: FragmentDetailsBinding
    private val teamDetailsAdapter = TeamDetailsAdapter()

    companion object {
        private const val TAG = "FragmentDetails"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedTeam = arguments?.let { FragmentDetailsArgs.fromBundle(it).team }

        Log.d(TAG, "onViewCreated: $selectedTeam")

        (activity as AppCompatActivity).supportActionBar?.title = selectedTeam?.name

        binding.detailsTopBar.winspercentagetitleTextview.text = "Total"

        setOnClickListeners()

        binding.detailsRecyclerView.apply {
            adapter = teamDetailsAdapter
            layoutManager = LinearLayoutManager(context)
            val detailsList: List<TeamDetail> =
                selectedTeam?.details ?: mutableListOf()
            binding.detailsTopBar.winstitleTextview.tag = SORTORDER.DESCENDING
            binding.detailsTopBar.winstitleTextview.setAscendingIcon()
            teamDetailsAdapter.differ.submitList(detailsList.sortedByDescending { it.wins })
        }
    }


    private fun setOnClickListeners() {
        binding.detailsTopBar.winstitleTextview.setOnClickListener(this)
        binding.detailsTopBar.lossestitleTextview.setOnClickListener(this)
        binding.detailsTopBar.drawstitleTextview.setOnClickListener(this)
        binding.detailsTopBar.winspercentagetitleTextview.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        view?.let {
            when (it.id) {
                R.id.winstitle_textview -> sortByWins(it)
                R.id.drawstitle_textview -> sortByDraws(it)
                R.id.lossestitle_textview -> sortByLosses(it)
                R.id.winspercentagetitle_textview -> sortByTotal(it)
                else -> {
                    Log.d(TAG, "onClick: No action for onclick")
                }
            }
        }
    }

    private fun sortByWins(view: View) {
        if (view.tag == null || view.tag == SORTORDER.ASCENDING) {
            teamDetailsAdapter.sortByWins(SORTORDER.DESCENDING)
            view.tag = SORTORDER.DESCENDING
            binding.detailsTopBar.winstitleTextview.setAscendingIcon()
        } else {
            teamDetailsAdapter.sortByWins(SORTORDER.ASCENDING)
            view.tag = SORTORDER.ASCENDING
            binding.detailsTopBar.winstitleTextview.setDescendingIcon()
        }
        scrollToTop()
    }

    private fun sortByDraws(view: View) {
        if (view.tag == null || view.tag == SORTORDER.ASCENDING) {
            teamDetailsAdapter.sortByDraws(SORTORDER.DESCENDING)
            view.tag = SORTORDER.DESCENDING
            binding.detailsTopBar.drawstitleTextview.setAscendingIcon()
        } else {
            teamDetailsAdapter.sortByDraws(SORTORDER.ASCENDING)
            view.tag = SORTORDER.ASCENDING
            binding.detailsTopBar.drawstitleTextview.setDescendingIcon()
        }
        scrollToTop()
    }

    private fun sortByLosses(view: View) {
        if (view.tag == null || view.tag == SORTORDER.ASCENDING) {
            teamDetailsAdapter.sortByLosses(SORTORDER.DESCENDING)
            view.tag = SORTORDER.DESCENDING
            binding.detailsTopBar.lossestitleTextview.setAscendingIcon()
        } else {
            teamDetailsAdapter.sortByLosses(SORTORDER.ASCENDING)
            view.tag = SORTORDER.ASCENDING
            binding.detailsTopBar.lossestitleTextview.setDescendingIcon()
        }
        scrollToTop()
    }

    private fun sortByTotal(view: View) {
        if (view.tag == null || view.tag == SORTORDER.ASCENDING) {
            teamDetailsAdapter.sortByTotal(SORTORDER.DESCENDING)
            view.tag = SORTORDER.DESCENDING
            binding.detailsTopBar.winspercentagetitleTextview.setAscendingIcon()
        } else {
            teamDetailsAdapter.sortByTotal(SORTORDER.ASCENDING)
            view.tag = SORTORDER.ASCENDING
            binding.detailsTopBar.winspercentagetitleTextview.setDescendingIcon()
        }
        scrollToTop()
    }

    private fun scrollToTop() {
        binding.detailsRecyclerView.post { binding.detailsRecyclerView.layoutManager?.scrollToPosition(0) }
    }
}