package com.hisabak

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.presentation.BrandEditBus
import com.hisabak.feature.brand.presentation.edit.BrandEditRoute
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.presentation.edit.CategoryEditRoute
import com.hisabak.core.data.preferences.AppLocale
import com.hisabak.core.domain.AppPreferences
import com.hisabak.core.domain.ThemeMode
import com.hisabak.core.domain.analytics.Analytics
import com.hisabak.feature.dashboard.presentation.CategoryFocusBus
import com.hisabak.feature.dashboard.presentation.DashboardRoute
import com.hisabak.feature.onboarding.presentation.OnboardingRoute
import com.hisabak.feature.settings.presentation.SettingsRoute
import com.hisabak.feature.notification.domain.NotificationRepository
import com.hisabak.feature.notification.platform.SystemNotifier
import com.hisabak.feature.notification.presentation.list.NotificationsRoute
import com.hisabak.feature.sms.presentation.inbox.SmsInboxRoute
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.feature.transaction.presentation.edit.TransactionEditRoute
import com.hisabak.feature.transaction.presentation.list.TransactionListFilterBus
import com.hisabak.feature.transaction.presentation.list.TransactionListFilterRequest
import com.hisabak.feature.transaction.presentation.list.TransactionListRoute
import com.hisabak.feature.backup.presentation.BackupRoute
import com.hisabak.nav.BackupKey
import com.hisabak.nav.BottomSheetSceneStrategy
import com.hisabak.nav.BrandEditKey
import com.hisabak.nav.CategoryEditKey
import com.hisabak.nav.DashboardKey
import com.hisabak.nav.ManageKey
import com.hisabak.nav.Navigator
import com.hisabak.nav.NotificationsKey
import com.hisabak.nav.SettingsKey
import com.hisabak.nav.SmsKey
import com.hisabak.nav.TransactionEditKey
import com.hisabak.nav.TransactionsKey
import com.hisabak.nav.rememberNavigationState
import com.hisabak.nav.toEntries
import com.hisabak.security.AppLockGate
import com.hisabak.ui.components.BottomNavTab
import com.hisabak.ui.components.DetailTopBar
import com.hisabak.ui.components.HisabakBottomNav
import com.hisabak.ui.components.HisabakTopBar
import com.hisabak.ui.components.clearFocusOnTap
import com.hisabak.ui.theme.HisabakTheme
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

// FragmentActivity (not plain ComponentActivity) is required by androidx.biometric's BiometricPrompt;
// it still extends ComponentActivity (so Navigation 3 keeps its dispatcher owner) and pulls in
// androidx.fragment, not appcompat — preserving the app's no-appcompat constraint.
class MainActivity : FragmentActivity() {

    private val categoryFocusBus: CategoryFocusBus by inject()
    private val brandEditBus: BrandEditBus by inject()

    // Apply the saved app language before any resources are resolved.
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLocale.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleFocusIntent(intent)
        setContent {
            val preferences = koinInject<AppPreferences>()
            val themeMode by preferences.themeMode.collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            HisabakTheme(darkTheme = darkTheme) {
                val onboardingCompleted by preferences.onboardingCompleted
                    .collectAsStateWithLifecycle(initialValue = null)
                when (onboardingCompleted) {
                    false -> {
                        val analytics = koinInject<Analytics>()
                        LaunchedEffect(Unit) { analytics.setCurrentScreen("onboarding") }
                        OnboardingRoute()
                    }
                    true -> AppLockGate { HisabakNav() }
                    // null = still loading the flag; show a blank themed canvas (no flash).
                    null -> Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleFocusIntent(intent)
    }

    /** A notification tap carries either a category to focus (dashboard) or an uncategorized brand
     *  to open in the editor; publish whichever is present so the nav layer can react. */
    private fun handleFocusIntent(intent: Intent?) {
        intent?.getStringExtra(SystemNotifier.EXTRA_CATEGORY_ID)?.let(categoryFocusBus::request)
        intent?.getStringExtra(SystemNotifier.EXTRA_BRAND_ID)?.let(brandEditBus::request)
    }
}

private enum class RootTab(
    val key: NavKey,
    val labelRes: Int,
    val icon: ImageVector,
    val iconOutlined: ImageVector,
) {
    Dashboard(DashboardKey, R.string.nav_dashboard, Icons.Filled.SpaceDashboard, Icons.Outlined.SpaceDashboard),
    Transactions(TransactionsKey, R.string.nav_transactions, Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List),
    Sms(SmsKey, R.string.nav_sms, Icons.AutoMirrored.Filled.Message, Icons.AutoMirrored.Outlined.Message),
    Manage(ManageKey, R.string.nav_manage, Icons.Filled.Layers, Icons.Outlined.Layers),
    Settings(SettingsKey, R.string.nav_settings, Icons.Filled.Settings, Icons.Outlined.Settings),
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
    val categoryFocusBus = koinInject<CategoryFocusBus>()
    val brandEditBus = koinInject<BrandEditBus>()
    val notificationRepository = koinInject<NotificationRepository>()

    val unreadCount by notificationRepository.observeUnreadCount().collectAsStateWithLifecycle(initialValue = 0)
    val pendingFocus by categoryFocusBus.pending.collectAsStateWithLifecycle()
    val pendingBrandEdit by brandEditBus.pending.collectAsStateWithLifecycle()

    // A system-notification tap publishes a focus while we may be on another tab — switch to the
    // dashboard so it can consume and expand the category.
    LaunchedEffect(pendingFocus) {
        if (pendingFocus != null && navigationState.topLevelRoute != DashboardKey) {
            navigator.navigate(DashboardKey)
        }
    }

    // A "transaction recorded" tap for an uncategorized brand asks to open that brand's editor:
    // switch to Manage and push the brand edit screen, then clear the request.
    LaunchedEffect(pendingBrandEdit) {
        pendingBrandEdit?.let { brandId ->
            navigator.navigate(ManageKey)
            navigator.navigate(BrandEditKey(id = brandId))
            brandEditBus.consume()
        }
    }

    // Ask for notification permission once on first launch (Android 13+).
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) {}
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val tabs = RootTab.entries.map {
        BottomNavTab(
            key = it.name,
            label = stringResource(it.labelRes),
            icon = it.icon,
            iconOutlined = it.iconOutlined,
        )
    }

    val currentTab = RootTab.entries.first { it.key == navigationState.topLevelRoute }
    // The transaction add/edit screen is an overlay bottom sheet (tab chrome stays behind it).
    // Brand/Category edits and the notifications screen are full-screen pages with a back arrow.
    val leaf = navigationState.backStacks[navigationState.topLevelRoute]?.lastOrNull()
    val fullScreen = leaf is BrandEditKey || leaf is CategoryEditKey ||
        leaf == NotificationsKey || leaf == BackupKey

    val analytics = koinInject<Analytics>()
    val screenName = when (leaf) {
        is TransactionEditKey -> "transaction_edit"
        is BrandEditKey -> "brand_edit"
        is CategoryEditKey -> "category_edit"
        NotificationsKey -> "notifications"
        BackupKey -> "backup"
        else -> when (currentTab) {
            RootTab.Dashboard -> "dashboard"
            RootTab.Transactions -> "transactions"
            RootTab.Sms -> "sms_inbox"
            RootTab.Manage -> "manage"
            RootTab.Settings -> "settings"
        }
    }
    LaunchedEffect(screenName) { analytics.setCurrentScreen(screenName) }

    Scaffold(
        topBar = {
            when (leaf) {
                is CategoryEditKey -> DetailTopBar(
                    title = stringResource(if (leaf.id == null) R.string.category_new_title else R.string.category_edit_title),
                    onBack = { navigator.goBack() },
                )
                is BrandEditKey -> DetailTopBar(
                    title = stringResource(if (leaf.id == null) R.string.brand_new_title else R.string.brand_edit_title),
                    onBack = { navigator.goBack() },
                )
                NotificationsKey -> DetailTopBar(
                    title = stringResource(R.string.notifications_title),
                    onBack = { navigator.goBack() },
                )
                BackupKey -> DetailTopBar(
                    title = stringResource(R.string.backup_title),
                    onBack = { navigator.goBack() },
                )
                else -> HisabakTopBar(
                    title = when (currentTab) {
                        RootTab.Dashboard -> stringResource(R.string.app_brand_name)
                        RootTab.Transactions -> stringResource(R.string.nav_transactions)
                        RootTab.Sms -> stringResource(R.string.sms_inbox_title)
                        RootTab.Manage -> stringResource(R.string.nav_manage)
                        RootTab.Settings -> stringResource(R.string.nav_settings)
                    },
                    onNotificationsClick = { navigator.navigate(NotificationsKey) },
                    unreadCount = unreadCount,
                )
            }
        },
        bottomBar = {
            if (!fullScreen) {
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
                    Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.transaction_add))
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
            entry<SettingsKey> {
                SettingsRoute(
                    onOpenBackup = { navigator.navigate(BackupKey) },
                    modifier = Modifier.fillMaxSize(),
                )
            }
            entry<BackupKey> {
                BackupRoute(modifier = Modifier.fillMaxSize())
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
            entry<NotificationsKey> {
                NotificationsRoute(
                    onOpenCategory = { id ->
                        navigator.goBack()
                        categoryFocusBus.request(id)
                        navigator.navigate(DashboardKey)
                    },
                    modifier = Modifier.fillMaxSize(),
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
        // NavDisplay scene transition fights the sheet's own open/close animation. Sheets animate
        // themselves; tab switches use NavDisplay's defaults.
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
