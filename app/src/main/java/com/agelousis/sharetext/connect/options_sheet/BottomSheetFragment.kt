package com.agelousis.sharetext.connect.options_sheet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import com.agelousis.sharetext.R
import com.agelousis.sharetext.custom.bottom_sheet.RoundedBottomSheetDialogFragment
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.extensions.isNightMode
import kotlinx.android.synthetic.main.options_sheet_fragment_layout.view.*

class BottomSheetFragment: RoundedBottomSheetDialogFragment() {

    companion object {
        fun show(supportFragmentManager: FragmentManager) {
            with(BottomSheetFragment()) {
                retainInstance = true
                this
            }.show(supportFragmentManager, Constants.OPTIONS_BOTTOM_SHEET_TAG)
        }
    }

    private val sharedPreferences: SharedPreferences? by lazy { context?.getSharedPreferences(Constants.PREFERENCES_TAG, Context.MODE_PRIVATE) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.options_sheet_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureDarkMode(view = view)
        configureIPAddress(view = view)
    }

    private fun configureDarkMode(view: View) {
        view.darkModeLayout.setOnClickListener { view.darkModeSwitchButton.isChecked = !view.darkModeSwitchButton.isChecked }
        view.darkModeSwitchButton.isChecked = sharedPreferences?.isNightMode == 1 || context?.isNightMode == 1
        view.darkModeSwitchButton.setOnCheckedChangeListener { _, isChecked ->
            dismiss()
            with(sharedPreferences?.edit()) {
                this?.putInt(Constants.DARK_MODE_VALUE, if (isChecked) 1 else 0)
                this?.apply()
            }
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun configureIPAddress(view: View) {
        view.rememberIPAddressLayout.setOnClickListener { view.rememberIPAddressSwitchButton.isChecked = !view.rememberIPAddressSwitchButton.isChecked }
        view.rememberIPAddressSwitchButton.isChecked = sharedPreferences?.getBoolean(Constants.REMEMBER_IP_ADDRESS_VALUE, false) ?: false
        view.rememberIPAddressSwitchButton.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPreferences?.edit()) {
                this?.putBoolean(Constants.REMEMBER_IP_ADDRESS_VALUE, isChecked)
                this?.apply()
            }
        }
    }

}