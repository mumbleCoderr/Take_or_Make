package com.biernatmdev.simple_service.core.offer.domain.enums

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText

enum class OfferSuperCategory(
    override val displayName: UiText,
    val offerType: OfferType,
) : CategoryDisplayable {

    // PRODUCTS
    ELECTRONICS(UiText.StringResource(R.string.offer_super_category_product_name_electronics), OfferType.PRODUCT),
    AUTOMOTIVE(UiText.StringResource(R.string.offer_super_category_product_name_automotive), OfferType.PRODUCT),
    FASHION(UiText.StringResource(R.string.offer_super_category_product_name_fashion), OfferType.PRODUCT),
    HOME_GARDEN(UiText.StringResource(R.string.offer_super_category_product_name_home_garden), OfferType.PRODUCT),
    SPORT_HOBBY(UiText.StringResource(R.string.offer_super_category_product_name_sport_hobby), OfferType.PRODUCT),
    CULTURE_ENTERTAINMENT(UiText.StringResource(R.string.offer_super_category_product_name_culture_entertainment), OfferType.PRODUCT),
    HEALTH_BEAUTY(UiText.StringResource(R.string.offer_super_category_product_name_health_beauty), OfferType.PRODUCT),
    OTHER_PRODUCTS(UiText.StringResource(R.string.offer_super_category_product_name_other_products), OfferType.PRODUCT),

    // SERVICES
    RENOVATION_CONSTRUCTION(UiText.StringResource(R.string.offer_super_category_service_name_renovation_construction), OfferType.SERVICE),
    GARDEN_SERVICES(UiText.StringResource(R.string.offer_super_category_service_name_garden_services), OfferType.SERVICE),
    TRANSPORT_SERVICES(UiText.StringResource(R.string.offer_super_category_service_name_transport_services), OfferType.SERVICE),
    AUTO_SERVICES(UiText.StringResource(R.string.offer_super_category_service_name_auto_services), OfferType.SERVICE),
    CLEANING_SERVICES(UiText.StringResource(R.string.offer_super_category_service_name_cleaning_services), OfferType.SERVICE),
    EDUCATION(UiText.StringResource(R.string.offer_super_category_service_name_education), OfferType.SERVICE),
    IT_DESIGN(UiText.StringResource(R.string.offer_super_category_service_name_it_design), OfferType.SERVICE),
    EVENTS(UiText.StringResource(R.string.offer_super_category_service_name_events), OfferType.SERVICE),
    BEAUTY_WELLNESS(UiText.StringResource(R.string.offer_super_category_service_name_beauty_wellness), OfferType.SERVICE),
    CARE_SERVICES(UiText.StringResource(R.string.offer_super_category_service_name_care_services), OfferType.SERVICE),
    PROFESSIONAL_SERVICES(UiText.StringResource(R.string.offer_super_category_service_name_professional_services), OfferType.SERVICE),
    OTHER_SERVICES(UiText.StringResource(R.string.offer_super_category_service_name_other_services), OfferType.SERVICE);

    companion object {
        fun getParentsByType(type: OfferType): List<OfferSuperCategory> {
            return entries.filter { it.offerType == type }
        }
    }
}