package dev.aaa1115910.bv.component

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import dev.aaa1115910.biliapi.entity.pgc.PgcType
import dev.aaa1115910.biliapi.entity.ugc.UgcTypeV2
import dev.aaa1115910.bv.BVApp
import dev.aaa1115910.bv.util.getDisplayName
import dev.aaa1115910.bv.util.ifElse
import dev.aaa1115910.bv.util.isDpadLeft
import dev.aaa1115910.bv.util.isKeyDown

@Composable
fun TopNav(
    modifier: Modifier = Modifier,
    items: List<TopNavItem>,
    isLargePadding: Boolean,
    initialSelectedItem: TopNavItem? = null,
    onSelectedChanged: (TopNavItem) -> Unit = {},
    onClick: (TopNavItem) -> Unit = {},
    onLeftKeyEvent: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    var selectedNav by remember(initialSelectedItem) {
        mutableStateOf(initialSelectedItem ?: items.first())
    }

    var selectedTabIndex by remember(initialSelectedItem) {
        mutableIntStateOf(
            if (initialSelectedItem != null) {
                val index = items.indexOf(initialSelectedItem)
                if (index >= 0) index else 0
            } else 0
        )
    }

    val verticalPadding by animateDpAsState(
        targetValue = if (isLargePadding) 24.dp else 12.dp,
        label = "top nav vertical padding"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 0.dp,
                end = 12.dp,
                top = verticalPadding - 12.dp,
                bottom = verticalPadding
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        TabRow(
            modifier = Modifier
                .focusRestorer(focusRequester)
                .onPreviewKeyEvent { keyEvent ->
                    // 只有在最左边的选项，按左键时才向外传递事件
                    if (keyEvent.isDpadLeft() && keyEvent.isKeyDown() && selectedTabIndex == 0) {
                        onLeftKeyEvent()
                        return@onPreviewKeyEvent true
                    }
                    false
                },
            selectedTabIndex = selectedTabIndex,
            separator = { Spacer(modifier = Modifier.width(12.dp)) },
        ) {
            items.forEachIndexed { index, tab ->
                NavItemTab(
                    modifier = Modifier
                        .ifElse(index == 0, Modifier.focusRequester(focusRequester)),
                    topNavItem = tab,
                    selected = index == selectedTabIndex,
                    onFocus = {
                        selectedNav = tab
                        selectedTabIndex = index
                        onSelectedChanged(tab)
                    },
                    onClick = { onClick(tab) }
                )
            }
        }
    }
}

@Composable
private fun TabRowScope.NavItemTab(
    modifier: Modifier = Modifier,
    topNavItem: TopNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    onFocus: () -> Unit
) {
    val context = LocalContext.current

    Tab(
        modifier = modifier,
        selected = selected,
        onFocus = onFocus,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier
                .height(32.dp)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            text = topNavItem.getDisplayName(context),
            color = LocalContentColor.current,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

interface TopNavItem {
    fun getDisplayName(context: Context = BVApp.context): String
}

enum class LiveTopNavItem(private val displayName: String) : TopNavItem {
    Recommends("推荐"),
    Followed("关注");

    override fun getDisplayName(context: Context): String {
        return displayName
    }
}

enum class HomeTopNavItem(private val displayName: String) : TopNavItem {
    Recommend("推荐"),
    Popular("热门"),
    Dynamics("动态"),
    User("个人");

    override fun getDisplayName(context: Context): String {
        return displayName
    }
}

enum class UgcTopNavItem(private val ugcType: UgcTypeV2) : TopNavItem {
    Douga(UgcTypeV2.Douga),
    Game(UgcTypeV2.Game),
    Kichiku(UgcTypeV2.Kichiku),
    Music(UgcTypeV2.Music),
    Dance(UgcTypeV2.Dance),
    Cinephile(UgcTypeV2.Cinephile),
    Ent(UgcTypeV2.Ent),
    Knowledge(UgcTypeV2.Knowledge),
    Tech(UgcTypeV2.Tech),
    Information(UgcTypeV2.Information),
    Food(UgcTypeV2.Food),
    ShortPlay(UgcTypeV2.Shortplay),
    Car(UgcTypeV2.Car),
    Fashion(UgcTypeV2.Fashion),
    Sports(UgcTypeV2.Sports),
    Animal(UgcTypeV2.Animal),
    Vlog(UgcTypeV2.Vlog),
    Painting(UgcTypeV2.Painting),
    Ai(UgcTypeV2.Ai),
    Home(UgcTypeV2.Home),
    Outdoors(UgcTypeV2.Outdoors),
    Gym(UgcTypeV2.Gym),
    Handmake(UgcTypeV2.Handmake),
    Travel(UgcTypeV2.Travel),
    Rural(UgcTypeV2.Rural),
    Parenting(UgcTypeV2.Parenting),
    Health(UgcTypeV2.Health),
    Emotion(UgcTypeV2.Emotion),
    LifeJoy(UgcTypeV2.LifeJoy),
    LifeExperience(UgcTypeV2.LifeExperience),
    Mysticism(UgcTypeV2.Mysticism);

    override fun getDisplayName(context: Context): String {
        return ugcType.getDisplayName(context)
    }
}

enum class PgcTopNavItem(private val pgcType: PgcType) : TopNavItem {
    Anime(PgcType.Anime),
    GuoChuang(PgcType.GuoChuang),
    Movie(PgcType.Movie),
    Documentary(PgcType.Documentary),
    Tv(PgcType.Tv),
    Variety(PgcType.Variety);

    override fun getDisplayName(context: Context): String {
        return pgcType.getDisplayName(context)
    }
}