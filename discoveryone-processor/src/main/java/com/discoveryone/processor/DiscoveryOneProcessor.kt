package com.discoveryone.processor

import com.google.auto.common.BasicAnnotationProcessor
import javax.lang.model.SourceVersion

class DiscoveryOneProcessor : BasicAnnotationProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun initSteps(): Iterable<ProcessingStep> {
        val env = processingEnv
        return listOf(
            ActivityRouteGenerationProcessingStep(env),
            FragmentRouteGenerationProcessingStep(env),
            DialogFragmentRouteGenerationProcessingStep(env)
        )
    }
}
