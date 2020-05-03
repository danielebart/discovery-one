package com.discoveryone.testutils

import com.discoveryone.navigation.result.ResultSpy

class TestResultSpy: ResultSpy {

    private val results: MutableList<Any> = mutableListOf()

    override fun recordResult(result: Any) {
        results.add(result)
    }

    override fun getRecorderResults(): List<Any> =
        results
}