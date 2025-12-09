package com.biernatmdev.simple_service.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.biernatmdev.simple_service.R

@Composable
fun robotoFont() = FontFamily(
    Font(R.font.roboto_font)
)

@Composable
fun momoFont() = FontFamily(
    Font(R.font.momo_font)
)

object FontSize {
    val EXTRA_SMALL = 10.sp
    val SMALL = 12.sp
    val REGULAR = 14.sp
    val EXTRA_REGULAR = 16.sp
    val MEDIUM = 18.sp
    val EXTRA_MEDIUM = 20.sp
    val SEMI_LARGE = 25.sp
    val LARGE = 30.sp
    val EXTRA_LARGE = 40.sp
}

object LineHeight {
    val EXTRA_SMALL = 16.sp
    val SMALL = 16.sp

    val REGULAR = 20.sp
    val EXTRA_REGULAR = 24.sp

    val MEDIUM = 24.sp
    val EXTRA_MEDIUM = 28.sp

    val SEMI_LARGE = 32.sp
    val LARGE = 36.sp
    val EXTRA_LARGE = 48.sp
}
