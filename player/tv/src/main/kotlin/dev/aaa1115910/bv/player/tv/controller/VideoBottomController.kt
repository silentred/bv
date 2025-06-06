package dev.aaa1115910.bv.player.tv.controller

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import dev.aaa1115910.bv.player.entity.VideoPlayerSeekData
import dev.aaa1115910.bv.util.formatMinSec

/**
 * @author LiCheng
 * @date 2025/6/4
 * @desc
 */
@Composable
fun VideoBottomController(
    modifier: Modifier = Modifier,
    seekDataLambda: () -> VideoPlayerSeekData,
    isPlayingLambda: () -> Boolean,
    isShowDanmakuLambda: () -> Boolean,
    focusRequester: FocusRequester? = null,
    onClickPlay: () -> Unit = {},
    onClickSupport: () -> Unit = {},
    onClickDanmaku: () -> Unit = {},
    onClickSetting: () -> Unit = {},
    onClickBack: () -> Unit = {},
    onFocusBack: () -> Unit = {},
) {
    val controlItems = remember {
        listOf(
            ControlItemData(
                imageVector = { if (isPlayingLambda()) Icons.Rounded.Pause else Icons.Rounded.PlayArrow },
                onClick = onClickPlay,
                showSlash = { false }
            ),
            ControlItemData(
                imageVector = { Icons.Outlined.ClearAll },
                onClick = onClickDanmaku,
                showSlash = { isShowDanmakuLambda() }
            ),
            ControlItemData(
                imageVector = { Icons.Rounded.ThumbUp },
                onClick = onClickSupport,
                showSlash = { false }
            ),
            ControlItemData(
                imageVector = { Icons.Rounded.Settings },
                onClick = onClickSetting,
                showSlash = { false }
            ),
            ControlItemData(
                imageVector = { Icons.Rounded.Close },
                onClick = onClickBack,
                showSlash = { false }
            )
        )
    }
    
    var selectedIndex by remember { mutableIntStateOf(0) }
    
    Row(
        modifier = modifier
            .focusRequester(focusRequester ?: remember { FocusRequester() })
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when (keyEvent.key) {
                        Key.DirectionLeft -> {
                            selectedIndex = if (selectedIndex > 0) selectedIndex - 1 else controlItems.size - 1
                            true
                        }
                        Key.DirectionRight -> {
                            selectedIndex = if (selectedIndex < controlItems.size - 1) selectedIndex + 1 else 0
                            true
                        }
                        Key.DirectionCenter, Key.Enter -> {
                            controlItems[selectedIndex].onClick()
                            true
                        }
                        Key.DirectionUp,
                        Key.DirectionDown,
                        Key.Back-> {
                            onFocusBack()
                            true
                        }
                        else -> false
                    }
                } else false
            }
    ) {
        controlItems.forEachIndexed { index, item ->
            ControlItem(
                imageVector = item.imageVector(),
                showSlash = item.showSlash(),
                isSelected = selectedIndex == index,
                onClick = item.onClick
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        TimeInfo(
            seekDataLambda = seekDataLambda
        )
    }
}

private data class ControlItemData(
    val imageVector: () -> ImageVector,
    val onClick: () -> Unit,
    val showSlash: () -> Boolean
)

@Composable
private fun ControlItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    showSlash: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier
            .padding(end = 10.dp)
            .clickable {
                onClick()
            },
        colors = SurfaceDefaults.colors(
            containerColor = if (isSelected) Color.White.copy(0.5f) else Color.Black.copy(0.5f)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(
            modifier = Modifier
                .padding(12.dp, 4.dp)
                .size(40.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = Color.White
        )
        if (showSlash) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .padding(8.dp)
            ) {
                val gap = 15f
                drawLine(
                    color = Color.White,
                    start = Offset(gap, gap),
                    end = Offset(size.width - gap, size.height - gap),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun TimeInfo(
    modifier: Modifier = Modifier,
    seekDataLambda: () -> VideoPlayerSeekData,
) {
    val seekData = seekDataLambda()
    Text(
        modifier = modifier.padding(top = 16.dp, bottom = 0.dp, end = 40.dp),
        text = "${seekData.position.formatMinSec()} / ${seekData.duration.formatMinSec()}",
        color = Color.White
    )
}