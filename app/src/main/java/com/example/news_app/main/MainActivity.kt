package com.example.news_app.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

import com.example.news_app.main.fragments.categories.CategoriesFragment
import com.example.news_app.main.fragments.news.NewsFragment
import com.example.news_app.main.fragments.settings.SettingsFragment
import com.route.news.R
import com.route.news.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var categoriesFragment: CategoriesFragment
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolBar)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, binding.root,
            R.string.drawer_open, R.string.drawer_close
        )
        binding.root.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initFragments()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, categoriesFragment, "")
            .commit()
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.settingsSideMenuItem -> {
                    showFragment(SettingsFragment())
                }

                R.id.categoriesSideMenuItem -> {
                    showFragment(categoriesFragment)
                }
            }
            binding.root.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

        binding.icOpenSearchImv.setOnClickListener {
            showSearchView(true)
        }
        binding.icCloseImv.setOnClickListener {
            showSearchView(false)
        }


        binding.searchEdt
            .setOnEditorActionListener { view, actionId, event ->
                onSearchClickListener?.onSearchClick(view.text.toString())
                view.text = ""
                true
            }
    }
    private fun showSearchView(show: Boolean) {
        binding.titleTv.isVisible=!show
        binding.icOpenSearchImv.isVisible=!show
        binding.searchViewCv.isVisible=show
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, "")
            .commit()
    }

    private fun initFragments() {
        categoriesFragment = CategoriesFragment {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, NewsFragment(it.id), "")
                .addToBackStack("")
                .commit()

        }
    }

    private  var onSearchClickListener: OnSearchClickListener? = null
    fun setOnSearchClickListener(listener: OnSearchClickListener){
        onSearchClickListener =listener
    }
    fun interface OnSearchClickListener{
       fun onSearchClick(query : String)
    }
}
