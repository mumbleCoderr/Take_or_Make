package com.biernatmdev.simple_service.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.fallback
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferStatus
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.REGULAR
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.FavouriteFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.FavouriteOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Offering
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Product
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Requesting
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Service
import com.biernatmdev.simple_service.core.ui.theme.Resources.Image.AppForegroundImage
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker

@Composable
fun OfferCard(
    modifier: Modifier = Modifier,
    offer: Offer,
    isPhotoLeading: Boolean,
    isTakeModule: Boolean = false,
    onOfferCardClick: (Offer) -> Unit,
    onFavouriteClick: (Offer) -> Unit = { }
) {
    @Composable
    fun FavouriteIconButton(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        isFavourite: Boolean,
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Icon(
                imageVector = if(isFavourite) FavouriteFilled else FavouriteOutlined,
                tint = ColorPrimary,
                contentDescription = "Add to favorites",
                modifier = Modifier.size(28.dp)
            )
        }
    }

    @Composable
    fun imageSection(modifier: Modifier) {
        Box(modifier = modifier) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(offer.images.firstOrNull())
                    .fallback(AppForegroundImage)
                    .crossfade(true)
                    .build(),
                contentDescription = UiText.StringResource(R.string.offer_photo).asString(),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Composable
    fun detailsSection(modifier: Modifier) {
        val shouldShowFavouriteInline = isTakeModule && !isPhotoLeading

        Column(
            modifier = modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            if (shouldShowFavouriteInline) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = offer.title,
                        fontSize = REGULAR,
                        lineHeight = LineHeight.REGULAR,
                        color = onColorBackground,
                        fontWeight = FontWeight.Bold,
                        fontFamily = momoFont(),
                        maxLines = 2,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    if (isTakeModule) {
                        FavouriteIconButton(
                            onClick = { onFavouriteClick(offer) },
                            modifier = Modifier.offset(y = (-8).dp, x = 8.dp),
                            isFavourite = offer.isFavourite,
                        )
                    }
                }
            } else {
                Text(
                    text = offer.title,
                    fontSize = REGULAR,
                    lineHeight = LineHeight.REGULAR,
                    color = onColorBackground,
                    fontWeight = FontWeight.Bold,
                    fontFamily = momoFont(),
                    maxLines = 2,
                    modifier = Modifier.padding(
                        end = if (isTakeModule && isPhotoLeading) 24.dp else 0.dp,
                    )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = if (offer.transactionType == TransactionType.OFFER) painterResource(
                            Offering
                        ) else painterResource(
                            Requesting
                        ),
                        tint = Color.Unspecified,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Icon(
                        painter = if (offer.offerType == OfferType.PRODUCT) painterResource(Product) else painterResource(
                            Service
                        ),
                        tint = Color.Unspecified,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Text(
                text = "${offer.price ?: "-"} ${offer.currency} / ${offer.priceUnit.displayName.asString()}",
                fontSize = REGULAR,
                lineHeight = LineHeight.REGULAR,
                color = onColorBackgroundDarker,
                fontWeight = FontWeight.Bold,
                fontFamily = momoFont(),
            )
        }
    }

    Surface(
        color = ColorSecondary,
        shape = RoundedCornerShape(22.dp),
        modifier = modifier
            .height(140.dp)
            .clickable { onOfferCardClick(offer) },
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imageSectionModifier = Modifier
                    .fillMaxHeight()
                    .weight(0.4f)
                val detailsSectionModifier = Modifier
                    .fillMaxHeight()
                    .weight(0.6f)

                if (isPhotoLeading) {
                    imageSection(imageSectionModifier)
                    detailsSection(detailsSectionModifier)
                } else {
                    detailsSection(detailsSectionModifier)
                    imageSection(imageSectionModifier)
                }
            }
            if (offer.status == OfferStatus.INACTIVE) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                )
            }

            if (isTakeModule && isPhotoLeading) {
                FavouriteIconButton(
                    onClick = { onFavouriteClick(offer) },
                    modifier = Modifier.align(Alignment.TopEnd),
                    isFavourite = offer.isFavourite
                )
            }
        }
    }
}