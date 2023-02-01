package com.ars.technologies.myapplication

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MasterDAO {

    @Insert
    suspend fun insert(master:Master)

    @Insert
    suspend fun insert(masters:List<Master>)

    @Update
    suspend fun update(maser:Master)

    @Delete
    suspend fun delete(maser:Master)

    @Query("select * from master where code=:code")
    fun get(code:String) : Master

    @Query("select * from master where code=:code and transaction_date >=:fromDate and transaction_date <=:toDate ")
    fun get(code:String, fromDate:Long, toDate: Long) : Master

    @Query("select * from master")
    fun getAll(): List<Master>


}
