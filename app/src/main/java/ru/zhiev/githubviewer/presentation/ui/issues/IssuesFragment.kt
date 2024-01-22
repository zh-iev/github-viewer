package ru.zhiev.githubviewer.presentation.ui.issues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
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
        val issuesViewModel =
            ViewModelProvider(this,
                IssuesViewModelFactory(workWithGitHubUseCase)
            )[IssuesViewModel::class.java]

        _binding = FragmentIssuesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tokenManager = TokenManager(requireContext())
        val token = tokenManager.accessToken ?: ""

        var issuesAdapter: IssuesAdapter
        val issuesRecyclerView = binding.rvIssues

        val progressBar: ProgressBar = binding.progressBar2
        val webView: WebView = binding.issueWebView
        val flavour = CommonMarkFlavourDescriptor()
        val closeButton: FloatingActionButton = binding.closeFAButton

        issuesViewModel.getIssues(token)
        issuesViewModel.issues.observe(viewLifecycleOwner) {
            progressBar.visibility = View.GONE
            issuesAdapter = IssuesAdapter(requireContext(), it) { clickedIssue ->
                binding.progressBar2.visibility = View.VISIBLE
                val src = clickedIssue.body ?: ""
                val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(src)
                val html = HtmlGenerator(src, parsedTree, flavour).generateHtml()
                val htmlWithFormatting = html
                    .replace("\n", "<br>")
                    .replace("<img", "<img style=\"width:100%;\"")
                closeButton.visibility = View.VISIBLE
                webView.settings.javaScriptEnabled = true
                webView.webChromeClient = WebChromeClient()
                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        binding.progressBar2.visibility = View.GONE
                    }
                }

                webView.loadDataWithBaseURL(null, htmlWithFormatting, "text/html", "UTF-8", null)
                webView.visibility = View.VISIBLE

                closeButton.setOnClickListener {
                    webView.visibility = View.GONE
                    webView.loadUrl("about:blank")
                    closeButton.visibility = View.GONE
                }
            }
            issuesRecyclerView.adapter = issuesAdapter
            issuesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}