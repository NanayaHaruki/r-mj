package com.nanaya.r_mj.data.network

import com.nanaya.r_mj.data.ApiService
import com.nanaya.r_mj.data.Dispatcher
import com.nanaya.r_mj.data.RmjDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PlayerFetcher@Inject constructor(
    private val apiService: ApiService,
    @Dispatcher(RmjDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {

}