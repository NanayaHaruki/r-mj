package com.nanaya.r_mj.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nanaya.r_mj.data.IAppContainer
import com.nanaya.r_mj.ui.home.HomeViewModel
import com.nanaya.r_mj.ui.home.RmjTopBar
import com.nanaya.r_mj.ui.home.HomeContent

@Composable
fun RmjApp(
    appContainer: IAppContainer
) {
    val vm: HomeViewModel = viewModel(
        HomeViewModel::class.java,
        factory = HomeViewModel.providerFactory(appContainer.schoolRepo)
    )
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val areaLabels = listOf("全国", "华东", "华北", "东北", "中南", "西部")
    Scaffold(
        topBar = {
            RmjTopBar(
                title = if (uiState.detail == null) "雀庄公式战" else uiState.detail!!.name,
                showNavBtn = uiState.detail != null,
                backAction = { vm.navToList() },
                searchAction = { vm.searchSchools(it) }
            )
        }

    ) { innerPadding ->

        HomeContent(innerPadding, areaLabels, vm, uiState)

    }
}

