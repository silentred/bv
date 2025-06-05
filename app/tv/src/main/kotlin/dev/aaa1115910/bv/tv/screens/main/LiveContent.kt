package dev.aaa1115910.bv.tv.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.aaa1115910.bv.R
import dev.aaa1115910.bv.component.LiveTopNavItem
import dev.aaa1115910.bv.ui.theme.BVTheme
import dev.aaa1115910.bv.util.Prefs
import dev.aaa1115910.bv.util.fInfo
import io.github.oshai.kotlinlogging.KotlinLogging

@Composable
fun LiveContent(
    modifier: Modifier = Modifier,
    contentFocusRequester: FocusRequester,
    isLogin: Boolean = false,
) {

    var logger = KotlinLogging.logger("LiveContent")

    // 已登录的话，优先选择关注的Up直播间
    var initialSelectedTab = LiveTopNavItem.Recommends
    if (isLogin) {
        initialSelectedTab = LiveTopNavItem.Followed
    }

    // 从全局状态获取上次选择的标签位置，如果没有则默认为Dynamics
    // 将这个值提到可组合函数的顶部，避免在重组时重新计算
    val initialSelectedTabIndex = currentSelectedTabs[DrawerItem.Live]
    var selectedTab by remember(initialSelectedTabIndex) {
        mutableStateOf(
            (initialSelectedTabIndex as? LiveTopNavItem)
                ?.let { LiveTopNavItem.entries.getOrNull(it.ordinal) }
                ?: initialSelectedTab
        )
    }

    LaunchedEffect(Unit) {
        logger.fInfo { "SelectedTab is ${initialSelectedTab.name}" }
    }

    Scaffold (
        modifier = modifier.focusRequester(contentFocusRequester),
        topBar = {
            Box(
                modifier = Modifier.padding(
                    start = 48.dp,
                    top = 12.dp,
                    bottom = 8.dp,
                    end = 48.dp
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.title_activity_settings),
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Row (
            modifier = Modifier.padding(innerPadding)
        ) {
            Box {
                Text(
                    text = "left",
                )
            }
            Box {
                Text(
                    text = "right",
                )
            }
        }

    }
}

@Preview(device = "id:tv_1080p")
@Composable
private fun LiveContentPreview() {
    BVTheme {
        LiveContent(contentFocusRequester = FocusRequester())
    }
}