package com.ars.technologies.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class MasterViewModel (val dao: MasterDAO): ViewModel() {

    val masters = dao.getAll()

    fun addMaster(master : Master){
        viewModelScope.launch {
            dao.insert(master)
        }
    }

    fun addAll(masters : List<Master>){
        viewModelScope.launch {
            for(master in masters) {
                dao.insert(master)
            }
        }
    }

    fun updateMaster(master : Master){
        viewModelScope.launch {
            dao.update(master)
        }
    }

    fun getMaster(){
        viewModelScope.launch {
            dao.getAll()
        }
    }







}