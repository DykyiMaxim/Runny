package com.WM.runny.presentation.MainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.WM.runny.data.repository.MainRepository
import com.WM.runny.domain.run.Run
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
):ViewModel() {

    fun InsertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}