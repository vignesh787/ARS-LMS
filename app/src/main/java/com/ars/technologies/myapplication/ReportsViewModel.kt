package com.ars.technologies.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportsViewModel(transcations: Any?) : ViewModel() {

    private val transactionsLiveData: MutableLiveData<List<Transactions>> = MutableLiveData<List<Transactions>>()

    fun ReportsViewModel(transactionsList:List<Transactions>) {
       transactionsLiveData.postValue(transactionsList)
    }

    fun getTransactionList(): LiveData<List<Transactions>>? {
        return transactionsLiveData
    }
//
//    val transactions: LiveData<Transactions> = liveData
//
////    = dao.getAll()
////    val transactionString  = Transformations.map(transactions){
////            transactions->formatTransactions(transactions)
////    }

    fun formatTransactions(transactions: List<Transactions>) :String{
        return transactions.fold(""){ str, item ->str + '\n' + formatTransaction(item)
        }
    }

    fun formatTransaction(transaction: Transactions) :String{
        var str = "ID:${transaction.transactionId}"
        str +='\n' + "Name: ${transaction.code}"
        str += '\n' +"Complete: ${transaction.documentId}" + '\n'
        return str

    }
}