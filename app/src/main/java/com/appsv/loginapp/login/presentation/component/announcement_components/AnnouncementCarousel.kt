package com.appsv.loginapp.login.presentation.component.announcement_components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnnouncementCarousel(
    announcements: List<String>,
    modifier: Modifier = Modifier
) {
    val itemWidth = 280.dp
    val itemWidthPx = with(LocalDensity.current) { itemWidth.toPx() }
    val spacing = 16.dp
    val spacingPx = with(LocalDensity.current) { spacing.toPx() }

    val listState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(
        SnapLayoutInfoProvider(
            lazyListState = listState,
            snapPosition = SnapPosition.Center
        )
    )

    // Calculate the exact centered item index
    val currentIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) {
                0
            } else {
                val center = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.width / 2
                layoutInfo.visibleItemsInfo
                    .minByOrNull { item ->
                        val itemCenter = item.offset + item.size / 2
                        (itemCenter - center).absoluteValue.toInt() // Convert to comparable Int
                    }?.index ?: 0
            }
        }
    }
    // Auto-scroll logic
    LaunchedEffect(announcements.size) {
        while (true) {
            delay(5000)

            val nextIndex = if (currentIndex >= announcements.lastIndex) 0 else currentIndex + 1

            listState.animateScrollToItem(
                index = nextIndex,
                scrollOffset = 0
            )
        }
    }

    // Pause auto-scroll when user interacts
    var isUserScrolling by remember { mutableStateOf(false) }
    LaunchedEffect(isUserScrolling) {
        if (isUserScrolling) {
            delay(3000) // 3 seconds pause after user interaction
            isUserScrolling = false
        }
    }

    Column(modifier = modifier) {
        LazyRow(
            state = listState,
            flingBehavior = snapBehavior,
            horizontalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            itemsIndexed(announcements) { index, announcement ->
                AnnouncementCard(
                    text = announcement,
                    modifier = Modifier.width(itemWidth),
                    isActive = currentIndex == index
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Indicator dots
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            announcements.forEachIndexed { index, _ ->
                val size by animateDpAsState(
                    targetValue = if (currentIndex == index) 7.dp else 6.dp,
                    label = "indicatorSize"
                )
                val color = if (currentIndex == index) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }

                Box(
                    modifier = Modifier
                        .size(size)
                        .background(
                            color = color,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(2.dp)
                )

                if (index < announcements.lastIndex) {
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}