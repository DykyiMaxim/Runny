package com.WM.runny.presentation.SettingsFragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.WM.runny.R
import com.WM.runny.common.Constans.KEY_NAME
import com.WM.runny.common.Constans.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fargment_setings.*
import kotlinx.android.synthetic.main.fargment_setup.*
import kotlinx.android.synthetic.main.fargment_setup.etName
import kotlinx.android.synthetic.main.fargment_setup.etWeight
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment:Fragment(R.layout.fargment_setings) {


    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadfeildsFromSharedPref()
        btnApplyChanges.setOnClickListener {
            val success =  applyChangesToSharedPreferences()
            if(success){
                Snackbar.make(view,"Saved changes",Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(view,"Make sure you fill all field's ",Snackbar.LENGTH_LONG).show()
            }
        }
    }


    private fun loadfeildsFromSharedPref(){
        val name = sharedPreferences.getString(KEY_NAME,"")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT,80f)
        etName.setText(name)
        etWeight.setText(weight.toString())

    }

    private fun applyChangesToSharedPreferences():Boolean{
        val nameText = etName.text.toString()
        val weightText = etWeight.text.toString()
        if(nameText.isEmpty() || weightText.isEmpty() ){
            return false
        }
        sharedPreferences.edit()
            .putString(KEY_NAME,nameText)
            .putFloat(KEY_WEIGHT,weightText.toFloat())
            .apply()
        val toolbarText =  "Let's go $nameText"
        requireActivity().tvToolbarTitle.text = toolbarText
        return true

    }


}