package com.hisabak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.presentation.edit.BrandEditRoute
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.presentation.edit.CategoryEditRoute
import com.hisabak.feature.dashboard.presentation.DashboardRoute
import com.hisabak.feature.sms.presentation.inbox.SmsInboxRoute
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.feature.transaction.presentation.edit.TransactionEditRoute
import com.hisabak.feature.transaction.presentation.list.TransactionListFilterBus
import com.hisabak.feature.transaction.presentation.list.TransactionListFilterRequest
import com.hisabak.feature.transaction.presentation.list.TransactionListRoute
import com.hisabak.nav.BottomSheetSceneStrategy
import com.hisabak.nav.BrandEditKey
import com.hisabak.nav.CategoryEditKey
import com.hisabak.nav.DashboardKey
import com.hisabak.nav.ManageKey
import com.hisabak.nav.Navigator
import com.hisabak.nav.SmsKey
import com.hisabak.nav.TransactionEditKey
import com.hisabak.nav.TransactionsKey
import com.hisabak.nav.rememberNavigationState
import com.hisabak.nav.toEntries
import com.hisabak.ui.components.BottomNavTab
import com.hisabak.ui.components.DetailTopBar
import com.hisabak.ui.components.HisabakBottomNav
import com.hisabak.ui.components.HisabakTopBar
import com.hisabak.ui.components.clearFocusOnTap
import com.hisabak.ui.theme.HisabakTheme
import org.koin.compose.koinInject

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

private enum class RootTab(
    val key: NavKey,
    val label: String,
    val icon: ImageVector,
    val iconOutlined: ImageVector,
) {
    Dashboard(DashboardKey, "Dashboard", Icons.Filled.SpaceDashboard, Icons.Outlined.SpaceDashboard),
    Transactions(TransactionsKey, "Transactions", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List),
    Sms(SmsKey, "SMS", Icons.AutoMirrored.Filled.Message, Icons.AutoMirrored.Outlined.Message),
    Manage(ManageKey, "Manage", Icons.Filled.Layers, Icons.Outlined.Layers),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HisabakNav() {
    val navigationState = rememberNavigationState(
        startRoute = DashboardKey,
        topLevelRoutes = RootTab.entries.map { it.key },
    )
    val navigator = remember { Navigator(navigationState) }
    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }
    val filterBus = koinInject<TransactionListFilterBus>()

    val tabs = remember {
        RootTab.entries.map {
            BottomNavTab(key = it.name, label = it.label, icon = it.icon, iconOutlined = it.iconOutlined)
        }
    }

    val currentTab = RootTab.entries.first { it.key == navigationState.topLevelRoute }
    // The transaction add/edit screen is an overlay bottom sheet (tab chrome stays behind it).
    // Brand/Category edits are full-screen pages with a back arrow and no bottom nav.
    val leaf = navigationState.backStacks[navigationState.topLevelRoute]?.lastOrNull()
    val fullScreenEdit = leaf is BrandEditKey || leaf is CategoryEditKey

    Scaffold(
        topBar = {
            when (leaf) {
                is CategoryEditKey -> DetailTopBar(
                    title = if (leaf.id == null) "New category" else "Edit category",
                    onBack = { navigator.goBack() },
                )
                is BrandEditKey -> DetailTopBar(
                    title = if (leaf.id == null) "New brand" else "Edit brand",
                    onBack = { navigator.goBack() },
                )
                else -> HisabakTopBar(
                    title = when (currentTab) {
                        RootTab.Dashboard -> "Hisabak"
                        RootTab.Transactions -> "Transactions"
                        RootTab.Sms -> "SMS Inbox"
                        RootTab.Manage -> "Manage"
                    },
                )
            }
        },
        bottomBar = {
            if (!fullScreenEdit) {
                HisabakBottomNav(
                    tabs = tabs,
                    selectedKey = currentTab.name,
                    onSelect = { key -> navigator.navigate(RootTab.valueOf(key).key) },
                )
            }
        },
        floatingActionButton = {
            if (leaf == TransactionsKey) {
                FloatingActionButton(
                    onClick = { navigator.navigate(TransactionEditKey(id = null)) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add transaction")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        val entryProvider = entryProvider<NavKey> {
            entry<DashboardKey> {
                DashboardRoute(
                    onShowUncategorized = {
                        filterBus.request(TransactionListFilterRequest.Uncategorized)
                        navigator.navigate(TransactionsKey)
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
            entry<TransactionsKey> {
                TransactionListRoute(
                    onAdd = { navigator.navigate(TransactionEditKey(id = null)) },
                    onEdit = { id -> navigator.navigate(TransactionEditKey(id = id.value)) },
                )
            }
            entry<SmsKey> {
                SmsInboxRoute(modifier = Modifier.fillMaxSize())
            }
            entry<ManageKey> {
                ManageRoute(
                    modifier = Modifier.fillMaxSize(),
                    onAddBrand = { navigator.navigate(BrandEditKey(id = null)) },
                    onEditBrand = { id -> navigator.navigate(BrandEditKey(id = id.value)) },
                    onAddCategory = { navigator.navigate(CategoryEditKey(id = null)) },
                    onEditCategory = { id -> navigator.navigate(CategoryEditKey(id = id.value)) },
                )
            }
            entry<TransactionEditKey>(metadata = BottomSheetSceneStrategy.bottomSheet()) { key ->
                TransactionEditRoute(
                    transactionId = key.id?.let(::TransactionId),
                    onDone = { navigator.goBack() },
                    onCancel = { navigator.goBack() },
                )
            }
            entry<BrandEditKey> { key ->
                BrandEditRoute(
                    brandId = key.id?.let(::BrandId),
                    onDone = { navigator.goBack() },
                    onCancel = { navigator.goBack() },
                )
            }
            entry<CategoryEditKey> { key ->
                CategoryEditRoute(
                    categoryId = key.id?.let(::CategoryId),
                    onDone = { navigator.goBack() },
                    onCancel = { navigator.goBack() },
                )
            }
        }

        // No custom NavDisplay transitions: the edit screens are bottom-sheet overlays, and a
        // NavDisplay scene transition fights the sheet's own open/close animation (it made the
        // sheet snap back to full and oscillate on dismiss). Sheets animate themselves; tab
        // switches use NavDisplay's defaults.
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { navigator.goBack() },
            sceneStrategy = bottomSheetStrategy.then(SinglePaneSceneStrategy()),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clearFocusOnTap(),
        )
    }
}
