package com.nanaya.r_mj.data

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher:RmjDispatcher)

enum class RmjDispatcher{
    Main,IO,Default
}