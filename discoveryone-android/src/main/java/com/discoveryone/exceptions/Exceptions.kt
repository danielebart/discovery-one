package com.discoveryone.exceptions

class NoActionRegisteredForGivenKeyException : RuntimeException()

class ActivityNotFoundOnResultRegistration :
    RuntimeException(
        """
        No activity found while registering a result. This usually means that you're trying to register 
        a result before the onCreate method of an activity of a fragment
        """.trimIndent()
    )

class FragmentNotFoundOnResultRegistration :
    RuntimeException(
        """
        No fragment found while registering a result. This usually means that you're trying to register 
        a result before the onCreate method of a fragment
        """.trimIndent()
    )

class NoActivityOnStack : RuntimeException()
