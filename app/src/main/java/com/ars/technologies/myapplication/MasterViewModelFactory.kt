package com.ars.technologies.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class MasterViewModelFactory(private val dao:MasterDAO): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MasterViewModel::class.java)){
            return MasterViewModel(this.dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
