package com.sailor.processor

import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import com.sailor.destination.ActivityDestination
import com.sailor.destination.ActivityNavigationDestination
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

internal class ActivityDestinationGenerationProcessingStep(
    private val env: ProcessingEnvironment
) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(
        elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>?
    ): MutableSet<out Element> {
        elementsByAnnotation?.values()
            ?.filter { it.kind == ElementKind.CLASS }
            ?.toMutableSet()
            ?.let { generateDestinationClass(it) }

        return mutableSetOf()
    }

    private fun generateDestinationClass(elements: Set<Element>) {
        elements.map { it as TypeElement }
            .forEach { typeElement ->
                val annotation =
                    typeElement.getAnnotation(ActivityNavigationDestination::class.java)
                val destinationClassName = annotation.name
                val packageName = typeElement.asClassName().packageName
                val nameProperty = PropertySpec.builder("name", String::class, KModifier.OVERRIDE)
                    .initializer("\"${annotation.name}\"")
                    .build()
                val classProperty = PropertySpec.builder(
                    "clazz",
                    KClass::class.asClassName().parameterizedBy(STAR),
                    KModifier.OVERRIDE
                )
                    .initializer("${typeElement.asClassName().simpleName}::class")
                    .build()
                val classTypeSpec = TypeSpec.objectBuilder(destinationClassName)
                    .addSuperinterface(ActivityDestination::class)
                    .addProperties(listOf(nameProperty, classProperty))
                    .build()
                FileSpec.builder(packageName, "$destinationClassName.kt")
                    .addType(classTypeSpec)
                    .build()
                    .writeTo(env.filer)
            }
    }

    override fun annotations(): Set<Class<out Annotation>> {
        return setOf(ActivityNavigationDestination::class.java)
    }
}