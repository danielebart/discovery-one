package com.discoveryone.processor

import com.discoveryone.annotations.RouteArgument
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.type.MirroredTypeException

fun RouteArgument.getArgumentTypeName(): TypeName {
    return try {
        type.java.asTypeName().javaToKotlinType()
    } catch (mte: MirroredTypeException) {
        mte.typeMirror.asTypeName().javaToKotlinType()
    }
}