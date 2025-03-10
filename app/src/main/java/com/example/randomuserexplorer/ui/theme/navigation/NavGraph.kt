package com.example.randomuserexplorer.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.randomuserexplorer.screens.UserListScreen
import com.example.randomuserexplorer.screens.UserInfoDetailsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { UserListScreen(navController) }
        composable("detail/{userJson}") { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson")
            UserInfoDetailsScreen(navController,userJson)
        }
    }
}