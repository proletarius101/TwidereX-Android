/*
 *  Twidere X
 *
 *  Copyright (C) 2020-2021 Tlaster <tlaster@outlook.com>
 * 
 *  This file is part of Twidere X.
 * 
 *  Twidere X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  Twidere X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Twidere X. If not, see <http://www.gnu.org/licenses/>.
 */
package com.twidere.twiderex.scenes.search.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import com.twidere.twiderex.R
import com.twidere.twiderex.component.foundation.SwipeToRefreshLayout
import com.twidere.twiderex.component.lazy.LazyColumn2
import com.twidere.twiderex.component.lazy.collectAsLazyPagingItems
import com.twidere.twiderex.component.lazy.itemsPagingGridIndexed
import com.twidere.twiderex.component.lazy.loadState
import com.twidere.twiderex.component.navigation.LocalNavigator
import com.twidere.twiderex.component.status.StatusMediaPreviewItem
import com.twidere.twiderex.di.assisted.assistedViewModel
import com.twidere.twiderex.extensions.refreshOrRetry
import com.twidere.twiderex.preferences.proto.DisplayPreferences
import com.twidere.twiderex.ui.LocalActiveAccount
import com.twidere.twiderex.ui.LocalVideoPlayback
import com.twidere.twiderex.ui.standardPadding
import com.twidere.twiderex.viewmodel.twitter.search.TwitterSearchMediaViewModel

class TwitterSearchMediaItem : SearchSceneItem {
    @Composable
    override fun name(): String {
        return stringResource(id = R.string.scene_search_tabs_media)
    }

    @Composable
    override fun Content(keyword: String) {
        val account = LocalActiveAccount.current ?: return
        val viewModel =
            assistedViewModel<TwitterSearchMediaViewModel.AssistedFactory, TwitterSearchMediaViewModel> {
                it.create(account, keyword)
            }
        val source = viewModel.source.collectAsLazyPagingItems()
        CompositionLocalProvider(
            LocalVideoPlayback provides DisplayPreferences.AutoPlayback.Off
        ) {
            SwipeToRefreshLayout(
                refreshingState = source.loadState.refresh is LoadState.Loading,
                onRefresh = {
                    source.refreshOrRetry()
                }
            ) {
                if (source.itemCount > 0) {
                    LazyColumn2 {
                        item {
                            Box(modifier = Modifier.height(standardPadding))
                        }
                        itemsPagingGridIndexed(
                            source,
                            rowSize = 2,
                            spacing = standardPadding,
                            padding = standardPadding
                        ) { index, pair ->
                            pair?.let { item ->
                                val navigator = LocalNavigator.current
                                StatusMediaPreviewItem(
                                    item.first,
                                    modifier = Modifier
                                        .aspectRatio(1F),
                                    onClick = {
                                        navigator.media(item.second.statusKey, index)
                                    }
                                )
                            }
                        }
                        item {
                            Box(modifier = Modifier.height(standardPadding))
                        }
                        loadState(source.loadState.append) {
                            source.retry()
                        }
                    }
                }
            }
        }
    }
}
