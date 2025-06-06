package dev.aaa1115910.bv.player.tv.controller

import android.os.CountDownTimer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import dev.aaa1115910.bv.player.entity.*
import dev.aaa1115910.bv.player.seekbar.SeekMoveState
import dev.aaa1115910.bv.player.tv.VideoSeekBar

@Composable
fun ControllerVideoInfo(
    modifier: Modifier = Modifier,
    show: Boolean,
    focusRequester: FocusRequester? = null,
    onHideInfo: () -> Unit,
    isPlayingLambda: () -> Boolean,
    isShowDanmakuLambda: () -> Boolean,
    onClickPlay: () -> Unit = {},
    onClickSupport: () -> Unit = {},
    onClickDanmaku: () -> Unit = {},
    onClickSetting: () -> Unit = {},
    onClickBack: () -> Unit = {},
) {
    val videoPlayerClockData = LocalVideoPlayerClockData.current
    val videoPlayerSeekData = LocalVideoPlayerSeekData.current
    val videoPlayerSeekThumbData = LocalVideoPlayerSeekThumbData.current
    val videoPlayerVideoInfoData = LocalVideoPlayerVideoInfoData.current

    var seekHideTimer: CountDownTimer? by remember { mutableStateOf(null) }
    val setCloseInfoTimer: () -> Unit = {
        if (show) {
            seekHideTimer?.cancel()
            seekHideTimer = object : CountDownTimer(5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() = onHideInfo()
            }
            seekHideTimer?.start()
        } else {
            seekHideTimer?.cancel()
            seekHideTimer = null
        }
    }

    LaunchedEffect(Unit) {
        setCloseInfoTimer()
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(
                    MaterialTheme.shapes.large
                        .copy(topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp))
                )
                .background(Color.Black.copy(0.5f))
                .padding(horizontal = 32.dp, vertical = 16.dp),
            visible = show,
            enter = expandVertically(),
            exit = shrinkVertically(),
            label = "ControllerTopVideoInfo"
        ) {
            Clock(
                hour = videoPlayerClockData.hour,
                minute = videoPlayerClockData.minute,
                second = videoPlayerClockData.second
            )
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = show,
            enter = expandVertically(),
            exit = shrinkVertically(),
            label = "ControllerBottomVideoInfo"
        ) {
            ControllerVideoInfoBottom(
                seekDataLambda = { videoPlayerSeekData },
                title = videoPlayerVideoInfoData.title,
                idleIcon = videoPlayerSeekThumbData.idleIcon,
                movingIcon = videoPlayerSeekThumbData.movingIcon,
                isPlayingLambda = isPlayingLambda,
                isShowDanmakuLambda = isShowDanmakuLambda,
                focusRequester = focusRequester,
                onClickPlay = onClickPlay,
                onClickSupport = onClickSupport,
                onClickDanmaku = onClickDanmaku,
                onClickSetting = onClickSetting,
                onClickBack = onClickBack,
                onFocusBack = onHideInfo,
            )
        }
    }
}

@Composable
fun ControllerVideoInfoTop(
    modifier: Modifier = Modifier,
    title: String,
    clock: Triple<Int, Int, Int>
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                MaterialTheme.shapes.large
                    .copy(topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp))
            )
            .background(Color.Black.copy(0.5f))
            .padding(horizontal = 32.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 100.dp),
            text = title,
            style = MaterialTheme.typography.displaySmall,
            color = Color.White,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Clock(
            hour = clock.first,
            minute = clock.second,
            second = clock.third
        )
    }
}

@Composable
fun ControllerVideoInfoBottom(
    modifier: Modifier = Modifier,
    title: String,
    seekDataLambda: ()-> VideoPlayerSeekData,
    idleIcon: String,
    movingIcon: String,
    isPlayingLambda: () -> Boolean,
    isShowDanmakuLambda: () -> Boolean,
    focusRequester: FocusRequester?,
    onClickPlay: () -> Unit,
    onClickSupport: () -> Unit,
    onClickDanmaku: () -> Unit,
    onClickSetting: () -> Unit,
    onClickBack: () -> Unit,
    onFocusBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(
                MaterialTheme.shapes.large
                    .copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp))
            )
            .background(Color.Black.copy(0.5f))
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            modifier = Modifier
                .padding(top = 16.dp, start = 28.dp, end = 28.dp)
                .fillMaxWidth(),
            text = title,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = (MaterialTheme.typography.displaySmall.fontSize.value - 15).sp
            ),
        )
        VideoSeekBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            seekDataLambda = seekDataLambda,
            moveState = SeekMoveState.Idle,
            idleIcon = idleIcon,
            movingIcon = movingIcon
        )
        VideoBottomController(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            seekDataLambda = seekDataLambda,
            isPlayingLambda = isPlayingLambda,
            isShowDanmakuLambda = isShowDanmakuLambda,
            focusRequester = focusRequester,
            onClickPlay = onClickPlay,
            onClickSupport = onClickSupport,
            onClickDanmaku = onClickDanmaku,
            onClickSetting = onClickSetting,
            onClickBack = onClickBack,
            onFocusBack = onFocusBack,
        )
    }
}

@Composable
private fun Clock(
    modifier: Modifier = Modifier,
    hour: Int,
    minute: Int,
    second: Int
) {
    Text(
        modifier = modifier,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontSize = 32.sp)) {
                append("$hour".padStart(2, '0'))
                append(":")
                append("$minute".padStart(2, '0'))
            }
            withStyle(SpanStyle(fontSize = 18.sp)) {
                append(":")
                append("$second".padStart(2, '0'))
            }
        }
    )
}

@Preview
@Composable
private fun ClockPreview() {
    val clock = Triple(12, 30, 30)
    MaterialTheme {
        Clock(
            hour = clock.first,
            minute = clock.second,
            second = clock.third
        )
    }
}

@Preview(device = "id:tv_1080p")
@Composable
private fun ControllerVideoInfoPreview() {
    var show by remember { mutableStateOf(true) }

    CompositionLocalProvider(
        LocalVideoPlayerSeekData provides VideoPlayerSeekData(
            duration = 100,
            position = 33,
            bufferedPercentage = 66
        ),
        LocalVideoPlayerVideoInfoData provides VideoPlayerVideoInfoData(
            title = "【A320】民航史上最佳逆袭！A320的前世今生！民航史上最佳逆袭！A320的前世今生！",
            partTitle = "2023车队车手介绍分析预测"
        ),
        LocalVideoPlayerClockData provides VideoPlayerClockData(
            hour = 12,
            minute = 30,
            second = 30
        ),
        LocalVideoPlayerSeekThumbData provides VideoPlayerSeekThumbData(
            idleIcon = "",
            movingIcon = ""
        )
    ) {
        MaterialTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { show = !show }) {
                    Text(text = "Switch")
                }
            }
            ControllerVideoInfo(
                modifier = Modifier.fillMaxSize(),
                show = show,
                onHideInfo = {},
                isPlayingLambda = { true },
                isShowDanmakuLambda = { true }
            )
        }
    }
}