package com.biernatmdev.simple_service.core.ui.components.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceButton
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceTextField
import com.biernatmdev.simple_service.core.ui.components.dropdowns.CategoryDropdown
import com.biernatmdev.simple_service.core.ui.components.dropdowns.CurrencyDropdown
import com.biernatmdev.simple_service.core.ui.components.dropdowns.ItemConditionDropdown
import com.biernatmdev.simple_service.core.ui.components.dropdowns.PriceUnitDropdown
import com.biernatmdev.simple_service.core.ui.models.IconType
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_SMALL
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.REGULAR
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowDown
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowUp
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.CheckFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.CloseFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.LocalisationOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Offering
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Product
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Requesting
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.SearchOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Service
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker

@Composable
fun DropdownExpandAnimation(
    animationDuration: Int,
    isExpanded: Boolean,
    insideDropdown: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(
            animationSpec = tween(animationDuration),
            expandFrom = Alignment.Top
        ) + fadeIn(animationSpec = tween(animationDuration)),

        exit = shrinkVertically(
            animationSpec = tween(animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut(animationSpec = tween(animationDuration))
    ) {
        Column {
            insideDropdown()
        }
    }
}

@Composable
fun MainFilterDropdown(
    filterState: FilterState,
    onFilterEvent: (FilterEvent) -> Unit,

    priceFromState: TextFieldState,
    priceToState: TextFieldState,
    cityState: TextFieldState,

    animationDuration: Int = 600,

    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FilterDropdown(
            label = UiText.StringResource(R.string.filter_main_filter_dropdown_label).asString(),
            isExpanded = filterState.isFilterDropdownExpanded,
            contentColor = onColorBackground,
            onDropdownClick = { onFilterEvent(FilterEvent.OnMainFilterDropdownClick) },
        )
        Spacer(Modifier.height(16.dp))
        DropdownExpandAnimation(
            animationDuration = animationDuration,
            isExpanded = filterState.isFilterDropdownExpanded,
        ) {
            TransactionAndOfferTypeFilter(
                animationDuration = animationDuration,

                selectedTransactionType = filterState.selectedTransactionType,
                onTransactionTypeFilterDropdownClick = { onFilterEvent(FilterEvent.OnTransactionTypeFilterDropdownClick) },
                onTransactionTypeFilterClick = { onFilterEvent(FilterEvent.OnTransactionTypeClick(it)) },
                isTransactionTypeFilterDropdownExpanded = filterState.isTransactionTypeFilterDropdownExpanded,

                selectedOfferType = filterState.selectedOfferType,
                onOfferTypeFilterDropdownClick = { onFilterEvent(FilterEvent.OnOfferTypeFilterDropdownClick) },
                onOfferTypeFilterClick = { onFilterEvent(FilterEvent.OnOfferTypeClick(it)) },
                isOfferTypeFilterDropdownExpanded = filterState.isOfferTypeFilterDropdownExpanded,
            )
            Spacer(Modifier.height(16.dp))
            CategoryFilter(
                animationDuration = animationDuration,
                isCategoryFilterDropdownExpanded = filterState.isCategoryDropdownExpanded,
                onCategoryFilterDropdownClick = { onFilterEvent(FilterEvent.OnCategoryFilterDropdownClick) },

                selectedSuperCategory = filterState.selectedSuperCategory,
                isSuperCategoryFilterDropdownExpanded = filterState.isSuperCategoryDropdownExpanded,
                onSuperCategoryFilterDropdownClick = { onFilterEvent(FilterEvent.OnSuperCategoryFilterDropdownClick) },
                onSuperCategoryFilterClick = { onFilterEvent(FilterEvent.OnSuperCategoryClick(it)) },
                onSuperCategoryFilterDropdownDismiss = { onFilterEvent(FilterEvent.OnSuperCategoryFilterDropdownDismiss) },
                onDisabledSuperCategoryClick = { onFilterEvent(FilterEvent.OnDisabledSuperCategoryClick) },

                selectedSubcategory = filterState.selectedSubcategory,
                isSubcategoryFilterDropdownExpanded = filterState.isSubcategoryDropdownExpanded,
                onSubcategoryFilterDropdownClick = { onFilterEvent(FilterEvent.OnSubcategoryFilterDropdownClick) },
                onSubcategoryFilterClick = { onFilterEvent(FilterEvent.OnSubcategoryClick(it)) },
                onSubcategoryFilterDropdownDismiss = { onFilterEvent(FilterEvent.OnSubcategoryFilterDropdownDismiss) },
                onDisabledSubcategoryClick = { onFilterEvent(FilterEvent.OnDisabledSubcategoryClick) },

                selectedOfferType = filterState.selectedOfferType,
            )
            Spacer(Modifier.height(16.dp))
            PriceFilter(
                animationDuration = animationDuration,

                onPriceFilterDropdownClick = { onFilterEvent(FilterEvent.OnPriceFilterDropdownClick) },
                isPriceFilterExpanded = filterState.isPriceDropdownExpanded,

                selectedPriceFrom = priceFromState,
                selectedPriceTo = priceToState,

                selectedPriceCurrency = filterState.selectedCurrency,
                isCurrencyDropdownExpanded = filterState.isCurrencyDropdownExpanded,
                onCurrencyDropdownClick = { onFilterEvent(FilterEvent.OnPriceCurrencyFilterDropdownClick) },
                onCurrencySelected = { onFilterEvent(FilterEvent.OnPriceCurrencyClick(it)) },
                onCurrencyDropdownDismiss = { onFilterEvent(FilterEvent.OnPriceCurrencyFilterDropdownDismiss) },

                selectedPriceUnit = filterState.selectedPriceUnit,
                isPriceUnitDropdownExpanded = filterState.isPriceUnitDropdownExpanded,
                onPriceUnitDropdownClick = { onFilterEvent(FilterEvent.OnPriceUnitFilterDropdownClick) },
                onPriceUnitSelected = { onFilterEvent(FilterEvent.OnPriceUnitClick(it)) },
                onPriceUnitDropdownDismiss = { onFilterEvent(FilterEvent.OnPriceUnitFilterDropdownDismiss) }
            )
            Spacer(Modifier.height(16.dp))
            CityAndItemCondition(
                selectedOfferType = filterState.selectedOfferType,

                cityState = cityState,
                onDisabledCityClick = { onFilterEvent(FilterEvent.OnDisabledCityClick) },

                selectedItemCondition = filterState.selectedItemCondition,
                isItemConditionFilterDropdownExpanded = filterState.isItemConditionFilterDropdownExpanded,
                onItemConditionFilterDropdownClick = { onFilterEvent(FilterEvent.OnItemConditionFilterDropdownClick) },
                onItemConditionFilterClick = { onFilterEvent(FilterEvent.OnItemConditionFilterClick(it)) },
                onItemConditionFilterDropdownDismiss = { onFilterEvent(FilterEvent.OnItemConditionFilterDropdownDismiss) },
                onDisabledItemConditionClick = { onFilterEvent(FilterEvent.OnDisabledItemConditionClick) },
            )
            Spacer(Modifier.height(16.dp))
            FilterButtonsSection(
                onApplyFiltersButtonClick = { onFilterEvent(FilterEvent.OnApplyFiltersButtonClick) },
                onClearFiltersButtonClick = { onFilterEvent(FilterEvent.OnClearFiltersButtonClick) }
            )
            Spacer(Modifier.height(120.dp))
        }
    }
}

@Composable
fun SearchBarFilter(
    searchbarState: TextFieldState,
    onSearchbarButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            SimpleServiceTextField(
                state = searchbarState,
                onFocus = {},
                errorText = null,
                verticalPadding = 8.dp,
                horizontalPadding = 16.dp,
                roundedCornerShapeValue = 14.dp,
                placeholder = UiText.StringResource(R.string.filter_search_filter_textfield_placeholder).asString(),
                icon = IconType.Vector(SearchOutlined),
            )
        }
        Spacer(Modifier.width(16.dp))
        SimpleServiceButton(
            text = "",
            buttonPadding = 8.dp,
            roundedCornerShapeValue = 14.dp,
            icon = IconType.Vector(SearchOutlined),
            iconSize = 24.dp,
            isAnimated = false,
            onClick = { onSearchbarButtonClick() }
        )
    }
}

@Composable
fun TransactionAndOfferTypeFilter(
    animationDuration: Int,

    selectedTransactionType: TransactionType?,
    onTransactionTypeFilterDropdownClick: () -> Unit,
    onTransactionTypeFilterClick: (TransactionType?) -> Unit,
    isTransactionTypeFilterDropdownExpanded: Boolean,

    selectedOfferType: OfferType?,
    onOfferTypeFilterDropdownClick: () -> Unit,
    onOfferTypeFilterClick: (OfferType?) -> Unit,
    isOfferTypeFilterDropdownExpanded: Boolean,
) {

    @Composable
    fun TransactionAndOfferTypeChip(
        onClick: () -> Unit,
        label: String,
        isSelected: Boolean,
        painter: Painter,
        modifier: Modifier = Modifier,
    ) {
        Surface(
            color = ColorSecondary,
            shape = RoundedCornerShape(14.dp),
            modifier = modifier
                .clickable { onClick() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painter,
                    tint = Color.Unspecified,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = label,
                    fontFamily = momoFont(),
                    color = if (isSelected) ColorPrimary else onColorBackgroundDarker,
                    fontSize = EXTRA_SMALL,
                )
            }

        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FilterDropdown(
                label = selectedTransactionType?.displayName?.asString() ?:
                UiText.StringResource(R.string.filter_transaction_offer_filter_dropdown_label_transaction).asString(),
                isExpanded = isTransactionTypeFilterDropdownExpanded,
                contentColor = onColorBackgroundDarker,
                onDropdownClick = { onTransactionTypeFilterDropdownClick() }
            )
            DropdownExpandAnimation(
                animationDuration = animationDuration,
                isExpanded = isTransactionTypeFilterDropdownExpanded
            ) {
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TransactionAndOfferTypeChip(
                        painter = painterResource(Offering),
                        isSelected = selectedTransactionType == TransactionType.OFFER,
                        onClick = {
                            val next =
                                if (selectedTransactionType == TransactionType.OFFER) null else TransactionType.OFFER
                            onTransactionTypeFilterClick(next)
                        },
                        label = TransactionType.OFFER.displayName.asString(),
                        modifier = Modifier.weight(1f)
                    )
                    TransactionAndOfferTypeChip(
                        painter = painterResource(Requesting),
                        isSelected = selectedTransactionType == TransactionType.REQUEST,
                        onClick = {
                            val next =
                                if (selectedTransactionType == TransactionType.REQUEST) null else TransactionType.REQUEST
                            onTransactionTypeFilterClick(next)
                        },
                        label = TransactionType.REQUEST.displayName.asString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FilterDropdown(
                label = selectedOfferType?.displayName?.asString() ?:
                UiText.StringResource(R.string.filter_transaction_offer_filter_dropdown_label_offer).asString(),
                isExpanded = isOfferTypeFilterDropdownExpanded,
                contentColor = onColorBackgroundDarker,
                onDropdownClick = { onOfferTypeFilterDropdownClick() }
            )
            DropdownExpandAnimation(
                animationDuration = animationDuration,
                isExpanded = isOfferTypeFilterDropdownExpanded
            ) {
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TransactionAndOfferTypeChip(
                        painter = painterResource(Product),
                        isSelected = selectedOfferType == OfferType.PRODUCT,
                        onClick = {
                            val next =
                                if (selectedOfferType == OfferType.PRODUCT) null else OfferType.PRODUCT
                            onOfferTypeFilterClick(next)
                        },
                        label = OfferType.PRODUCT.displayName.asString(),
                        modifier = Modifier.weight(1f),
                    )
                    TransactionAndOfferTypeChip(
                        painter = painterResource(Service),
                        isSelected = selectedOfferType == OfferType.SERVICE,
                        onClick = {
                            val next =
                                if (selectedOfferType == OfferType.SERVICE) null else OfferType.SERVICE
                            onOfferTypeFilterClick(next)
                        },
                        label = OfferType.SERVICE.displayName.asString(),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
fun PriceFilter(
    animationDuration: Int,

    onPriceFilterDropdownClick: () -> Unit,
    isPriceFilterExpanded: Boolean,
    selectedPriceFrom: TextFieldState,
    selectedPriceTo: TextFieldState,

    selectedPriceCurrency: String,
    isCurrencyDropdownExpanded: Boolean,
    onCurrencyDropdownClick: () -> Unit,
    onCurrencySelected: (String) -> Unit,
    onCurrencyDropdownDismiss: () -> Unit,

    selectedPriceUnit: OfferPriceUnit,
    isPriceUnitDropdownExpanded: Boolean,
    onPriceUnitDropdownClick: () -> Unit,
    onPriceUnitSelected: (OfferPriceUnit) -> Unit,
    onPriceUnitDropdownDismiss: () -> Unit,
) {
    FilterDropdown(
        label = UiText.StringResource(R.string.filter_price_dropdown_label).asString(),
        isExpanded = isPriceFilterExpanded,
        contentColor = onColorBackgroundDarker,
        onDropdownClick = { onPriceFilterDropdownClick() }
    )
    DropdownExpandAnimation(
        animationDuration = animationDuration,
        isExpanded = isPriceFilterExpanded
    ) {
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleServiceTextField(
                state = selectedPriceFrom,
                onFocus = {},
                isPhoneNumber = true,
                errorText = null,
                placeholder = UiText.StringResource(R.string.filter_price_textfield_placeholder_from).asString(),
                modifier = Modifier.weight(2.25f),
                verticalPadding = 8.dp,
                horizontalPadding = 8.dp,
                roundedCornerShapeValue = 14.dp,
            )
            SimpleServiceTextField(
                state = selectedPriceTo,
                onFocus = {},
                isPhoneNumber = true,
                errorText = null,
                placeholder = UiText.StringResource(R.string.filter_price_textfield_placeholder_to).asString(),
                modifier = Modifier.weight(2.25f),
                verticalPadding = 8.dp,
                horizontalPadding = 8.dp,
                roundedCornerShapeValue = 14.dp,
            )
            CurrencyDropdown(
                selectedCurrency = selectedPriceCurrency,
                isExpanded = isCurrencyDropdownExpanded,
                onDropdownClick = { onCurrencyDropdownClick() },
                onCurrencySelected = { onCurrencySelected(it) },
                onDismiss = { onCurrencyDropdownDismiss() },
                withAny = true,
                labelSize = REGULAR,
                modifier = Modifier.weight(2.5f)
            )
            PriceUnitDropdown(
                selectedUnit = selectedPriceUnit,
                isExpanded = isPriceUnitDropdownExpanded,
                onDropdownClick = { onPriceUnitDropdownClick() },
                onUnitSelected = { onPriceUnitSelected(it) },
                onDismiss = { onPriceUnitDropdownDismiss() },
                withAny = true,
                labelSize = REGULAR,
                horizontalPadding = 8.dp,
                modifier = Modifier.weight(3f)
            )
        }
    }
}

@Composable
fun CategoryFilter(
    animationDuration: Int,
    isCategoryFilterDropdownExpanded: Boolean,
    onCategoryFilterDropdownClick: () -> Unit,

    selectedSuperCategory: OfferSuperCategory?,
    isSuperCategoryFilterDropdownExpanded: Boolean,
    onSuperCategoryFilterDropdownClick: () -> Unit,
    onSuperCategoryFilterClick: (OfferSuperCategory) -> Unit,
    onSuperCategoryFilterDropdownDismiss: () -> Unit,
    onDisabledSuperCategoryClick: () -> Unit,

    selectedSubcategory: OfferCategory?,
    isSubcategoryFilterDropdownExpanded: Boolean,
    onSubcategoryFilterDropdownClick: () -> Unit,
    onSubcategoryFilterClick: (OfferCategory) -> Unit,
    onSubcategoryFilterDropdownDismiss: () -> Unit,
    onDisabledSubcategoryClick: () -> Unit,

    selectedOfferType: OfferType?,
) {
    val superCategories = remember(selectedOfferType) {
        if (selectedOfferType != null) {
            OfferSuperCategory.getParentsByType(selectedOfferType)
        } else {
            emptyList()
        }
    }

    val subCategories = remember(selectedSuperCategory) {
        if (selectedSuperCategory != null) {
            OfferCategory.getSubcategories(selectedSuperCategory)
        } else {
            emptyList()
        }
    }

    FilterDropdown(
        label = UiText.StringResource(R.string.filter_category_dropdown_label).asString(),
        isExpanded = isCategoryFilterDropdownExpanded,
        contentColor = onColorBackgroundDarker,
        onDropdownClick = { onCategoryFilterDropdownClick() }
    )
    DropdownExpandAnimation(
        animationDuration = animationDuration,
        isExpanded = isCategoryFilterDropdownExpanded
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryDropdown(
                items = superCategories,
                selectedItem = selectedSuperCategory,
                isExpanded = isSuperCategoryFilterDropdownExpanded,
                onDropdownClick = { onSuperCategoryFilterDropdownClick() },
                onItemSelected = { onSuperCategoryFilterClick(it as OfferSuperCategory) },
                onDismiss = { onSuperCategoryFilterDropdownDismiss() },
                isEnabled = selectedOfferType != null,
                onDisabledClick = { onDisabledSuperCategoryClick() },
                placeholder = UiText.StringResource(R.string.filter_category_dropdown_placeholder_super).asString(),
                modifier = Modifier.weight(1f)
            )
            CategoryDropdown(
                items = subCategories,
                selectedItem = selectedSubcategory,
                isExpanded = isSubcategoryFilterDropdownExpanded,
                onDropdownClick = { onSubcategoryFilterDropdownClick() },
                onItemSelected = { onSubcategoryFilterClick(it as OfferCategory) },
                onDismiss = { onSubcategoryFilterDropdownDismiss() },
                isEnabled = selectedSuperCategory != null,
                onDisabledClick = { onDisabledSubcategoryClick() },
                placeholder = UiText.StringResource(R.string.filter_category_dropdown_placeholder_sub).asString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CityAndItemCondition(
    selectedOfferType: OfferType?,
    cityState: TextFieldState,
    selectedItemCondition: ItemCondition?,
    isItemConditionFilterDropdownExpanded: Boolean,
    onItemConditionFilterDropdownClick: () -> Unit,
    onItemConditionFilterClick: (ItemCondition) -> Unit,
    onItemConditionFilterDropdownDismiss: () -> Unit,
    onDisabledCityClick: () -> Unit,
    onDisabledItemConditionClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimpleServiceTextField(
            state = cityState,
            onFocus = {},
            errorText = null,
            verticalPadding = 8.dp,
            horizontalPadding = 16.dp,
            roundedCornerShapeValue = 14.dp,
            icon = IconType.Vector(LocalisationOutlined),
            placeholder = UiText.StringResource(R.string.filter_city_condition_filter_textfield_placeholder).asString(),
            isEnabled = selectedOfferType == OfferType.SERVICE,
            onDisabledClick = { onDisabledCityClick() },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )
        ItemConditionDropdown(
            selectedCondition = selectedItemCondition,
            isExpanded = isItemConditionFilterDropdownExpanded,
            onDropdownClick = { onItemConditionFilterDropdownClick() },
            onConditionSelected = { onItemConditionFilterClick(it) },
            onDismiss = { onItemConditionFilterDropdownDismiss() },
            isEnabled = selectedOfferType == OfferType.PRODUCT,
            onDisabledClick = { onDisabledItemConditionClick() },
            withAny = true,
            labelSize = MEDIUM,
            horizontalPadding = 8.dp,
            verticalPadding = 8.dp,
            minHeight = false,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}

@Composable
fun FilterButtonsSection(
    onApplyFiltersButtonClick: () -> Unit,
    onClearFiltersButtonClick: () -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        SimpleServiceButton(
            text = UiText.StringResource(R.string.filter_btn_clear).asString(),
            isAnimated = false,
            textFontSize = MEDIUM,
            icon = IconType.Vector(CloseFilled),
            iconTint = onColorBackgroundDarker,
            backgroundColor = ColorSecondary,
            textColor = onColorBackgroundDarker,
            onClick = { onClearFiltersButtonClick() },
            buttonPadding = 8.dp,
            roundedCornerShapeValue = 14.dp,
            modifier = Modifier.weight(1f)
        )
        SimpleServiceButton(
            text = UiText.StringResource(R.string.filter_btn_apply).asString(),
            isAnimated = false,
            textFontSize = MEDIUM,
            icon = IconType.Vector(CheckFilled),
            onClick = { onApplyFiltersButtonClick() },
            buttonPadding = 8.dp,
            roundedCornerShapeValue = 14.dp,
            modifier = Modifier.weight(1f)
        )
    }

}

@Composable
fun FilterDropdown(
    label: String,
    isExpanded: Boolean,
    contentColor: Color,
    onDropdownClick: () -> Unit,
) {
    Surface(
        color = ColorSecondary,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .clickable { onDropdownClick() }
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = MEDIUM,
                fontFamily = momoFont(),
                fontWeight = FontWeight.Bold,
                color = contentColor,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp)
            )
            Icon(
                imageVector = if (isExpanded) ArrowUp else ArrowDown,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
