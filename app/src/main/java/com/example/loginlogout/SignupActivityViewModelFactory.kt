package com.example.loginlogout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignupActivityViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignupActivityViewModel(repository) as T
    }

}