package com.sailor.processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

fun TypeName.javaToKotlinType(): TypeName {
    return when (this) {
        is ParameterizedTypeName -> {
            (rawType.javaToKotlinType() as ClassName).parameterizedBy(*(typeArguments.map { it.javaToKotlinType() }.toTypedArray()))
        }
        is WildcardTypeName -> {
            outTypes[0].javaToKotlinType()
        }
        else -> {
            val className = JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(toString()))
                ?.asSingleFqName()?.asString()
            return if (className == null) {
                this
            } else {
                ClassName.bestGuess(className)
            }
        }
    }
}