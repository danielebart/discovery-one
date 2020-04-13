package com.discoveryone.processor

import com.discoveryone.destination.FragmentNavigationDestination
import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

internal class FragmentDestinationGenerationProcessingStep(
    private val env: ProcessingEnvironment
) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(
        elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>
    ): MutableSet<out Element> {
        elementsByAnnotation.values()
            .filter { it.kind == ElementKind.CLASS }
            .toMutableSet()
            .let { generateDestinationClass(it) }

        return mutableSetOf()
    }

    private fun generateDestinationClass(elements: Set<Element>) {
        elements.map { it as TypeElement }
            .forEach { typeElement ->
                DestinationClassGenerator.generateFragmentDestinationClass(env, typeElement)
            }
    }

    override fun annotations(): Set<Class<out Annotation>> {
        return setOf(FragmentNavigationDestination::class.java)
    }
}