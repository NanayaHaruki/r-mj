package com.nanaya.r_mj.ui;

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.MjSchoolDetailEntry
import com.nanaya.r_mj.ui.home.HomeScreen
import com.nanaya.r_mj.ui.home.MjSchoolDetailPage
import com.nanaya.r_mj.ui.home.MjSchoolDetailViewModel

@Composable
fun RmjApp() {
    val navController = rememberNavController()
    Log.d("RmjApp", "app")
    NavHost(navController = navController, startDestination = RmjScreen.MjSchoolList.route) {
        composable(RmjScreen.MjSchoolList.route) { backStackEntry: NavBackStackEntry ->
            HomeScreen(
                navigateToDetail = { id ->
                    navController.navigate(
                        RmjScreen.MjSchoolDetail.createRoute(
                            id
                        )
                    )
                }
            )
        }
        composable(
            route = RmjScreen.MjSchoolDetail.route,
            arguments = listOf(navArgument(RmjScreen.ARG_DETAIL_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(RmjScreen.ARG_DETAIL_ID) ?: 0
            val mjSchoolDetailVm :MjSchoolDetailViewModel= hiltViewModel()
            MjSchoolDetailPage() {
                navController.popBackStack()
            }

        }


    }
}

sealed class RmjScreen(val route: String) {
    data object MjSchoolList : RmjScreen("home")
    data object MjSchoolDetail : RmjScreen("detail/{$ARG_DETAIL_ID}") {
        fun createRoute(id: Int) = "detail/$id"
    }

    companion object{
        const val ARG_DETAIL_ID = "detail_id"
    }
}