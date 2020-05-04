package com.discoveryone.processor

import com.discoveryone.annotations.ActivityRoute
import com.discoveryone.annotations.FragmentRoute
import com.discoveryone.annotations.InternalRouteArgumentMarker
import com.discoveryone.annotations.RouteArgument
import com.discoveryone.routes.AbstractRoute
import com.discoveryone.routes.GeneratedActivityRoute
import com.discoveryone.routes.GeneratedFragmentRoute
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

object RouteClassGenerator {

    fun generateActivityRouteClass(
        env: ProcessingEnvironment,
        typeElement: TypeElement
    ) {
        val annotation =
            typeElement.getAnnotation(ActivityRoute::class.java)
        val routeClassName = if (annotation.name.isNotBlank()) {
            annotation.name
        } else {
            "${typeElement.simpleName}Route"
        }
        val packageName = typeElement.asClassName().packageName
        val arguments = annotation.arguments.toList()

        val classTypeSpec = typeElement
            .commonClassTypeBuilder(routeClassName, arguments, GeneratedActivityRoute::class)
            .build()

        FileSpec.builder(packageName, routeClassName)
            .addType(classTypeSpec)
            .build()
            .writeTo(env.filer)
    }

    fun generateFragmentRouteClass(
        env: ProcessingEnvironment,
        typeElement: TypeElement
    ) {
        val annotation =
            typeElement.getAnnotation(FragmentRoute::class.java)
        val arguments = annotation.arguments.toList()
        val routeClassName = if (annotation.name.isNotBlank()) {
            annotation.name
        } else {
            "${typeElement.simpleName}Route"
        }
        val packageName = typeElement.asClassName().packageName
        val containerIdProperty =
            PropertySpec.builder("containerId", Int::class, KModifier.OVERRIDE)
                .initializer(annotation.containerId.toString())
                .build()

        val classTypeSpec = typeElement
            .commonClassTypeBuilder(routeClassName, arguments, GeneratedFragmentRoute::class)
            .addProperty(containerIdProperty)
            .build()

        FileSpec.builder(packageName, "$routeClassName.kt")
            .addType(classTypeSpec)
            .build()
            .writeTo(env.filer)
    }


    private fun TypeElement.commonClassTypeBuilder(
        routeName: String,
        arguments: List<RouteArgument>,
        routeSupertype: KClass<out AbstractRoute>
    ): TypeSpec.Builder {
        val classProperty = PropertySpec.builder(
            "clazz",
            KClass::class.asClassName().parameterizedBy(STAR),
            KModifier.OVERRIDE
        )
            .initializer("${this.asClassName().simpleName}::class")
            .build()

        return if (arguments.isEmpty()) {
            TypeSpec.objectBuilder(routeName)
                .addSuperinterface(routeSupertype)
                .addProperty(classProperty)
        } else {
            val constructor = FunSpec.constructorBuilder().run {
                arguments.forEach { arg ->
                    addParameter(arg.name, arg.getArgumentTypeName())
                }
                build()
            }
            val properties = arguments.map { arg ->
                PropertySpec.builder(arg.name, arg.getArgumentTypeName()).initializer(arg.name)
                    .addAnnotation(InternalRouteArgumentMarker::class)
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
}