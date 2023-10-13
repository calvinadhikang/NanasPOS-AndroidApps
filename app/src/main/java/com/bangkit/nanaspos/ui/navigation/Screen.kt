package com.bangkit.nanaspos.ui.navigation

sealed class Screen(val route: String){
    object Add : Screen("add")
    object Transaction : Screen("transaction")
    object DetailTransaction : Screen("transaction/{key}"){
        fun createRoute(key: Int) = "transaction/$key"
    }
    object Profile : Screen("profile")
}
