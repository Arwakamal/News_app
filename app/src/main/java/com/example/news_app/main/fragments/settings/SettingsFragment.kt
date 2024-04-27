package com.route.newsc39.ui.main.fragments.settings

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.route.newsc39.R
import com.route.newsc39.api.model.Constants
import com.route.newsc39.databinding.FragmentSettingsBinding
import com.route.newsc39.ui.main.MainActivity
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //checkAndSetSettingsPreferences()
        setupExposedDropdownMenus()

        binding.languageActv.doOnTextChanged { text, _, _, _ ->
            activateArabicLang(
                when (text.toString()) {
                    resources.getString(R.string.arabic_language) -> true
                    else -> false
                }
            )
        }

     //   settingsSP =activity?.getSharedPreferences(Constants.SETTINGS_FILE_NAME, Context.MODE_PRIVATE)


    }
//
//    private fun checkAndSetSettingsPreferences() {
//
//        activateDarkMode(settingsSP?.getBoolean(Constants.MODE_KEY,false) ?:false)
//    }

    private fun activateArabicLang(activate: Boolean) {
        setLocale(if (activate) Constants.ARABIC_CODE else Constants.ENGLISH_CODE)
       // activity?.let { ActivityCompat.recreate(it) }
        startActivity(Intent(requireActivity(),MainActivity::class.java))
        requireActivity().finish()

    }



    private fun populateMenusWithOptions() {
        binding.languageActv.setText(
            when (isArabicLangActive()) {
                true -> resources.getString(R.string.arabic_language)
                false -> resources.getString(R.string.english_language)
            }, false
        )

    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        activity?.let {
            @Suppress("DEPRECATION")
            requireActivity().baseContext.resources.updateConfiguration(
                config,
                requireActivity().baseContext.resources.displayMetrics
            )
        }
    }



    private fun isArabicLangActive(): Boolean {
        return when (context?.resources?.configuration?.locales!![0].language) {
            Constants.ARABIC_CODE -> true
            else -> false
        }
    }

    private fun setupExposedDropdownMenus() {
        // setup
        val langItems = listOf(
            resources.getString(R.string.english_language),
            resources.getString(R.string.arabic_language)
        )
        val lanAdapter =
            ArrayAdapter(requireContext(), R.layout.item_settings_dropdown_menus, langItems)
        binding.languageActv.setAdapter(lanAdapter)

        // populate
        populateMenusWithOptions()
    }

}