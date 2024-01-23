package ru.zhiev.githubviewer.presentation.ui.repositories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ru.zhiev.githubviewer.GitHubViewerApplication
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.TokenManager
import ru.zhiev.githubviewer.databinding.FragmentModalWindowBinding
import ru.zhiev.githubviewer.domain.models.GitHubRepositoryModel
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase

class AddReposModalWindow : Fragment() {

    private lateinit var repositoriesViewModel: RepositoriesViewModel
    private lateinit var tokenManager: TokenManager
    private var _binding: FragmentModalWindowBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val appRepository =
            (requireContext().applicationContext as GitHubViewerApplication).repository
        val workWithGitHubUseCase = WorkWithGitHubUseCase(appRepository)
        repositoriesViewModel =
            ViewModelProvider(
                this,
                RepositoriesViewModelFactory(workWithGitHubUseCase)
            )[RepositoriesViewModel::class.java]
        _binding = FragmentModalWindowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = requireActivity().supportFragmentManager
        tokenManager = TokenManager(requireContext())
        val token = tokenManager.accessToken ?: ""

        var isPrivate = false

        binding.switchPrivate.setOnCheckedChangeListener { _, isChecked ->
            Log.d("SWITCHLOG", isChecked.toString())
            if (isChecked) {
                isPrivate = true
                binding.switchPrivate.text = getString(R.string.privateRepos)
            } else {
                isPrivate = false
                binding.switchPrivate.text = getString(R.string.publicRepos)
            }
        }

        binding.createButton.setOnClickListener {
            repositoriesViewModel.createRepository(token, GitHubRepositoryModel(
                    name = "${binding.nameRepos.text}",
                    isPrivate = isPrivate,
                    description = "${binding.descriptionRepos.text}"
                )
            )
            Toast.makeText(requireContext(),getString(R.string.reqSend),Toast.LENGTH_LONG).show()
            fragmentManager.popBackStack()
        }

        binding.parentLayout.setOnClickListener {
            fragmentManager.popBackStack()
        }
    }
}