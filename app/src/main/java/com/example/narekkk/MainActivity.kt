package com.example.narekkk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.narekkk.di.AppModule
import com.example.narekkk.navigation.AppNavigation
import com.example.narekkk.presentation.AuthViewModel
import com.example.narekkk.presentation.UsersViewModel
import com.example.narekkk.ui.theme.NarekkkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализируем наш модуль и передаем ему(чтобы DataStore работал)
        AppModule.init(applicationContext)

        enableEdgeToEdge()
        setContent {
            NarekkkTheme {
                //  Создаем ViewModel-и
                val authViewModel: AuthViewModel = viewModel(factory = AppModule.viewModelFactory)
                val usersViewModel: UsersViewModel = viewModel(factory = AppModule.viewModelFactory)

                //  Подписываемся на Токен из DataStore
                var isReady by remember { mutableStateOf(false) }
                var hasToken by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    AppModule.authRepository.getAuthToken().collect { token ->
                        hasToken = (token != null)
                        isReady = true // Как только мы прочитали память, можно рисовать UI
                    }
                }

                // Показываем крутилку, пока читаем DataStore, а потом - экраны
                if (!isReady) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    AppNavigation(
                        hasToken = hasToken,
                        authViewModel = authViewModel,
                        usersViewModel = usersViewModel
                    )
                }
            }
        }
    }
}
