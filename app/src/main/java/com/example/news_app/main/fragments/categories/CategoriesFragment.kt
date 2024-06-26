package com.example.news_app.main.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news_app.model.Category
import com.example.news_app.main.fragments.adapters.CategoriesAdapter
import com.route.news.databinding.FragmentCategoriesBinding

class CategoriesFragment (val onCategoryClick: (Category) -> Unit): Fragment() {
    lateinit var binding: FragmentCategoriesBinding
    lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        categoriesAdapter = CategoriesAdapter(Category.categories, onCategoryClick)
        binding.categoriesRecyclerView.adapter = categoriesAdapter
    }
}