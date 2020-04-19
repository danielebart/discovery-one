package com.discoveryone.result

import com.discoveryone.ResultToken
import java.util.concurrent.atomic.AtomicInteger

object ResultTokenGenerator {

    private val atomicInteger = AtomicInteger()

    fun generateToken(): ResultToken =
        ResultToken(atomicInteger.getAndIncrement())
}