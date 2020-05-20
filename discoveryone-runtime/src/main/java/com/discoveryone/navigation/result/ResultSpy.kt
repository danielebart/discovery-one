package com.discoveryone.navigation.result

interface ResultSpy {

    fun recordResult(result: Any)

    fun getRecorderResults(): List<Any>
}