package com.discoveryone

import com.discoveryone.stubs.StubNavigator

object DiscoveryOne {

    var navigator: Navigator = StubNavigator()
        private set

    fun install(navigator: Navigator) {
        this.navigator = navigator
    }
}
