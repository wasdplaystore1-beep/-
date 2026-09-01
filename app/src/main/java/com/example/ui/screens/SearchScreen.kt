package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.ProductFilterCriteria
import com.example.data.repository.ProductSortOption
import com.example.ui.components.ProductGridCard
import com.example.ui.components.StoreCard
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.util.SouqnaUtils
import com.example.ui.viewmodel.SearchTab
import com.example.ui.viewmodel.SouqnaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val searchFilter by viewModel.searchFilter.collectAsStateWithLifecycle()
    val searchTab by viewModel.searchTab.collectAsStateWithLifecycle()
    val productResults by viewModel.productSearchResults.collectAsStateWithLifecycle()
    val storeResults by viewModel.storeSearchResults.collectAsStateWithLifecycle()
    val categories by viewModel.platformCategories.collectAsStateWithLifecycle()
    val allStores by viewModel.allActiveStores.collectAsStateWithLifecycle()

    var showFilterSheet by remember { mutableStateOf(false) }

    val activeFilterCount = listOfNotNull(
        searchFilter.categoryKey,
        searchFilter.storeId,
        searchFilter.city,
        searchFilter.minPrice,
        searchFilter.maxPrice
    ).size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "البحث الموحد في سوقنا 🔍",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search Input Box & Filter Action
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            value = searchFilter.query,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = {
                                Text(
                                    text = "ابحث بالاسم، المتجر، أو التصنيف...",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "بحث",
                                    tint = EmeraldPrimary
                                )
                            },
                            trailingIcon = {
                                if (searchFilter.query.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "مسح")
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("unified_search_input")
                        )

                        // Filter Button with Badge
                        BadgedBox(
                            badge = {
                                if (activeFilterCount > 0) {
                                    Badge(
                                        containerColor = EmeraldPrimary,
                                        contentColor = Color.White
                                    ) {
                                        Text("$activeFilterCount")
                                    }
                                }
                            }
                        ) {
                            IconButton(
                                onClick = { showFilterSheet = true },
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(
                                        if (activeFilterCount > 0) EmeraldContainer else MaterialTheme.colorScheme.surfaceVariant,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .testTag("search_filter_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = "تصفية متقدمة",
                                    tint = if (activeFilterCount > 0) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Segmented Tabs: All, Products, Stores
                    PrimaryTabRow(
                        selectedTabIndex = searchTab.ordinal,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = EmeraldPrimary,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Tab(
                            selected = searchTab == SearchTab.ALL,
                            onClick = { viewModel.setSearchTab(SearchTab.ALL) },
                            text = { Text("الكل", fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = searchTab == SearchTab.PRODUCTS,
                            onClick = { viewModel.setSearchTab(SearchTab.PRODUCTS) },
                            text = { Text("المنتجات (${productResults.size})", fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = searchTab == SearchTab.STORES,
                            onClick = { viewModel.setSearchTab(SearchTab.STORES) },
                            text = { Text("المتاجر (${storeResults.size})", fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }

            // Quick Category Filter Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = searchFilter.categoryKey == null,
                        onClick = {
                            viewModel.updateSearchFilter(searchFilter.copy(categoryKey = null))
                        },
                        label = { Text("جميع التصنيفات") }
                    )
                }
                items(categories) { cat ->
                    FilterChip(
                        selected = searchFilter.categoryKey == cat.key,
                        onClick = {
                            val newKey = if (searchFilter.categoryKey == cat.key) null else cat.key
                            viewModel.updateSearchFilter(searchFilter.copy(categoryKey = newKey))
                        },
                        label = { Text("${cat.iconEmoji} ${cat.nameAr}") }
                    )
                }
            }

            // Active Filters Bar with Clear Option
            if (activeFilterCount > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "تم تطبيق $activeFilterCount معايير تصفية",
                        fontSize = 12.sp,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "إعادة ضبط",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.clearSearchFilter() }
                            .padding(4.dp)
                    )
                }
            }

            // Search Content / Results
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (searchTab) {
                    SearchTab.ALL -> {
                        // 1. Stores Result Horizontal Strip
                        if (storeResults.isNotEmpty()) {
                            item {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "المتاجر المطابقة (${storeResults.size})",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "عرض الكل",
                                            fontSize = 12.sp,
                                            color = EmeraldPrimary,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.clickable { viewModel.setSearchTab(SearchTab.STORES) }
                                        )
                                    }

                                    LazyRow(
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(storeResults) { store ->
                                            Box(modifier = Modifier.width(280.dp)) {
                                                StoreCard(
                                                    store = store,
                                                    onStoreClick = { viewModel.selectStore(store.id) },
                                                    onWhatsAppClick = {
                                                        SouqnaUtils.openWhatsApp(
                                                            context = context,
                                                            phone = store.whatsapp,
                                                            message = "مرحباً ${store.name}، شاهدت متجركم عبر محرك بحث سوقنا."
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 2. Products Result Grid
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "المنتجات المطابقة (${productResults.size})",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = searchFilter.sortOption.titleAr,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (productResults.isEmpty() && storeResults.isEmpty()) {
                            item {
                                SearchEmptyState(query = searchFilter.query)
                            }
                        } else {
                            val chunked = productResults.chunked(2)
                            items(chunked) { rowItems ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    for (item in rowItems) {
                                        Box(modifier = Modifier.weight(1f)) {
                                            ProductGridCard(
                                                productWithStore = item,
                                                onProductClick = { viewModel.selectProduct(item.product.id) },
                                                onStoreClick = { viewModel.selectStore(item.store.id) }
                                            )
                                        }
                                    }
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    SearchTab.PRODUCTS -> {
                        if (productResults.isEmpty()) {
                            item {
                                SearchEmptyState(query = searchFilter.query)
                            }
                        } else {
                            val chunked = productResults.chunked(2)
                            items(chunked) { rowItems ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    for (item in rowItems) {
                                        Box(modifier = Modifier.weight(1f)) {
                                            ProductGridCard(
                                                productWithStore = item,
                                                onProductClick = { viewModel.selectProduct(item.product.id) },
                                                onStoreClick = { viewModel.selectStore(item.store.id) }
                                            )
                                        }
                                    }
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    SearchTab.STORES -> {
                        if (storeResults.isEmpty()) {
                            item {
                                SearchEmptyState(query = searchFilter.query)
                            }
                        } else {
                            items(storeResults) { store ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    StoreCard(
                                        store = store,
                                        onStoreClick = { viewModel.selectStore(store.id) },
                                        onWhatsAppClick = {
                                            SouqnaUtils.openWhatsApp(
                                                context = context,
                                                phone = store.whatsapp,
                                                message = "مرحباً ${store.name}، شاهدت متجركم عبر محرك بحث سوقنا."
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Advanced Filter BottomSheet
    if (showFilterSheet) {
        SearchFilterBottomSheet(
            currentCriteria = searchFilter,
            categories = categories,
            stores = allStores,
            onApply = { updatedCriteria ->
                viewModel.updateSearchFilter(updatedCriteria)
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@Composable
fun SearchEmptyState(query: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🔍", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (query.isNotBlank()) "لم يتم العثور على نتائج لـ \"$query\"" else "لا توجد منتجات أو متاجر مطابقة لمعايير البحث",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "جرب البحث بكلمات أخرى أو تقليل الفلاتر المطبقة",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterBottomSheet(
    currentCriteria: ProductFilterCriteria,
    categories: List<com.example.data.model.PlatformCategory>,
    stores: List<com.example.data.model.Store>,
    onApply: (ProductFilterCriteria) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var selectedCategoryKey by remember { mutableStateOf(currentCriteria.categoryKey) }
    var selectedStoreId by remember { mutableStateOf(currentCriteria.storeId) }
    var selectedCity by remember { mutableStateOf(currentCriteria.city) }
    var minPriceText by remember { mutableStateOf(currentCriteria.minPrice?.toInt()?.toString() ?: "") }
    var maxPriceText by remember { mutableStateOf(currentCriteria.maxPrice?.toInt()?.toString() ?: "") }
    var selectedSort by remember { mutableStateOf(currentCriteria.sortOption) }

    val cities = listOf("الرياض", "جدة", "الخبر", "الدمام", "مكة المكرمة", "المدينة المنورة")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "تصفية وترتيب النتائج ⚙️",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "إعادة ضبط الكل",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        selectedCategoryKey = null
                        selectedStoreId = null
                        selectedCity = null
                        minPriceText = ""
                        maxPriceText = ""
                        selectedSort = ProductSortOption.LATEST
                    }
                )
            }

            // 1. Sort Options
            Text(text = "ترتيب حسب:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ProductSortOption.values()) { opt ->
                    FilterChip(
                        selected = selectedSort == opt,
                        onClick = { selectedSort = opt },
                        label = { Text(opt.titleAr, fontSize = 12.sp) }
                    )
                }
            }

            // 2. City Filter
            Text(text = "المدينة:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedCity == null,
                        onClick = { selectedCity = null },
                        label = { Text("الكل") }
                    )
                }
                items(cities) { city ->
                    FilterChip(
                        selected = selectedCity == city,
                        onClick = { selectedCity = if (selectedCity == city) null else city },
                        label = { Text(city) }
                    )
                }
            }

            // 3. Price Range
            Text(text = "نطاق السعر (ر.س):", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = minPriceText,
                    onValueChange = { minPriceText = it },
                    label = { Text("من") },
                    placeholder = { Text("0") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = maxPriceText,
                    onValueChange = { maxPriceText = it },
                    label = { Text("إلى") },
                    placeholder = { Text("1000") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Apply Button
            Button(
                onClick = {
                    val minP = minPriceText.toDoubleOrNull()
                    val maxP = maxPriceText.toDoubleOrNull()
                    val newCriteria = currentCriteria.copy(
                        categoryKey = selectedCategoryKey,
                        storeId = selectedStoreId,
                        city = selectedCity,
                        minPrice = minP,
                        maxPrice = maxP,
                        sortOption = selectedSort
                    )
                    onApply(newCriteria)
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("تطبيق التصفية", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
