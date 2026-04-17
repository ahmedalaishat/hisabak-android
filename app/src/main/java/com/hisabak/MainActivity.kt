package com.hisabak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.feature.transaction.presentation.edit.TransactionEditScreen
import com.hisabak.feature.transaction.presentation.edit.TransactionEditViewModel
import com.hisabak.feature.transaction.presentation.list.TransactionListScreen
import com.hisabak.feature.transaction.presentation.list.TransactionListViewModel
import com.hisabak.ui.theme.HisabakTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HisabakTheme {
                HisabakNav()
            }
        }
    }
}

private sealed interface Screen {
    data object List : Screen
    data class Edit(val id: TransactionId?) : Screen
}

@Composable
private fun HisabakNav() {
    var screen: Screen by remember { mutableStateOf(Screen.List) }

    when (val current = screen) {
        Screen.List -> {
            val vm: TransactionListViewModel = koinViewModel()
            TransactionListScreen(
                viewModel = vm,
                onAdd = { screen = Screen.Edit(id = null) },
                onEdit = { id -> screen = Screen.Edit(id = id) },
            )
        }
        is Screen.Edit -> {
            val vm: TransactionEditViewModel = koinViewModel(
                key = current.id?.value ?: "new",
                parameters = { parametersOf(current.id) },
            )
            TransactionEditScreen(
                viewModel = vm,
                onDone = { screen = Screen.List },
                onCancel = { screen = Screen.List },
            )
        }
    }
}
