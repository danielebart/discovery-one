package com.discoveryone.processor

import com.discoveryone.annotations.DialogFragmentRoute
import com.discoveryone.processor.routes.DialogFragmentRouteClassGenerator
import com.discoveryone.processor.safeargs.SafeArgsExtensionGenerator
import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement


internal class DialogFragmentRouteGenerationProcessingStep(
    private val env: ProcessingEnvironment
) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(
        elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>
    ): MutableSet<out Element> {
        elementsByAnnotation.values()
            .filter { it.kind == ElementKind.CLASS }
            .toMutableSet()
            .let { generateRouteClass(it) }

        return mutableSetOf()
    }

    private fun generateRouteClass(elements: Set<Element>) {
        elements.map { it as TypeElement }
            .forEach { typeElement ->
                DialogFragmentRouteClassGenerator.generateRouteClass(env, typeElement)
                SafeArgsExtensionGenerator.generateSafeArgExtensionsForDialogFragment(
                    env,
                    typeElement
                )
            }
    }

    override fun annotations(): Set<Class<out Annotation>> =
        setOf(DialogFragmentRoute::class.java)
}