package ru.zhiev.githubviewer.presentation.ui.issues

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ru.zhiev.githubviewer.GitHubViewerApplication
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.TokenManager
import ru.zhiev.githubviewer.databinding.CreateIssueModalBinding
import ru.zhiev.githubviewer.domain.models.GitHubRepositoryModel
import ru.zhiev.githubviewer.domain.models.IssueModel
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase
import ru.zhiev.githubviewer.presentation.main.MainViewModel
import ru.zhiev.githubviewer.presentation.main.MainViewModelFactory

class AddIssueModalWindow() : Fragment() {

    private lateinit var issuesViewModel: IssuesViewModel
    private lateinit var tokenManager: TokenManager
    private var _binding: CreateIssueModalBinding? = null
    private lateinit var activityViewModel: MainViewModel

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
        issuesViewModel =
            ViewModelProvider(
                this,
                IssuesViewModelFactory(workWithGitHubUseCase)
            )[IssuesViewModel::class.java]
        activityViewModel =
            ViewModelProvider(
                requireActivity(),
                MainViewModelFactory(workWithGitHubUseCase),
            )[MainViewModel::class.java]
        _binding = CreateIssueModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = parentFragmentManager
        tokenManager = TokenManager(requireContext())
        val token = tokenManager.accessToken ?: ""


        val repositories = activityViewModel.repos.value.orEmpty()
        initSpinnerRepos(repositories)

        binding.createButton.setOnClickListener {
            val selectedRepoName = binding.repositoriesSpinner.selectedItem
            val selectedRepository = repositories.find { it.name == selectedRepoName }
            issuesViewModel.createIssue(
                token,
                activityViewModel.userData.value!!,
                IssueModel(
                    title = binding.nameIssue.text.toString(),
                    body = binding.issueBody.text.toString(),
                    repository = selectedRepository!!
                ))
            Toast.makeText(requireContext(), getString(R.string.reqSend), Toast.LENGTH_LONG).show()
            fragmentManager.popBackStack()
        }

        binding.parentLayout.setOnClickListener {
            fragmentManager.popBackStack()
        }
    }

    private fun initSpinnerRepos(repositories: List<GitHubRepositoryModel>) {
        val repositoriesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            repositories.map { it.name }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.repositoriesSpinner.adapter = repositoriesAdapter
    }
}