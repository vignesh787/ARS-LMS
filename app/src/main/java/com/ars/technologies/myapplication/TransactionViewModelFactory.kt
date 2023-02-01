package com.ars.technologies.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class TransactionViewModelFactory(private val dao:TransactionsDAO): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TransactionViewModel::class.java)){
            return TransactionViewModel(this.dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}