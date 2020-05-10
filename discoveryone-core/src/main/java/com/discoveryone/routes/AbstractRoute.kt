package com.discoveryone.routes

import kotlin.reflect.KClass

interface AbstractRoute {
    val clazz: KClass<*>
}

interface GeneratedFragmentRoute : AbstractRoute {

    val containerId: Int
}

interface GeneratedActivityRoute : AbstractRoute

interface GeneratedDialogFragmentRoute : AbstractRoute