package com.example.newsapp.ui.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentDetailBinding
import com.example.newsapp.ui.news.NewsViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()

    lateinit var article: Article

    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        article = args.article
        isFavorite = args.favorite
        bind()
    }

    private fun bind() {
        binding.apply {
            newsTitle.text = article.title
            newsText.text = article.content
            newsSource.text = article.source?.name
            newsDate.text = article.publishedAt
            Picasso.get().load(article.urlToImage).into(newsImage)

            goToSource.setOnClickListener {
                val action =
                    DetailFragmentDirections.actionNavigationDetailToArticleWebViewFragment(article.url)
                findNavController().navigate(action)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}