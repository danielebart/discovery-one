package com.discoveryone.processor.routes

import com.discoveryone.annotations.RouteArgument
import com.discoveryone.processor.extensions.getArgumentTypeName
import com.discoveryone.routes.AbstractRoute
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

object CommonClassTypeBuilder {

    fun createClassProperty(typeElement: TypeElement): PropertySpec =
        PropertySpec.builder(
            "clazz",
            KClass::class.asClassName().parameterizedBy(STAR),
            KModifier.OVERRIDE
        )
            .initializer("${typeElement.asClassName().simpleName}::class")
            .build()

    fun createDataClassBuilder(
        typeElement: TypeElement,
        routeName: String,
        arguments: List<RouteArgument>,
        routeSupertype: KClass<out AbstractRoute>
    ): TypeSpec.Builder {
        val classProperty = createClassProperty(typeElement)
        val constructor = FunSpec.constructorBuilder().run {
            arguments.forEach { arg ->
                addParameter(arg.name, arg.getArgumentTypeName())
            }
            build()
        }
        val properties = arguments.map { arg ->
            PropertySpec.builder(arg.name, arg.getArgumentTypeName())
                .initializer(arg.name)
                .build()
        }
        return TypeSpec.classBuilder(routeName)
            .addModifiers(KModifier.DATA)
            .addProperties(properties)
            .addSuperinterface(routeSupertype)
            .addProperty(classProperty)
            .primaryConstructor(constructor)
    }
}
