package com.nocholla.nyc.schools.koin.retrofit.compose.ui.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.presentation.theme.NYCSchoolsTheme
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.presentation.view.screens.SchoolDetailScreen
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.presentation.view.screens.SchoolListScreen
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.presentation.view.screens.ScoresScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NYCSchoolsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "schoolList",
                        modifier = Modifier.systemBarsPadding()
                    ) {
                        composable("schoolList") { SchoolListScreen(navController) }
                        composable("schoolDetail/{dbn}") { backStackEntry ->
                            SchoolDetailScreen(
                                navController, backStackEntry.arguments?.getString("dbn")
                            )
                        }
                        composable("scores/{dbn}") { backStackEntry ->
                            ScoresScreen(
                                navController, backStackEntry.arguments?.getString("dbn")
                            )
                        }
                    }
                }
            }
        }
    }
}