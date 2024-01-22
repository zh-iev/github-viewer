package ru.zhiev.githubviewer.presentation.ui.repositories

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zhiev.githubviewer.GitHubViewerApplication
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.TokenManager
import ru.zhiev.githubviewer.databinding.FragmentRepositotiesBinding
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase

class RepositoriesFragment : Fragment() {

    private var _binding: FragmentRepositotiesBinding? = null
    private lateinit var tokenManager: TokenManager
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appRepository = (requireContext().applicationContext as GitHubViewerApplication).repository
        val workWithGitHubUseCase = WorkWithGitHubUseCase(appRepository)
        val repositoriesViewModel =
            ViewModelProvider(this,
                RepositoriesViewModelFactory(workWithGitHubUseCase)
            )[RepositoriesViewModel::class.java]
        _binding = FragmentRepositotiesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        tokenManager = TokenManager(requireContext())
        val token = tokenManager.accessToken ?: ""

        val repositoriesRecyclerView : RecyclerView = binding.repositoriesRecyclerView
        var repositoriesAdapter: RepositoriesAdapter

        repositoriesViewModel.getRepos(token)
        repositoriesViewModel.repositories.observe(viewLifecycleOwner) {
            repositoriesAdapter = RepositoriesAdapter(it) {clickedRepos ->
                Toast.makeText(requireContext(), "Clicked: ${clickedRepos.name}",
                Toast.LENGTH_LONG).show()
            }
            repositoriesRecyclerView.adapter = repositoriesAdapter
            repositoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            val dividerDrawable: Drawable? = requireContext().getDrawable(R.drawable.divider_for_rv)
            val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            dividerItemDecoration.setDrawable(dividerDrawable!!)
            repositoriesRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}