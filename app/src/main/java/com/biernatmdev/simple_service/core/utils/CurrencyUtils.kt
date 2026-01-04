package com.biernatmdev.simple_service.core.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyUtils {
    fun getAllCurrencies(): List<String> {
        return Currency.getAvailableCurrencies()
            .map { it.currencyCode }
            .sorted()
    }

    fun getCommonCurrencies(): List<String> {
        val common = listOf("PLN", "EUR", "USD", "GBP")
        val all = getAllCurrencies()
        return (common + (all - common)).distinct()
    }

    fun getSymbol(currencyCode: String): String {
        return try {
            val currency = Currency.getInstance(currencyCode)
            currency.getSymbol(Locale.getDefault())
        } catch (e: Exception) {
            currencyCode
        }
    }

    fun getName(currencyCode: String): String {
        return try {
            val currency = Currency.getInstance(currencyCode)
            currency.getDisplayName(Locale.getDefault())
        } catch (e: Exception) {
            currencyCode
        }
    }

    fun formatPrice(amount: Double, currencyCode: String): String {
        return try {
            val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
            format.currency = Currency.getInstance(currencyCode)
            format.format(amount)
        } catch (e: Exception) {
            "$amount $currencyCode"
        }
    }
}