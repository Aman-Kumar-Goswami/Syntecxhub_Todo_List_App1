package com.example.todolistapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolistapp.ui.AddEditTaskScreen
import com.example.todolistapp.ui.TaskScreen
import com.example.todolistapp.utils.Constants
import com.example.todolistapp.viewmodel.TaskViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: TaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Constants.TASK_SCREEN
    ) {
        composable(route = Constants.TASK_SCREEN) {
            TaskScreen(
                viewModel = viewModel,
                onAddTaskClick = {
                    navController.navigate("${Constants.ADD_EDIT_TASK_SCREEN}/-1")
                },
                onEditTaskClick = { taskId ->
                    navController.navigate("${Constants.ADD_EDIT_TASK_SCREEN}/$taskId")
                }
            )
        }
        
        composable(
            route = "${Constants.ADD_EDIT_TASK_SCREEN}/{${Constants.TASK_ID_ARG}}",
            arguments = listOf(
                navArgument(Constants.TASK_ID_ARG) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt(Constants.TASK_ID_ARG) ?: -1
            AddEditTaskScreen(
                taskId = taskId,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
