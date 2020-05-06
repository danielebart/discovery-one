package com.discoveryone.processor.routes

import com.discoveryone.annotations.FragmentRoute
import com.discoveryone.processor.routes.CommonClassTypeBuilder.createClassProperty
import com.discoveryone.processor.routes.CommonClassTypeBuilder.createDataClassBuilder
import com.discoveryone.routes.GeneratedFragmentRoute
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

internal object FragmentRouteClassGenerator {

    fun generateRouteClass(
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

        val classTypeSpec = if (arguments.isEmpty()) {
            TypeSpec.objectBuilder(routeClassName)
                .addSuperinterface(GeneratedFragmentRoute::class)
                .addProperty(createClassProperty(typeElement))
                .addProperty(containerIdProperty)
                .build()
        } else {
            createDataClassBuilder(
                typeElement = typeElement,
                routeName = routeClassName,
                arguments = arguments,
                routeSupertype = GeneratedFragmentRoute::class
            )
                .addProperty(containerIdProperty)
                .build()
        }

        FileSpec.builder(packageName, routeClassName)
            .addType(classTypeSpec)
            .build()
            .writeTo(env.filer)
    }

}