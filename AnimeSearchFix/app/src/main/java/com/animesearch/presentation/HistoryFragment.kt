package com.animesearch.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.animesearch.adapter.HistoryAdapter
import com.animesearch.database.AppDatabase
import com.animesearch.databinding.FragmentHistoryBinding
import com.animesearch.interactor.HistoryInteractorImpl
import com.animesearch.repository.HistoryRepositoryImpl
import com.animesearch.viewmodel.HistoryViewModel
import com.animesearch.viewmodel.HistoryViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyAdapter by lazy { HistoryAdapter() }
    private val dao by lazy { AppDatabase.getInstance(requireContext()).historyDao() }
    private val historyRepository by lazy { HistoryRepositoryImpl(dao) }
    private val historyInteractor by lazy { HistoryInteractorImpl(historyRepository) }
    private val historyViewModel by lazy {
        ViewModelProvider(
            this,
            HistoryViewModelFactory(historyInteractor)
        )[HistoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        getHistory()
    }

    private fun initialize() {
        initRecyclerView()
        initListeners()
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.historyFragmentTitle.setOnClickListener {
            historyViewModel.clearHistory()
        }
    }

    private fun getHistory() {
        historyViewModel.loadHistory()
    }

    private fun initRecyclerView() {
        binding.historyRv.layoutManager = LinearLayoutManager(context)
        binding.historyRv.adapter = historyAdapter

        historyViewModel.historyList.observe(viewLifecycleOwner) { list ->
            historyAdapter.setList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}