package com.animesearch.presentation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.animesearch.adapter.ResultsAdapter
import com.animesearch.database.AppDatabase
import com.animesearch.databinding.FragmentSearchBinding
import com.animesearch.interactor.HistoryInteractorImpl
import com.animesearch.interactor.SearchInteractorImpl
import com.animesearch.repository.HistoryRepositoryImpl
import com.animesearch.repository.SearchRepositoryImpl
import com.animesearch.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class SearchFragment : Fragment() {
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val pickPhoto =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                sendImage(uri)
            }
        }
    private val resultsAdapter by lazy { ResultsAdapter() }
    private val dao by lazy { AppDatabase.getInstance(requireContext()).historyDao() }
    private val historyRepository by lazy { HistoryRepositoryImpl(dao) }
    private val historyInteractor by lazy { HistoryInteractorImpl(historyRepository) }
    private val searchRepository by lazy { SearchRepositoryImpl() }
    private val searchInteractor by lazy { SearchInteractorImpl(searchRepository) }
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        initRecyclerView()
        initListeners()
    }

    private fun initRecyclerView() {
        binding.resultsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resultsAdapter
        }
        searchViewModel.resultList.observe(viewLifecycleOwner) { list ->
            resultsAdapter.setList(list)
        }
    }

    private fun initListeners() {
        binding.btnPhoto.setOnClickListener {
            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun sendImage(uri: Uri) {
        binding.btnPhoto.isEnabled = false
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val file = it.saveFileFromInputStream()
                    val results = searchInteractor.searchImage(file)
                    val firstResult = results.firstOrNull()
                    withContext(Dispatchers.Main) {
                        firstResult?.let { result ->
                            resultsAdapter.setList(listOf(result))
                            searchViewModel.setResultList(listOf(result))
                        } ?: run {
                            resultsAdapter.setList(emptyList())
                            searchViewModel.setResultList(emptyList())
                        }
                        binding.btnPhoto.isEnabled = true
                    }
                    firstResult?.let { result ->
                        historyInteractor.insertHistory(listOf(result.toHistoryModel()))
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        binding.btnPhoto.isEnabled = true
                        Toast.makeText(context, "Произошла ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } finally {
                    inputStream.close()
                }
            }
        } ?: run {
            binding.btnPhoto.isEnabled = true
            Toast.makeText(requireContext(), "Попробуйте еще раз", Toast.LENGTH_SHORT).show()
        }
    }

    private fun InputStream.saveFileFromInputStream(): File {
        val file = File(requireContext().cacheDir, "uploaded_image.jpg")
        file.outputStream().use { output ->
            this.copyTo(output)
        }
        return file
    }
}