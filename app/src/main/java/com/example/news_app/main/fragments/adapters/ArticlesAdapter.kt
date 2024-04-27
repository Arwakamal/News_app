package com.route.newsc39.ui.main.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.route.newsc39.api.model.Article
import com.route.newsc39.databinding.ItemNewsBinding

class ArticlesAdapter(var articles: List<Article?>) : Adapter<ArticlesAdapter.ArticlesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticlesViewHolder(binding)
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val articleItem = articles[position]?:return
        holder.bind(articleItem )
        holder.binding.root.setOnClickListener {
            onArticleClickListener?.onArticleClick(articleItem)
        }
    }

    fun update(newList: List<Article?>){
        articles = newList
        notifyDataSetChanged()
    }

    class ArticlesViewHolder(val binding: ItemNewsBinding) : ViewHolder(binding.root) {
        fun bind(article: Article?) {
            binding.apply {
                newsDateTv.text = article?.publishedAt
                newsTitleTv.text = article?.title
                newsSourceTv.text = article?.source?.name
                Glide.with(root)
                    .load(article?.urlToImage)
                    .into(binding.newsImage)
            }
        }
    }
    private  var onArticleClickListener:OnArticleClickListener?=null
    fun setArticleClickListener (listener :OnArticleClickListener){
        onArticleClickListener=listener
    }
    fun interface OnArticleClickListener{
        fun onArticleClick(article: Article?)
    }
}