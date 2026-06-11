package com.hisabak

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.presentation.edit.BrandEditRoute
import com.hisabak.feature.brand.presentation.list.BrandListRoute
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.presentation.edit.CategoryEditRoute
import com.hisabak.feature.category.presentation.list.CategoryListRoute
import com.hisabak.ui.components.CreateActionButton
import com.hisabak.ui.components.SegmentedControl
import com.hisabak.ui.components.SegmentOption
import com.hisabak.ui.theme.Spacing

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
) {
    var tab by rememberSaveable { mutableStateOf(ManageTab.Brands) }
    var brandNav: BrandsNav by remember { mutableStateOf(BrandsNav.List) }
    var catNav: CategoriesNav by remember { mutableStateOf(CategoriesNav.List) }

    // Brand edit
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

    // Category edit
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

    // List view with tab switcher
    Column(modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.pageMargin, vertical = Spacing.cardGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SegmentedControl(
                options = listOf(
                    SegmentOption(ManageTab.Brands, "Brands"),
                    SegmentOption(ManageTab.Categories, "Categories"),
                ),
                selected = tab,
                onSelect = { tab = it },
                modifier = Modifier.weight(1f),
            )
            CreateActionButton(
                text = if (tab == ManageTab.Brands) "New brand" else "New category",
                onClick = {
                    when (tab) {
                        ManageTab.Brands -> brandNav = BrandsNav.Edit(null)
                        ManageTab.Categories -> catNav = CategoriesNav.Edit(null)
                    }
                },
                modifier = Modifier.padding(start = Spacing.cardGap),
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
}
