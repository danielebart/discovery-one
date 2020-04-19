package com.discoveryone.result

interface ResultSpy {

    fun recordResult(result: Any)

    fun getRecorderResults(): List<Any>
}