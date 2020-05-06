@file:Suppress("unused")

package com.discoveryone.processor.fakes

abstract class Activity {

    val intent: Intent = Intent()

    class Intent {

        val extras: Bundle = Bundle()
    }
}
