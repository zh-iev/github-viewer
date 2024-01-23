package ru.zhiev.githubviewer.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zhiev.githubviewer.GitHubViewerApplication
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.TokenManager
import ru.zhiev.githubviewer.databinding.FragmentSearchBinding
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase
import ru.zhiev.githubviewer.presentation.ui.repositories.RepositoriesAdapter

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
        val userRecyclerView: RecyclerView = binding.userRecyclerView
        val reposRecyclerView: RecyclerView = binding.repoSearchRV
        var usersAdapter: UsersAdapter
        var reposAdapter: RepositoriesAdapter
        var query: String

        binding.searchButton.setOnClickListener {
            query = binding.searchField.text.toString()
            if (query.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                userRecyclerView.visibility = View.GONE
                reposRecyclerView.visibility = View.GONE
                binding.countResults.text = getString(R.string.count_of_results, 0)
                if (binding.byRepRB.isChecked) {
                    searchViewModel.searchRepositories(token, query)
                } else {
                    searchViewModel.searchUsers(token, query)
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.enterReq), Toast.LENGTH_SHORT).show()
            }
        }


        searchViewModel.foundRepositories.observe(viewLifecycleOwner) {
            binding.repoSearchRV.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            binding.countResults.text = getString(R.string.count_of_results, it.totalCount)

            reposAdapter = RepositoriesAdapter(requireContext(), it.items) { clickedRepos ->
                Toast.makeText(requireContext(), "Clicked: ${clickedRepos.name}",
                    Toast.LENGTH_LONG).show()
            }
            reposRecyclerView.adapter = reposAdapter
            reposRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        searchViewModel.foundUsers.observe(viewLifecycleOwner) {
            userRecyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            binding.countResults.text = getString(R.string.count_of_results, it.totalCount)

            usersAdapter = UsersAdapter(it.items) { clickedUser ->
                Toast.makeText(requireContext(), "Clicked: ${clickedUser.login}", Toast.LENGTH_LONG).show()
            }
            userRecyclerView.adapter = usersAdapter
            userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}