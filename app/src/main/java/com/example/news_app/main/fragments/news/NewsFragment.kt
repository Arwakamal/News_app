package com.example.news_app.main.fragments.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.Gson
import com.example.news_app.api.ApiManager
import com.example.news_app.api.model.ArticlesResponse
import com.example.news_app.api.model.Constants
import com.example.news_app.api.model.Source
import com.example.news_app.api.model.SourcesResponse

import com.example.news_app.main.MainActivity
import com.example.news_app.main.fragments.adapters.ArticlesAdapter
import com.example.news_app.main.fragments.articleDetails.ArticleDetailsFragment
import com.route.news.R
import com.route.news.databinding.FragmentNewsBinding

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment(private val categoryId: String): Fragment(), OnTabSelectedListener {

    companion object{

    }
    private lateinit var binding: FragmentNewsBinding
    var adapter = ArticlesAdapter(listOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId.also { (activity as MainActivity).binding.titleTv.text = it }
        (activity as MainActivity).binding.icOpenSearchImv.isVisible=true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSources()
        initListeners()
        binding.articlesRecyclerView.adapter = adapter
    }

    private fun initListeners() {
        binding.errorView.retryBtn.setOnClickListener {
            loadSources()
        }
        binding.tabLayout.addOnTabSelectedListener(this)

        ( activity as MainActivity).setOnSearchClickListener{ query->
           loadArticlesByQuery(query)
       }
        adapter.setArticleClickListener{article->
            val arguments =Bundle()
            arguments.putParcelable(Constants.PASSED_ARTICLE,article)
            val articleDetailsFragment = ArticleDetailsFragment()
            articleDetailsFragment.arguments=arguments

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer,articleDetailsFragment)
                .addToBackStack("")
                .commit()
        }

    }

    private fun loadSources() {
        changeLoaderVisibility(true)
        changeErrorVisibility(false)
        ApiManager.getWebServices().getSources(ApiManager.apiKey, categoryId)
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    changeLoaderVisibility(false)
                    if (response.isSuccessful) {
                        response.body()?.sources.let {
                            showTabs(it!!)
                        }

                    } else {
                        changeErrorVisibility(true)
                        val errorResponse =
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                SourcesResponse::class.java
                            )
                        changeErrorVisibility(true, errorResponse.message ?: "Try again later")
                    }
                }

                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    changeLoaderVisibility(false)
                    changeErrorVisibility(
                        true,
                        t.localizedMessage ?: "Something went wrong please try again later"
                    )
                }
            })
    }

    private fun showTabs(sources: List<Source?>) {
        sources.forEach { source ->
            val tab = binding.tabLayout.newTab()
            tab.text = source?.name
            binding.tabLayout.addTab(tab)
            tab.tag = source
        }
        binding.tabLayout.getTabAt(0)?.select()

    }

    private fun changeErrorVisibility(isVisible: Boolean, message: String = "") {

    }

    private fun changeLoaderVisibility(isVisible: Boolean) {
        binding.loadingView.isVisible = isVisible
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val source = tab?.tag as Source?
        source?.id?.let {
            loadArticles(it)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {
        val source = tab?.tag as Source
        source.id?.let {
            loadArticles(it)
        }
    }

    private fun loadArticles(sourceId: String) {

        ApiManager.getWebServices().getArticles(
            ApiManager.apiKey,
            sourceId
        ).enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                changeLoaderVisibility(false)
                if (response.isSuccessful && response.body()?.articles?.isNotEmpty() == true) {
                    adapter.update(response.body()?.articles!!)
                } else {

                    changeErrorVisibility(true)
                    val errorResponse =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ArticlesResponse::class.java
                        )
                    changeErrorVisibility(true, errorResponse.message ?: "Try again later")
                }
            }
            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                changeLoaderVisibility(false)
                changeErrorVisibility(
                    true,
                    t.localizedMessage ?: "Something went wrong please try again later"
                )
            }
        })
    }

    private fun loadArticlesByQuery(query:String){
        ApiManager
            .getWebServices()
            .getArticlesByQuery(query=query)
            .enqueue(object :Callback<ArticlesResponse>{
                override fun onResponse(
                    call: Call<ArticlesResponse>,
                    response: Response<ArticlesResponse>
                ) {
                    changeLoaderVisibility(false)
                    if (response.isSuccessful && response.body()?.articles?.isNotEmpty() ==true){
                        adapter.update(response.body()?.articles!!)
                    }else{
                        changeErrorVisibility(true)
                        val errorResponse =
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ArticlesResponse::class.java
                            )
                        changeErrorVisibility(true, errorResponse.message ?: "Try again later")
                    }
                }

                override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                    changeLoaderVisibility(false)
                    changeErrorVisibility(
                        true,
                        t.localizedMessage ?: "Something went wrong please try again later"
                    )
                }

            })
    }
}