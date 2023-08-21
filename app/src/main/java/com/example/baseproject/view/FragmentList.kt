package com.example.baseproject.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.view.adapter.TeamsAdapter
import com.example.baseproject.databinding.FragmentListBinding
import com.example.baseproject.utils.safeNavigate
import com.example.baseproject.utils.setAscendingIcon
import com.example.baseproject.utils.setDescendingIcon
import com.example.baseproject.view.data.Team
import com.example.baseproject.viewmodel.TeamListUiState
import com.example.baseproject.viewmodel.TeamsViewModel
import com.example.baseproject.viewmodel.SORTORDER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragment for Overall Standings
 */
@AndroidEntryPoint
class FragmentList : Fragment(), OnClickListener {

    private lateinit var binding: FragmentListBinding

    private val teamsAdapter = TeamsAdapter()

    companion object {
        private const val TAG = "FragmentList"
    }

    private val viewModel: TeamsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getText(R.string.app_name)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListBinding.bind(view)
        binding.recyclerView.apply {
            adapter = teamsAdapter
            layoutManager = LinearLayoutManager(context)
            teamsAdapter.setOnItemClickListener { selectedTeam -> gotoDetails(selectedTeam) }
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setOnClickListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.teamsUiState.collect {
                    when (it) {
                        is TeamListUiState.Loading -> showLoadingView()
                        is TeamListUiState.Success -> {
                            showListView()
                            binding.listTopBar.winstitleTextview.tag = SORTORDER.DESCENDING
                            binding.listTopBar.winstitleTextview.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.sort_ascending,
                                0
                            )
                            teamsAdapter.differ.submitList(it.teamsList)
                        }

                        is TeamListUiState.Error -> {
                            showErrorView(it.errorMessageId.toString())
                        }
                    }
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.listTopBar.winstitleTextview.setOnClickListener(this)
        binding.listTopBar.lossestitleTextview.setOnClickListener(this)
        binding.listTopBar.drawstitleTextview.setOnClickListener(this)
        binding.listTopBar.winspercentagetitleTextview.setOnClickListener(this)
    }

    private fun showLoadingView() {
        binding.loadingProgressbar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.messageTextView.visibility = View.GONE
    }

    private fun showListView() {
        binding.messageTextView.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.loadingProgressbar.visibility = View.GONE
    }

    private fun showErrorView(message: String) {
        binding.messageTextView.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.loadingProgressbar.visibility = View.GONE
        binding.messageTextView.text = message
    }

    override fun onClick(view: View?) {
        view?.let {
            when (it.id) {
                R.id.winstitle_textview -> sortByWins(it)
                R.id.drawstitle_textview -> sortByDraws(it)
                R.id.lossestitle_textview -> sortByLosses(it)
                R.id.winspercentagetitle_textview -> sortByWinPer(it)
                else -> {
                    Log.d(TAG, "onClick: No action for onclick")
                }
            }
        }
    }

    private fun sortByWins(view: View) {
        if (view.tag == null || view.tag == SORTORDER.ASCENDING) {
            teamsAdapter.sortByWins(SORTORDER.DESCENDING)
            view.tag = SORTORDER.DESCENDING
            binding.listTopBar.winstitleTextview.setAscendingIcon()
        } else {
            teamsAdapter.sortByWins(SORTORDER.ASCENDING)
            view.tag = SORTORDER.ASCENDING
            binding.listTopBar.winstitleTextview.setDescendingIcon()
        }
        scrollToTop()
    }

    private fun sortByDraws(view: View) {
        if (view.tag == null || view.tag == SORTORDER.ASCENDING) {
            teamsAdapter.sortByDraws(SORTORDER.DESCENDING)
            view.tag = SORTORDER.DESCENDING
            binding.listTopBar.drawstitleTextview.setAscendingIcon()
        } else {
            teamsAdapter.sortByDraws(SORTORDER.ASCENDING)
            view.tag = SORTORDER.ASCENDING
            binding.listTopBar.drawstitleTextview.setDescendingIcon()
        }
        scrollToTop()
    }

    private fun sortByLosses(view: View) {
        if (view.tag == null || view.tag == SORTORDER.ASCENDING) {
            teamsAdapter.sortByLosses(SORTORDER.DESCENDING)
            view.tag = SORTORDER.DESCENDING
            binding.listTopBar.lossestitleTextview.setAscendingIcon()
        } else {
            teamsAdapter.sortByLosses(SORTORDER.ASCENDING)
            view.tag = SORTORDER.ASCENDING
            binding.listTopBar.lossestitleTextview.setDescendingIcon()
        }
        scrollToTop()
    }

    private fun sortByWinPer(view: View) {
        if (view.tag == null || view.tag == SORTORDER.ASCENDING) {
            teamsAdapter.sortByWinPer(SORTORDER.DESCENDING)
            view.tag = SORTORDER.DESCENDING
            binding.listTopBar.winspercentagetitleTextview.setAscendingIcon()
        } else {
            teamsAdapter.sortByWinPer(SORTORDER.ASCENDING)
            view.tag = SORTORDER.ASCENDING
            binding.listTopBar.winspercentagetitleTextview.setDescendingIcon()
        }
        scrollToTop()
    }

    private fun scrollToTop() {
        binding.recyclerView.post { binding.recyclerView.layoutManager?.scrollToPosition(0) }
    }


    private fun gotoDetails(selectedTeam: Team) {
        findNavController().safeNavigate(
            FragmentListDirections.actionFragmentListToFragmentDetails(
                selectedTeam
            )
        )
    }
}