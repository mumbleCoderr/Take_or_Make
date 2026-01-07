package com.biernatmdev.simple_service.core.offer.domain.enums

import android.os.Parcelable
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
enum class OfferCategory(
    override val displayName: UiText,
    val superCategory: OfferSuperCategory?
) : CategoryDisplayable, Parcelable {

    ANY(UiText.StringResource(R.string.offer_super_category_product_name_any), null),

    // PRODUCTS

    // --- Super: ELECTRONICS ---
    SMARTPHONES(UiText.StringResource(R.string.offer_category_product_name_smartphones), OfferSuperCategory.ELECTRONICS),
    LAPTOPS(UiText.StringResource(R.string.offer_category_product_name_laptops), OfferSuperCategory.ELECTRONICS),
    PC_COMPUTERS(UiText.StringResource(R.string.offer_category_product_name_pc_computers), OfferSuperCategory.ELECTRONICS),
    CONSOLES(UiText.StringResource(R.string.offer_category_product_name_consoles), OfferSuperCategory.ELECTRONICS),
    TV(UiText.StringResource(R.string.offer_category_product_name_tv), OfferSuperCategory.ELECTRONICS),
    AUDIO(UiText.StringResource(R.string.offer_category_product_name_audio), OfferSuperCategory.ELECTRONICS),
    CAMERAS(UiText.StringResource(R.string.offer_category_product_name_cameras), OfferSuperCategory.ELECTRONICS),
    PC_PARTS(UiText.StringResource(R.string.offer_category_product_name_pc_parts), OfferSuperCategory.ELECTRONICS),

    // --- Super: AUTOMOTIVE ---
    CARS(UiText.StringResource(R.string.offer_category_product_name_cars), OfferSuperCategory.AUTOMOTIVE),
    MOTORCYCLES(UiText.StringResource(R.string.offer_category_product_name_motorcycles), OfferSuperCategory.AUTOMOTIVE),
    CAR_PARTS(UiText.StringResource(R.string.offer_category_product_name_car_parts), OfferSuperCategory.AUTOMOTIVE),
    TIRES(UiText.StringResource(R.string.offer_category_product_name_tires), OfferSuperCategory.AUTOMOTIVE),

    // --- Super: FASHION ---
    CLOTHING_WOMEN(UiText.StringResource(R.string.offer_category_product_name_clothing_women), OfferSuperCategory.FASHION),
    CLOTHING_MEN(UiText.StringResource(R.string.offer_category_product_name_clothing_men), OfferSuperCategory.FASHION),
    SHOES(UiText.StringResource(R.string.offer_category_product_name_shoes), OfferSuperCategory.FASHION),
    ACCESSORIES(UiText.StringResource(R.string.offer_category_product_name_accessories), OfferSuperCategory.FASHION),
    JEWELRY(UiText.StringResource(R.string.offer_category_product_name_jewelry), OfferSuperCategory.FASHION),

    // --- Super: HOME_GARDEN ---
    FURNITURE(UiText.StringResource(R.string.offer_category_product_name_furniture), OfferSuperCategory.HOME_GARDEN),
    DECORATION(UiText.StringResource(R.string.offer_category_product_name_decoration), OfferSuperCategory.HOME_GARDEN),
    TOOLS(UiText.StringResource(R.string.offer_category_product_name_tools), OfferSuperCategory.HOME_GARDEN),
    BUILDING_MATERIALS(UiText.StringResource(R.string.offer_category_product_name_building_materials), OfferSuperCategory.HOME_GARDEN),

    // --- Super: SPORT_HOBBY ---
    BICYCLES(UiText.StringResource(R.string.offer_category_product_name_bicycles), OfferSuperCategory.SPORT_HOBBY),
    GYM(UiText.StringResource(R.string.offer_category_product_name_gym), OfferSuperCategory.SPORT_HOBBY),
    MUSICAL_INSTRUMENTS(UiText.StringResource(R.string.offer_category_product_name_musical_instruments), OfferSuperCategory.SPORT_HOBBY),

    // --- Super: CULTURE_ENTERTAINMENT ---
    BOOKS(UiText.StringResource(R.string.offer_category_product_name_books), OfferSuperCategory.CULTURE_ENTERTAINMENT),
    GAMES(UiText.StringResource(R.string.offer_category_product_name_games), OfferSuperCategory.CULTURE_ENTERTAINMENT),
    MOVIES(UiText.StringResource(R.string.offer_category_product_name_movies), OfferSuperCategory.CULTURE_ENTERTAINMENT),

    // --- Super: HEALTH_BEAUTY ---
    PERFUME(UiText.StringResource(R.string.offer_category_product_name_perfume), OfferSuperCategory.HEALTH_BEAUTY),
    MAKEUP(UiText.StringResource(R.string.offer_category_product_name_makeup), OfferSuperCategory.HEALTH_BEAUTY),

    // --- Super: OTHER_PRODUCTS ---
    OTHER_PRODUCT_ITEM(UiText.StringResource(R.string.offer_category_product_name_other), OfferSuperCategory.OTHER_PRODUCTS),


    // SERVICES


    // --- Super: RENOVATION_CONSTRUCTION ---
    RENOVATION_GENERAL(UiText.StringResource(R.string.offer_category_service_name_renovation_general), OfferSuperCategory.RENOVATION_CONSTRUCTION),
    PAINTING(UiText.StringResource(R.string.offer_category_service_name_painting), OfferSuperCategory.RENOVATION_CONSTRUCTION),
    PLUMBING(UiText.StringResource(R.string.offer_category_service_name_plumbing), OfferSuperCategory.RENOVATION_CONSTRUCTION),
    ELECTRICIAN(UiText.StringResource(R.string.offer_category_service_name_electrician), OfferSuperCategory.RENOVATION_CONSTRUCTION),
    TILING(UiText.StringResource(R.string.offer_category_service_name_tiling), OfferSuperCategory.RENOVATION_CONSTRUCTION),
    CARPENTRY(UiText.StringResource(R.string.offer_category_service_name_carpentry), OfferSuperCategory.RENOVATION_CONSTRUCTION),

    // --- Super: GARDEN_SERVICES ---
    GARDENING(UiText.StringResource(R.string.offer_category_service_name_gardening), OfferSuperCategory.GARDEN_SERVICES),
    PAVING(UiText.StringResource(R.string.offer_category_service_name_paving), OfferSuperCategory.GARDEN_SERVICES),

    // --- Super: TRANSPORT_SERVICES ---
    MOVING(UiText.StringResource(R.string.offer_category_service_name_moving), OfferSuperCategory.TRANSPORT_SERVICES),
    TRANSPORT_FURNITURE(UiText.StringResource(R.string.offer_category_service_name_transport_furniture), OfferSuperCategory.TRANSPORT_SERVICES),
    FOOD_DELIVERY(UiText.StringResource(R.string.offer_category_service_name_food_delivery), OfferSuperCategory.TRANSPORT_SERVICES),

    // --- Super: AUTO_SERVICES ---
    MECHANIC(UiText.StringResource(R.string.offer_category_service_name_mechanic), OfferSuperCategory.AUTO_SERVICES),
    TIRE_CHANGE(UiText.StringResource(R.string.offer_category_service_name_tire_change), OfferSuperCategory.AUTO_SERVICES),
    AUTO_DETAILING(UiText.StringResource(R.string.offer_category_service_name_auto_detailing), OfferSuperCategory.AUTO_SERVICES),

    // --- Super: CLEANING_SERVICES ---
    HOME_CLEANING(UiText.StringResource(R.string.offer_category_service_name_home_cleaning), OfferSuperCategory.CLEANING_SERVICES),
    OFFICE_CLEANING(UiText.StringResource(R.string.offer_category_service_name_office_cleaning), OfferSuperCategory.CLEANING_SERVICES),
    CAR_WASH(UiText.StringResource(R.string.offer_category_service_name_car_wash), OfferSuperCategory.CLEANING_SERVICES),

    // --- Super: EDUCATION ---
    TUTORING_LANGUAGES(UiText.StringResource(R.string.offer_category_service_name_tutoring_languages), OfferSuperCategory.EDUCATION),
    TUTORING_MATH(UiText.StringResource(R.string.offer_category_service_name_tutoring_math), OfferSuperCategory.EDUCATION),
    DRIVING_LESSONS(UiText.StringResource(R.string.offer_category_service_name_driving_lessons), OfferSuperCategory.EDUCATION),

    // --- Super: IT_DESIGN ---
    IT_SUPPORT(UiText.StringResource(R.string.offer_category_service_name_it_support), OfferSuperCategory.IT_DESIGN),
    WEB_DEV(UiText.StringResource(R.string.offer_category_service_name_web_dev), OfferSuperCategory.IT_DESIGN),
    GRAPHICS(UiText.StringResource(R.string.offer_category_service_name_graphics), OfferSuperCategory.IT_DESIGN),

    // --- Super: EVENTS ---
    DJ_MUSIC(UiText.StringResource(R.string.offer_category_service_name_dj_music), OfferSuperCategory.EVENTS),
    PHOTOGRAPHY(UiText.StringResource(R.string.offer_category_service_name_photography), OfferSuperCategory.EVENTS),
    CATERING(UiText.StringResource(R.string.offer_category_service_name_catering), OfferSuperCategory.EVENTS),

    // --- Super: BEAUTY_WELLNESS ---
    HAIRDRESSER(UiText.StringResource(R.string.offer_category_service_name_hairdresser), OfferSuperCategory.BEAUTY_WELLNESS),
    BARBER(UiText.StringResource(R.string.offer_category_service_name_barber), OfferSuperCategory.BEAUTY_WELLNESS),
    NAILS(UiText.StringResource(R.string.offer_category_service_name_nails), OfferSuperCategory.BEAUTY_WELLNESS),

    // --- Super: CARE_SERVICES ---
    BABYSITTING(UiText.StringResource(R.string.offer_category_service_name_babysitting), OfferSuperCategory.CARE_SERVICES),
    PET_SITTING(UiText.StringResource(R.string.offer_category_service_name_pet_sitting), OfferSuperCategory.CARE_SERVICES),

    // --- Super: PROFESSIONAL_SERVICES ---
    ACCOUNTING(UiText.StringResource(R.string.offer_category_service_name_accounting), OfferSuperCategory.PROFESSIONAL_SERVICES),
    LEGAL_ADVICE(UiText.StringResource(R.string.offer_category_service_name_legal_advice), OfferSuperCategory.PROFESSIONAL_SERVICES),
    INSURANCE(UiText.StringResource(R.string.offer_category_service_name_insurance), OfferSuperCategory.PROFESSIONAL_SERVICES),
    TRANSLATION(UiText.StringResource(R.string.offer_category_service_name_translation), OfferSuperCategory.PROFESSIONAL_SERVICES),
    TAILORING(UiText.StringResource(R.string.offer_category_service_name_tailoring), OfferSuperCategory.PROFESSIONAL_SERVICES),
    SHOE_REPAIR(UiText.StringResource(R.string.offer_category_service_name_shoe_repair), OfferSuperCategory.PROFESSIONAL_SERVICES),

    // --- Super: OTHER_SERVICES ---
    OTHER_SERVICE_ITEM(UiText.StringResource(R.string.offer_category_service_name_other), OfferSuperCategory.OTHER_SERVICES);

    val type: OfferType?
        get() = superCategory?.offerType

    companion object {
        fun getSubcategories(parent: OfferSuperCategory): List<OfferCategory> {
            return entries.filter { it == ANY || it.superCategory == parent }
        }

        fun fromString(name: String): OfferCategory {
            return try {
                valueOf(name)
            } catch (e: Exception) {
                OTHER_SERVICE_ITEM
            }
        }
    }
}