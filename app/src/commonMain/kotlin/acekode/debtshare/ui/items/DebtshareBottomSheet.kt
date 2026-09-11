package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareTheme
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun DebtshareBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    partialHeightFraction: Float = 0.65f,
    containerColor: Color = DebtshareTheme.colors.background,
    content: @Composable () -> Unit,
) {
    if (!visible) return

    val scope = rememberCoroutineScope()
    var sheetOffsetY by remember { mutableFloatStateOf(Float.MAX_VALUE) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val fullHeightPx = constraints.maxHeight.toFloat()
        val anchors = remember(fullHeightPx, partialHeightFraction) {
            SheetAnchors(
                expanded = 0f,
                partial = fullHeightPx * (1f - partialHeightFraction),
                hidden = fullHeightPx,
            )
        }

        LaunchedEffect(fullHeightPx) {
            if (sheetOffsetY == Float.MAX_VALUE || sheetOffsetY >= anchors.hidden) {
                sheetOffsetY = anchors.hidden
                animate(
                    initialValue = anchors.hidden,
                    targetValue = anchors.partial,
                    animationSpec = tween(durationMillis = 300),
                ) { value, _ -> sheetOffsetY = value }
            }
        }

        fun settleToAnchor(velocity: Float) {
            val allAnchors = listOf(anchors.expanded, anchors.partial, anchors.hidden)
            val target = when {
                velocity > 1500f -> allAnchors.firstOrNull { it > sheetOffsetY } ?: anchors.hidden
                velocity < -1500f -> allAnchors.lastOrNull { it < sheetOffsetY } ?: anchors.expanded
                else -> allAnchors.minByOrNull { abs(it - sheetOffsetY) } ?: anchors.partial
            }
            scope.launch {
                animate(
                    initialValue = sheetOffsetY,
                    targetValue = target,
                    animationSpec = tween(durationMillis = 300),
                ) { value, _ -> sheetOffsetY = value }
                if (target == anchors.hidden) onDismissRequest()
            }
        }

        SheetScrim(offsetY = sheetOffsetY, fullHeightPx = fullHeightPx)

        SheetSurface(
            offsetY = sheetOffsetY,
            anchors = anchors,
            containerColor = containerColor,
            onDrag = { dragAmount ->
                sheetOffsetY = (sheetOffsetY + dragAmount)
                    .coerceIn(anchors.expanded, anchors.hidden)
            },
            onSettle = { settleToAnchor(0f) },
            nestedScrollConnection = remember(anchors) {
                SheetNestedScrollConnection(
                    anchors = anchors,
                    currentOffset = { sheetOffsetY },
                    onDrag = { delta ->
                        val prev = sheetOffsetY
                        sheetOffsetY = (prev + delta).coerceIn(anchors.expanded, anchors.hidden)
                        sheetOffsetY - prev
                    },
                    onSettle = { velocity -> settleToAnchor(velocity) },
                )
            },
            content = content,
        )
    }
}

@Composable
private fun SheetScrim(offsetY: Float, fullHeightPx: Float) {
    val progress = if (offsetY >= fullHeightPx) 0f else (1f - offsetY / fullHeightPx).coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = progress * 0.32f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            ),
    )
}

@Composable
private fun SheetSurface(
    offsetY: Float,
    anchors: SheetAnchors,
    containerColor: Color,
    onDrag: (Float) -> Unit,
    onSettle: () -> Unit,
    nestedScrollConnection: NestedScrollConnection,
    content: @Composable () -> Unit,
) {
    val sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .nestedScroll(nestedScrollConnection)
            .clip(sheetShape)
            .background(containerColor)
            .pointerInput(anchors) {
                detectVerticalDragGestures(
                    onDragEnd = { onSettle() },
                    onVerticalDrag = { _, dragAmount -> onDrag(dragAmount) },
                )
            }
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DragHandle()
        content()
    }
}

@Composable
private fun DragHandle() {
    Spacer(modifier = Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .height(4.dp)
            .fillMaxWidth(0.1f)
            .clip(RoundedCornerShape(2.dp))
            .background(DebtshareTheme.colors.textSecondary),
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Stable
private data class SheetAnchors(
    val expanded: Float,
    val partial: Float,
    val hidden: Float,
)

private class SheetNestedScrollConnection(
    private val anchors: SheetAnchors,
    private val currentOffset: () -> Float,
    private val onDrag: (Float) -> Float,
    private val onSettle: (Float) -> Unit,
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y
        if (delta > 0 && currentOffset() < anchors.partial) {
            return Offset(0f, onDrag(delta))
        }
        return Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val delta = available.y
        if (delta < 0 && currentOffset() > anchors.expanded) {
            return Offset(0f, onDrag(delta))
        }
        if (delta > 0 && currentOffset() < anchors.hidden) {
            return Offset(0f, onDrag(delta))
        }
        return Offset.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        onSettle(available.y)
        return available
    }
}
