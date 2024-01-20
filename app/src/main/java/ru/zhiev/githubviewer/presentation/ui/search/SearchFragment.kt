package ru.zhiev.githubviewer.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.zhiev.githubviewer.GitHubViewerApplication
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.TokenManager
import ru.zhiev.githubviewer.databinding.FragmentSearchBinding
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private lateinit var tokenManager: TokenManager

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appRepository = (requireContext().applicationContext as GitHubViewerApplication).repository
        val workWithGitHubUseCase = WorkWithGitHubUseCase(appRepository)
        val searchViewModel =
            ViewModelProvider(this,
                SearchViewModelFactory(workWithGitHubUseCase))[SearchViewModel::class.java]

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        tokenManager = TokenManager(requireContext())
        val token = tokenManager.accessToken ?: ""

        val progressBar: ProgressBar = binding.progressBar
        var query: String

        binding.searchButton.setOnClickListener {
            query = binding.searchField.text.toString()
            progressBar.visibility = View.VISIBLE
            if (query.isNotEmpty()) {
                if (binding.byRepRB.isChecked) {
                    searchViewModel.searchRepositories(token, query)
                } else {
                    searchViewModel.searchUsers(token, query)
                }
            }
        }

        searchViewModel.foundRepositories.observe(viewLifecycleOwner) {
            progressBar.visibility = View.GONE
            binding.countResults.text = getString(R.string.count_of_results, it.totalCount)
            Toast.makeText(requireContext(), "Repo: ${it.items.joinToString(",")}", Toast.LENGTH_LONG).show()
        }

        searchViewModel.foundUsers.observe(viewLifecycleOwner) {
            progressBar.visibility = View.GONE
            binding.countResults.text = getString(R.string.count_of_results, it.totalCount)
            Toast.makeText(requireContext(), "Repo: ${it.items.joinToString(",")}", Toast.LENGTH_LONG).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}