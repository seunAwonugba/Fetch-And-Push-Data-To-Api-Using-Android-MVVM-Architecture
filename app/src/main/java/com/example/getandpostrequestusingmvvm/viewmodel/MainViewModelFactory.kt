package com.example.getandpostrequestusingmvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.getandpostrequestusingmvvm.model.repository.Repository

/**
 * Create a class that will serve as the viewModel factory , then let it extend the viewModelFactory
 *
 * and also pass the repository class via it
 *
 * Next implement the ViewModelProvider.Factory method
 *
 * simply return the MainVieModel and pass your repository through it
 */
class MainViewModelFactory(private val mainViewModelFactoryRepository: Repository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(mainViewModelFactoryRepository) as T
    }
}