package com.hisabak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.presentation.edit.BrandEditRoute
import com.hisabak.feature.brand.presentation.list.BrandListRoute
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.presentation.edit.CategoryEditRoute
import com.hisabak.feature.category.presentation.list.CategoryListRoute
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.feature.transaction.presentation.edit.TransactionEditRoute
import com.hisabak.feature.transaction.presentation.list.TransactionListRoute
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

private enum class RootTab(val label: String, val icon: ImageVector) {
    Transactions("Transactions", Icons.AutoMirrored.Filled.List),
    Brands("Brands", Icons.Filled.Storefront),
    Categories("Categories", Icons.Filled.Category),
}

private sealed interface TransactionsNav {
    data object List : TransactionsNav
    data class Edit(val id: TransactionId?) : TransactionsNav
}

private sealed interface BrandsNav {
    data object List : BrandsNav
    data class Edit(val id: BrandId?) : BrandsNav
}

private sealed interface CategoriesNav {
    data object List : CategoriesNav
    data class Edit(val id: CategoryId?) : CategoriesNav
}

@Composable
private fun HisabakNav() {
    var currentTab by rememberSaveable { mutableStateOf(RootTab.Transactions) }
    var txNav: TransactionsNav by remember { mutableStateOf(TransactionsNav.List) }
    var brandNav: BrandsNav by remember { mutableStateOf(BrandsNav.List) }
    var catNav: CategoriesNav by remember { mutableStateOf(CategoriesNav.List) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                RootTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                    )
                }
            }
        },
    ) { padding ->
        val tabModifier = Modifier.fillMaxSize().padding(padding)
        when (currentTab) {
            RootTab.Transactions -> TransactionsGraph(
                nav = txNav,
                onNavChange = { txNav = it },
                modifier = tabModifier,
            )
            RootTab.Brands -> BrandsGraph(
                nav = brandNav,
                onNavChange = { brandNav = it },
                modifier = tabModifier,
            )
            RootTab.Categories -> CategoriesGraph(
                nav = catNav,
                onNavChange = { catNav = it },
                modifier = tabModifier,
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

@Composable
private fun BrandsGraph(
    nav: BrandsNav,
    onNavChange: (BrandsNav) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        when (nav) {
            BrandsNav.List -> BrandListRoute(
                onAdd = { onNavChange(BrandsNav.Edit(id = null)) },
                onEdit = { id -> onNavChange(BrandsNav.Edit(id = id)) },
            )
            is BrandsNav.Edit -> BrandEditRoute(
                brandId = nav.id,
                onDone = { onNavChange(BrandsNav.List) },
                onCancel = { onNavChange(BrandsNav.List) },
            )
        }
    }
}

@Composable
private fun CategoriesGraph(
    nav: CategoriesNav,
    onNavChange: (CategoriesNav) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        when (nav) {
            CategoriesNav.List -> CategoryListRoute(
                onAdd = { onNavChange(CategoriesNav.Edit(id = null)) },
                onEdit = { id -> onNavChange(CategoriesNav.Edit(id = id)) },
            )
            is CategoriesNav.Edit -> CategoryEditRoute(
                categoryId = nav.id,
                onDone = { onNavChange(CategoriesNav.List) },
                onCancel = { onNavChange(CategoriesNav.List) },
            )
        }
    }
}
