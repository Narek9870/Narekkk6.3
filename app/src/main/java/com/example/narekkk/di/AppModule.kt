package com.example.narekkk.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.narekkk.data.AuthRepositoryImpl
import com.example.narekkk.data.NetworkModule
import com.example.narekkk.data.TokenManager
import com.example.narekkk.data.UserRepositoryImpl
import com.example.narekkk.domain.AuthRepository
import com.example.narekkk.domain.UserRepository
import com.example.narekkk.presentation.AuthViewModel
import com.example.narekkk.presentation.UsersViewModel

object AppModule {
    lateinit var authRepository: AuthRepository
    lateinit var userRepository: UserRepository

    // Вызовем этот метод из MainActivity, чтобы передать Context
    fun init(context: Context) {
        val tokenManager = TokenManager(context)
        val client = NetworkModule.createClient(tokenManager)

        authRepository = AuthRepositoryImpl(client, tokenManager)
        userRepository = UserRepositoryImpl(client)
    }

    // Универсальная фабрика для ViewModel
    val viewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                return AuthViewModel(authRepository) as T
            }
            if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
                return UsersViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}