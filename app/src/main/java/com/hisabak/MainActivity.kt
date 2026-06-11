package com.hisabak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import com.hisabak.ui.components.DetailTopBar
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.hisabak.feature.dashboard.presentation.DashboardRoute
import com.hisabak.feature.sms.presentation.inbox.SmsInboxRoute
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.feature.transaction.presentation.edit.TransactionEditRoute
import com.hisabak.feature.transaction.presentation.list.TransactionListRoute
import com.hisabak.ui.components.BottomNavTab
import com.hisabak.ui.components.HisabakBottomNav
import com.hisabak.ui.components.HisabakTopBar
import com.hisabak.ui.theme.HisabakTheme

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

private enum class RootTab(val label: String, val icon: ImageVector, val iconOutlined: ImageVector) {
    Dashboard("Dashboard",       Icons.Filled.SpaceDashboard,       Icons.Outlined.SpaceDashboard),
    Transactions("Transactions", Icons.AutoMirrored.Filled.List,    Icons.AutoMirrored.Outlined.List),
    Sms("SMS",                   Icons.AutoMirrored.Filled.Message,  Icons.AutoMirrored.Outlined.Message),
    Manage("Manage",             Icons.Filled.Layers,                Icons.Outlined.Layers),
}

private sealed interface TransactionsNav {
    data object List : TransactionsNav
    data class Edit(val id: TransactionId?) : TransactionsNav
}

@Composable
private fun HisabakNav() {
    var currentTab by rememberSaveable { mutableStateOf(RootTab.Dashboard) }
    var txNav: TransactionsNav by remember { mutableStateOf(TransactionsNav.List) }

    // ManageRoute surfaces its detail state here so the Scaffold can render the right top bar
    var manageDetail by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }

    val tabs = remember {
        RootTab.entries.map { BottomNavTab(key = it.name, label = it.label, icon = it.icon, iconOutlined = it.iconOutlined) }
    }

    val txEditNav = txNav as? TransactionsNav.Edit
    val isOnDetail = txEditNav != null || manageDetail != null

    Scaffold(
        topBar = {
            when {
                txEditNav != null -> DetailTopBar(
                    title = if (txEditNav.id == null) "New transaction" else "Edit transaction",
                    onBack = { txNav = TransactionsNav.List },
                )
                manageDetail != null -> DetailTopBar(
                    title = manageDetail!!.first,
                    onBack = manageDetail!!.second,
                )
                else -> HisabakTopBar(
                    title = when (currentTab) {
                        RootTab.Dashboard    -> "Hisabak"
                        RootTab.Transactions -> "Transactions"
                        RootTab.Sms          -> "SMS Inbox"
                        RootTab.Manage       -> "Manage"
                    },
                )
            }
        },
        bottomBar = {
            if (!isOnDetail) {
                HisabakBottomNav(
                    tabs = tabs,
                    selectedKey = currentTab.name,
                    onSelect = { key -> currentTab = RootTab.valueOf(key) },
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        val tabModifier = Modifier.fillMaxSize().padding(padding)
        when (currentTab) {
            RootTab.Dashboard    -> DashboardRoute(modifier = tabModifier)
            RootTab.Transactions -> TransactionsGraph(
                nav = txNav,
                onNavChange = { txNav = it },
                modifier = tabModifier,
            )
            RootTab.Sms          -> SmsInboxRoute(modifier = tabModifier)
            RootTab.Manage       -> ManageRoute(
                modifier = tabModifier,
                onDetailEnter = { title, back -> manageDetail = title to back },
                onDetailExit  = { manageDetail = null },
            )
        }
    }
}

@Composable
private fun TransactionsGraph(
    nav: TransactionsNav,
    onNavChange: (TransactionsNav) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        when (nav) {
            TransactionsNav.List -> TransactionListRoute(
                onAdd = { onNavChange(TransactionsNav.Edit(id = null)) },
                onEdit = { id -> onNavChange(TransactionsNav.Edit(id = id)) },
            )
            is TransactionsNav.Edit -> TransactionEditRoute(
                transactionId = nav.id,
                onDone = { onNavChange(TransactionsNav.List) },
                onCancel = { onNavChange(TransactionsNav.List) },
            )
        }
    }
}

