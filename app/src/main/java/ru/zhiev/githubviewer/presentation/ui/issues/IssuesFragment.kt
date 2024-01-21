package ru.zhiev.githubviewer.presentation.ui.issues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.zhiev.githubviewer.GitHubViewerApplication
import ru.zhiev.githubviewer.TokenManager
import ru.zhiev.githubviewer.databinding.FragmentIssuesBinding
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase

class IssuesFragment : Fragment() {

    private var _binding: FragmentIssuesBinding? = null
    private lateinit var tokenManager: TokenManager

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appRepository = (requireContext().applicationContext as GitHubViewerApplication).repository
        val workWithGitHubUseCase = WorkWithGitHubUseCase(appRepository)
        val slideshowViewModel =
            ViewModelProvider(this,
                SlideshowViewModelFactory(workWithGitHubUseCase)
            )[SlideshowViewModel::class.java]

        _binding = FragmentIssuesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tokenManager = TokenManager(requireContext())
        val token = tokenManager.accessToken ?: ""

        slideshowViewModel.getIssues(token)
        slideshowViewModel.issues.observe(viewLifecycleOwner) {
            binding.textSlideshow.text = slideshowViewModel.issues.value.toString()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}