package com.ars.technologies.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ReportsViewModelFactory(private val dao:TransactionsDAO): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReportsViewModel::class.java)){
            return ReportsViewModel(this.dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
