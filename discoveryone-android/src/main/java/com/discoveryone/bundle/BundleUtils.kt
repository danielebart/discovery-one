package com.discoveryone.bundle

import android.os.Parcelable
import java.io.Serializable

internal fun Any?.canBeAddedInBundle(): Boolean {

    return this == null || isSupportedClass() || (this is Array<*> && isSupportedArray())
}

private fun Any.isSupportedClass(): Boolean {
    val supportedBundleClasses = listOf(
        Boolean::class,
        Byte::class,
        Char::class,
        Double::class,
        Float::class,
        Int::class,
        Long::class,
        Short::class,
        CharSequence::class,
        Parcelable::class,
        BooleanArray::class,
        ByteArray::class,
        CharArray::class,
        DoubleArray::class,
        FloatArray::class,
        IntArray::class,
        LongArray::class,
        ShortArray::class,
        Serializable::class
    )

    return supportedBundleClasses.any { clazz -> clazz.java.isAssignableFrom(this.javaClass) }
}

private fun Array<*>.isSupportedArray(): Boolean {
    val componentType = this::class.java.componentType!!
    val supportedArrayBundleClasses = listOf(
        Parcelable::class, String::class, CharSequence::class, Serializable::class
    )

    return supportedArrayBundleClasses.any { clazz ->
        clazz.java.isAssignableFrom(componentType)
    }
}