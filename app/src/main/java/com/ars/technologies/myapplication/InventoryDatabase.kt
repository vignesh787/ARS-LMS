package com.ars.technologies.myapplication

import android.content.Context
import androidx.room.*

@Database(entities=[Transactions::class,Master::class],version=12,exportSchema=false)
@TypeConverters(Converters::class)
abstract class InventoryDatabase :RoomDatabase() {
    abstract val transactionsDAO:TransactionsDAO

    abstract val masterDAO:MasterDAO

    companion object{
        @Volatile
        private var INSTANCE:InventoryDatabase? = null
        fun getInstance(context: Context):InventoryDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance== null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        InventoryDatabase::class.java,
                        "inventory_database"
                    ).fallbackToDestructiveMigration()
                      .allowMainThreadQueries()
                      .build()
                    INSTANCE=instance
                }
                return instance
            }
        }
    }
}