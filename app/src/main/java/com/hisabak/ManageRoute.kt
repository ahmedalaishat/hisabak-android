package com.hisabak

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.presentation.edit.BrandEditRoute
import com.hisabak.feature.brand.presentation.list.BrandListRoute
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.presentation.edit.CategoryEditRoute
import com.hisabak.feature.category.presentation.list.CategoryListRoute
import com.hisabak.ui.components.IconTile
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.theme.HisabakTheme
import com.hisabak.ui.theme.Spacing
import org.koin.compose.viewmodel.koinViewModel

private enum class ManageTab { Brands, Categories }

private sealed interface BrandsNav {
    data object List : BrandsNav
    data class Edit(val id: BrandId?) : BrandsNav
}

private sealed interface CategoriesNav {
    data object List : CategoriesNav
    data class Edit(val id: CategoryId?) : CategoriesNav
}

@Composable
fun ManageRoute(
    modifier: Modifier = Modifier,
    onDetailEnter: (title: String, onBack: () -> Unit) -> Unit = { _, _ -> },
    onDetailExit: () -> Unit = {},
    viewModel: ManageViewModel = koinViewModel(),
) {
    var tab by rememberSaveable { mutableStateOf(ManageTab.Brands) }
    var brandNav: BrandsNav by remember { mutableStateOf(BrandsNav.List) }
    var catNav: CategoriesNav by remember { mutableStateOf(CategoriesNav.List) }
    val counts by viewModel.counts.collectAsStateWithLifecycle()

    // Brand edit (full-screen detail)
    if (tab == ManageTab.Brands && brandNav is BrandsNav.Edit) {
        val id = (brandNav as BrandsNav.Edit).id
        val title = if (id == null) "New brand" else "Edit brand"
        DisposableEffect(Unit) {
            onDetailEnter(title) { brandNav = BrandsNav.List }
            onDispose { onDetailExit() }
        }
        BackHandler { brandNav = BrandsNav.List }
        Box(modifier) {
            BrandEditRoute(
                brandId = id,
                onDone = { brandNav = BrandsNav.List },
                onCancel = { brandNav = BrandsNav.List },
            )
        }
        return
    }

    // Category edit (full-screen detail)
    if (tab == ManageTab.Categories && catNav is CategoriesNav.Edit) {
        val id = (catNav as CategoriesNav.Edit).id
        val title = if (id == null) "New category" else "Edit category"
        DisposableEffect(Unit) {
            onDetailEnter(title) { catNav = CategoriesNav.List }
            onDispose { onDetailExit() }
        }
        BackHandler { catNav = CategoriesNav.List }
        Box(modifier) {
            CategoryEditRoute(
                categoryId = id,
                onDone = { catNav = CategoriesNav.List },
                onCancel = { catNav = CategoriesNav.List },
            )
        }
        return
    }

    // List view: count cards act as the Brands/Categories switcher; FAB adds the active type.
    Box(modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.pageMargin, vertical = Spacing.cardGap),
                horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap),
            ) {
                ManageSwitchCard(
                    label = "Brands",
                    count = counts.brands,
                    icon = Icons.Filled.Storefront,
                    selected = tab == ManageTab.Brands,
                    onClick = { tab = ManageTab.Brands },
                    modifier = Modifier.weight(1f),
                )
                ManageSwitchCard(
                    label = "Categories",
                    count = counts.categories,
                    icon = Icons.Filled.Category,
                    selected = tab == ManageTab.Categories,
                    onClick = { tab = ManageTab.Categories },
                    modifier = Modifier.weight(1f),
                )
            }

            when (tab) {
                ManageTab.Brands -> BrandListRoute(
                    onAdd = { brandNav = BrandsNav.Edit(null) },
                    onEdit = { brandNav = BrandsNav.Edit(it) },
                    showHeader = false,
                )
                ManageTab.Categories -> CategoryListRoute(
                    onAdd = { catNav = CategoriesNav.Edit(null) },
                    onEdit = { catNav = CategoriesNav.Edit(it) },
                    showHeader = false,
                )
            }
        }

        FloatingActionButton(
            onClick = {
                when (tab) {
                    ManageTab.Brands -> brandNav = BrandsNav.Edit(null)
                    ManageTab.Categories -> catNav = CategoriesNav.Edit(null)
                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Spacing.pageMargin),
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = if (tab == ManageTab.Brands) "New brand" else "New category",
            )
        }
    }
}

@Composable
private fun ManageSwitchCard(
    label: String,
    count: Int,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val border = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val bg = if (selected) HisabakTheme.colors.incomeSoft else MaterialTheme.colorScheme.surfaceContainerLowest
    val iconBg = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val iconFg = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    SurfaceCard(
        modifier = modifier,
        contentPadding = Spacing.s4,
        backgroundColor = bg,
        borderColor = border,
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            IconTile(
                icon = icon,
                size = Spacing.s8,
                iconSize = 16.dp,
                background = iconBg,
                foreground = iconFg,
            )
            Column {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
