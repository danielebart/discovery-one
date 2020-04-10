package com.sailor.processor

import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import com.sailor.destination.Destination
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

internal class DestinationGenerationProcessingStep(
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
                val destinationClassName = typeElement.getAnnotation(Destination::class.java).name
                val packageName = typeElement.asClassName().packageName
                FileSpec.builder(packageName, "$destinationClassName.kt")
                    .addType(TypeSpec.objectBuilder(destinationClassName).build())
                    .build()
                    .writeTo(env.filer)
            }
    }

    override fun annotations(): Set<Class<out Annotation>> {
        return setOf(Destination::class.java)
    }
}