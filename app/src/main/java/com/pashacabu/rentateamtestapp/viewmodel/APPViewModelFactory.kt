package com.pashacabu.rentateamtestapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pashacabu.rentateamtestapp.data.db.AppDB

class APPViewModelFactory(private val db: AppDB) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AppDB::class.java).newInstance(db)
    }
}