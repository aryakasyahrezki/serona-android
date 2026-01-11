package com.example.serona.ui.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.serona.ui.ui.auth.AuthViewModel
import com.example.serona.ui.ui.navigation.Routes

@Composable
fun HomePage(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ){
            Text("HomePage")
            Spacer(modifier = Modifier.height(6.dp))
            Text("HomePage")
            Button(
                onClick = {
                    authViewModel.logout()
                    navController.navigate("login"){
                        popUpTo(Routes.SPLASH){
                            inclusive = true
                        }
                    }
                },
            ) {
                Text("Logout")
            }
        }
    }

}

// home harus fetch data lagi kalo dia baru pertama login

//HomeViewModel
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val userRepository: UserRepository,
//    private val userSession: UserSession
//) : ViewModel() {
//
//    init {
//        if (!userSession.isInitialized()) {
//            viewModelScope.launch {
//                val result = userRepository.getProfile()
//                if (result.isSuccess) {
//                    userSession.setUser(result.getOrNull()!!)
//                }
//            }
//        }
//    }
//}