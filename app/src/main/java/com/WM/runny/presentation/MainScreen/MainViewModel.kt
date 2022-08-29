package com.WM.runny.presentation.MainScreen

import androidx.lifecycle.ViewModel
import com.WM.runny.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
):ViewModel() {
}