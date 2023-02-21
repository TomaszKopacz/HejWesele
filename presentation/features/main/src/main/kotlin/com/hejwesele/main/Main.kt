//package com.hejwesele.main
//
//import androidx.compose.animation.ExperimentalAnimationApi
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.asPaddingValues
//import androidx.compose.foundation.layout.navigationBars
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.BottomNavigationItem
//import androidx.compose.material.Icon
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.SideEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.stringResource
//import androidx.navigation.NavDestination.Companion.hierarchy
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.compose.currentBackStackEntryAsState
//import com.google.accompanist.insets.ui.BottomNavigation
//import com.google.accompanist.insets.ui.Scaffold
//import com.google.accompanist.navigation.animation.AnimatedNavHost
//import com.google.accompanist.navigation.animation.composable
//import com.google.accompanist.navigation.animation.rememberAnimatedNavController
//import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
//import com.google.accompanist.navigation.material.ModalBottomSheetLayout
//import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
//import com.google.accompanist.systemuicontroller.rememberSystemUiController
//import com.hejwesele.android.theme.Transitions
//import com.hejwesele.dashboard.Dashboard
//import com.hejwesele.settings.settingsGraph
//
//@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
//@Composable
//fun Main(appName: String, userId: Int) {
//    val systemUiController = rememberSystemUiController()
//    SideEffect {
//        systemUiController.setNavigationBarColor(Color.Transparent, darkIcons = false)
//    }
//
//    val bottomSheetNavigator = rememberBottomSheetNavigator()
//    val navController = rememberAnimatedNavController(bottomSheetNavigator)
//
//    Scaffold(
//        bottomBar = {
//            BottomNavigation(contentPadding = WindowInsets.navigationBars.asPaddingValues()) {
//                val backStackEntry by navController.currentBackStackEntryAsState()
//                val currentDestination = backStackEntry?.destination
//                bottomNavigationItems.forEach { item ->
//                    BottomNavigationItem(
//                        icon = { Icon(item.icon, contentDescription = null) },
//                        label = { Text(stringResource(item.label)) },
//                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
//                        onClick = {
//                            navController.navigate(item.route) {
//                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
//                                launchSingleTop = true
//                                restoreState = true
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    ) { innerPadding ->
//        ModalBottomSheetLayout(
//            bottomSheetNavigator = bottomSheetNavigator,
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            AnimatedNavHost(
//                navController = navController,
//                startDestination = MainRoutes.dashboard,
//                enterTransition = { Transitions.fadeIn },
//                exitTransition = { Transitions.fadeOut }
//            ) {
//                composable(MainRoutes.dashboard) { Dashboard(appName, userId) }
//                settingsGraph(MainRoutes.settings, navController)
//            }
//        }
//    }
//}
