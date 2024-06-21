package com.example.learningroomdatabase.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.learningroomdatabase.BaseApplication
import com.example.learningroomdatabase.data.MainDataBase
import com.example.learningroomdatabase.data.NameEntity
import kotlinx.coroutines.launch

class MainViewModel(val dataBase: MainDataBase) : ViewModel() {

    val itemsList = dataBase.dao.getAllItems()
    val newText = mutableStateOf("")
    var nameEntity: NameEntity? = null

    fun insertItem() = viewModelScope.launch {
        val nameItem = nameEntity?.copy(name = newText.value) ?: NameEntity(name = newText.value)
        dataBase.dao.insertItem(nameItem)
        nameEntity = null
        newText.value = ""
    }

    fun deleteItem(item: NameEntity) = viewModelScope.launch {
        dataBase.dao.deleteItem(item)
    }

    //constructor
    companion object{
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress ("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as BaseApplication).dataBase
                return MainViewModel(database) as T
            }
        }
    }
}