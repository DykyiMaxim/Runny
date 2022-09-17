package com.WM.runny.presentation.SetupFragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.WM.runny.R
import com.WM.runny.common.Constans.KEY_FIRST_TIME_TOGGLE
import com.WM.runny.common.Constans.KEY_NAME
import com.WM.runny.common.Constans.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fargment_setup.*
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment:Fragment(R.layout.fargment_setup) {
    @Inject
    lateinit var sharedPref:SharedPreferences

    @set:Inject
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isFirstAppOpen){
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment,true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
                )
        }

        val ToTrackingbtn = tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPreferences()
            if(success){
            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            }else{Snackbar.make(requireView(),"Please enter all the fields",Snackbar.LENGTH_LONG).show()}
        }
    }

    private fun writePersonalDataToSharedPreferences():Boolean{
        val name = etName.text.toString()
        val weight = etWeight.text.toString()
        if(name.isEmpty()||weight.isEmpty()){
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME,name)
            .putFloat(KEY_WEIGHT,weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE,false)
            .apply()
        val toolbarText = "Let's go, $name!"
        requireActivity().tvToolbarTitle.text = toolbarText
        return true

    }
}
