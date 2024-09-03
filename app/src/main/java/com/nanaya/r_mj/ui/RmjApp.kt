package com.nanaya.r_mj.ui;

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nanaya.r_mj.ui.home.HomeScreen
import com.nanaya.r_mj.ui.home.MjSchoolDetailPage
import com.nanaya.r_mj.ui.home.MonthRankPage
import com.nanaya.r_mj.ui.player.PlayerPage

@Composable
fun RmjApp() {
    val navController = rememberNavController()
    Log.d("RmjApp", "app")
    NavHost(navController = navController, startDestination = RmjScreen.MjSchoolList.route) {
        composable(
            route = RmjScreen.MjSchoolList.route,
            arguments = listOf(
                navArgument(RmjScreen.ARG_LIST_CHECK){
                    type = NavType.IntType
                }
            )
        ) { backStackEntry: NavBackStackEntry ->
            HomeScreen(
                navigateToDetail = { id ->
                    navController.navigate(
                        RmjScreen.MjSchoolDetail.createRoute(
                            id
                        )
                    )
                },
                navMonthRank = {
                    navController.navigate(RmjScreen.MonthRank.route)
                }
            )
        }
        composable(
            route = RmjScreen.MjSchoolDetail.route,
            arguments = listOf(navArgument(RmjScreen.ARG_DETAIL_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            MjSchoolDetailPage(
                backNav = { navController.popBackStack() },
                searchPlayer = { name -> navController.navigate(RmjScreen.Player.createRoute(name)) }
            )
        }
        composable(
            route = RmjScreen.Player.route,
            arguments = listOf(navArgument(RmjScreen.ARG_PLAYER_NAME){type = NavType.StringType})
        ){
            PlayerPage(
                backNav = { navController.popBackStack() },
            )
        }
        composable(
            route = RmjScreen.MonthRank.route
        ){
            MonthRankPage(
                navMjSchoolList = {navController.navigate(RmjScreen.MjSchoolList.route,)},
                navPop = {navController.popBackStack()}
            )
        }
    }
}

sealed class RmjScreen(val route: String) {
    data object MjSchoolList : RmjScreen("home/{$ARG_LIST_CHECK}"){
        fun createRoute(mode:Int)= "home/$mode"
    }

    data object MjSchoolDetail : RmjScreen("detail/{$ARG_DETAIL_ID}") {
        fun createRoute(id: Int) = "detail/$id"
    }

    data object Player : RmjScreen("player/{$ARG_PLAYER_NAME}") {
        fun createRoute(name: String) = "player/$name"
    }

    data object MonthRank:RmjScreen("monthRank")

    companion object {
        const val ARG_LIST_CHECK = "list_check"
        const val ARG_DETAIL_ID = "detail_id"
        const val ARG_PLAYER_NAME = "player_name"
    }
}