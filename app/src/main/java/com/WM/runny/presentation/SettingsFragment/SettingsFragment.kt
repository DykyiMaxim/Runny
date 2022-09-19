package com.WM.runny.presentation.SettingsFragment

import androidx.fragment.app.Fragment
import com.WM.runny.R
import com.WM.runny.common.Constans.KEY_NAME
import com.WM.runny.common.Constans.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar

class SettingsFragment:Fragment(R.layout.fargment_setings) {


    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFieldsFromSharedPref()
        binding.btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()
            if (success) {
                Snackbar.make(view, "Saved Changes", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(view, "Please enter all the fields", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun loadFieldsFromSharedPref() {
        val name = sharedPreferences.getString(KEY_NAME, "")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT, 80f)
        binding.etName.setText(name)
        binding.etWeight.setText(weight.toString())
    }
}