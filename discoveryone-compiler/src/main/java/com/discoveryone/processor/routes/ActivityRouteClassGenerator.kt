package com.discoveryone.processor.routes

import com.discoveryone.annotations.ActivityRoute
import com.discoveryone.routes.GeneratedActivityRoute
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

internal object ActivityRouteClassGenerator {

    fun generateRouteClass(
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

        val classTypeSpec = if (arguments.isEmpty()) {
            TypeSpec.objectBuilder(routeClassName)
                .addSuperinterface(GeneratedActivityRoute::class)
                .addProperty(CommonClassTypeBuilder.createClassProperty(typeElement))
                .build()
        } else {
            CommonClassTypeBuilder.createDataClassBuilder(
                typeElement = typeElement,
                routeName = routeClassName,
                arguments = arguments,
                routeSupertype = GeneratedActivityRoute::class
            ).build()
        }

        FileSpec.builder(packageName, routeClassName)
            .addType(classTypeSpec)
            .build()
            .writeTo(env.filer)
    }
}