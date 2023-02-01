package com.ars.technologies.myapplication

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionsDAO {

    @Insert
    suspend fun insert(transaction:Transactions) : Long

    @Insert
    suspend fun insert(transactions:List<Transactions>)

    @Update
    suspend fun update(transaction:Transactions)

    @Delete
    suspend fun delete(transaction:Transactions)

    @Query("select * from transactions where code=:code and transaction_date < :toDate")
    fun getPrevTxn(code:String,toDate:Long) : List<Transactions>


    @Query("select * from transactions where code=:code")
    fun get(code:String) : List<Transactions>

    @Query("select * from transactions")
    fun getAll(): LiveData<List<Transactions>>

    @Query("SELECT * FROM transactions where transactionType =:transactionType and transaction_date >=:fromDate and transaction_date <=:toDate ")
    fun getTransactionByParams(fromDate:Long, toDate: Long, transactionType:String): List<Transactions>

    @Query("SELECT * FROM transactions where transaction_date >=:fromDate and transaction_date <=:toDate ")
    fun getTransactionForSummary(fromDate:Long, toDate: Long): List<Transactions>


    @Query("SELECT * FROM transactions where code=:code and transaction_date >=:fromDate and transaction_date <=:toDate ")
    fun getTransactionForCodeAndDateRange(code:String, fromDate:Long, toDate: Long): List<Transactions>

}