package com.bangkit.nanaspos.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bangkit.nanaspos.UserPreference
import com.bangkit.nanaspos.ui.login.LoginActivity
import com.bangkit.nanaspos.ui.main.AddScreen
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme
import com.bangkit.nanaspos.ui.transaction.Transaction
import com.bangkit.nanaspos.ui.transaction_detail.TransactionDetail
import com.bangkit.nanaspos.ui.navigation.NavigationItem
import com.bangkit.nanaspos.ui.navigation.Screen
import com.bangkit.nanaspos.ui.theme.Brown
import com.bangkit.nanaspos.ui.theme.LightBrown
import com.bangkit.nanaspos.ui.theme.White


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NanasPOSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController
            )
        }
    ) {innerPadding ->
        Box(
            modifier = modifier.padding(innerPadding)
        ){
            NavHost(
                navController = navController,
                startDestination = Screen.Transaction.route,
                modifier = modifier
            ){
                composable(Screen.Transaction.route){
                    Transaction(
                        navigateToDetail = {
                            navController.navigate(Screen.DetailTransaction.createRoute(it))
                        }
                    )
                }
                composable(Screen.Add.route){
                    AddScreen()
                }
                composable(Screen.Profile.route){
                    ProfileScreen()
                }
                composable(
                    route = Screen.DetailTransaction.route,
                    arguments = listOf(navArgument("key") {type = NavType.IntType})
                ){
                    val key = it.arguments?.getInt("key") ?: -1
                    TransactionDetail(
                        key = key,
                        navigateBack = {
                            navController.navigateUp()
                        }
                    )
//                    SearchScreen(
//                        key = key,
//                        navigateToDetail = {smeId ->
//                            navController.navigate(Screen.Detail.createRoute(smeId))
//                        }
//                    )
                }
//                composable(Screen.Favorite.route){
//                    FavoriteScreen(
//                        navigateToDetail = {smeId ->
//                            navController.navigate(Screen.Detail.createRoute(smeId))
//                        }
//                    )
//                }

//                composable(
//                    route = Screen.Detail.route,
//                    arguments = listOf(navArgument("smeId") {type = NavType.StringType})
//                ){
//                    val id = it.arguments?.getString("smeId") ?: ""
//                    DetailScreen(
//                        smeId = id,
//                        navigateBack = {
//                            navController.navigateUp()
//                        },
//                        navigateToDetail = {smeId ->
//                            navController.navigate(Screen.Detail.createRoute(smeId))
//                        }
//                    )
//                }
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Brown,
        contentColor = White,
        tonalElevation = 16.dp,
        modifier = modifier
    ) {
        val navigationItems = listOf(
            NavigationItem(
                title = "Transaksi",
                icon = Icons.Default.ShoppingCart,
                screen = Screen.Transaction
            ),
            NavigationItem(
                title = "Tambah",
                icon = Icons.Default.AddCircle,
                screen = Screen.Add
            ),
            NavigationItem(
                title = "Profil",
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            )
        )
        navigationItems.map { item ->
            BottomNavigationItem(
                label = {
                    Text(text = item.title)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                selected = currentRoute == item.screen.route,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navController.navigate(item.screen.route){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val pref = UserPreference(context).getUser()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        Text(text = "Nama : ${pref.nama}", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(text = "Divisi : ${pref.divisi}", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Button(
            onClick = {
                UserPreference(context).clearUser()
                val activity = context as Activity
                activity.startActivity(Intent(activity, LoginActivity::class.java))
            }
        ) {
            Text(text = "Logout")
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GreetingPreview4() {
    NanasPOSTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Home()
        }
    }
}