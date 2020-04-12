package com.discoveryone.processor

import com.discoveryone.destination.ActivityDestination
import com.discoveryone.destination.ActivityNavigationDestination
import com.discoveryone.destination.DestinationArgument
import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
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
                val arguments = annotation.arguments.toList()
                val destinationClassName = annotation.name
                val packageName = typeElement.asClassName().packageName
                val classTypeSpec =
                    typeElement.buildClassType(annotation.name, arguments)

                FileSpec.builder(packageName, "$destinationClassName.kt")
                    .addType(classTypeSpec)
                    .build()
                    .writeTo(env.filer)
            }
    }

    private fun TypeElement.buildClassType(
        destinationName: String,
        arguments: List<DestinationArgument>
    ): TypeSpec {
        val nameProperty = PropertySpec.builder("name", String::class, KModifier.OVERRIDE)
            .initializer("\"${destinationName}\"")
            .build()
        val classProperty = PropertySpec.builder(
            "clazz",
            KClass::class.asClassName().parameterizedBy(STAR),
            KModifier.OVERRIDE
        )
            .initializer("${this.asClassName().simpleName}::class")
            .build()

        return if (arguments.isEmpty()) {
            TypeSpec.objectBuilder(destinationName)
                .addSuperinterface(ActivityDestination::class)
                .addProperties(listOf(nameProperty, classProperty))
                .build()
        } else {
            val constructor = FunSpec.constructorBuilder().run {
                arguments.forEach { arg -> addParameter(arg.name, arg.getArgumentTypeName()) }
                build()
            }
            val properties = arguments.map { arg ->
                PropertySpec.builder(arg.name, arg.getArgumentTypeName()).initializer(arg.name)
                    .build()
            }
            TypeSpec.classBuilder(destinationName)
                .addModifiers(KModifier.DATA)
                .addProperties(properties)
                .addSuperinterface(ActivityDestination::class)
                .addProperties(listOf(nameProperty, classProperty))
                .primaryConstructor(constructor)
                .build()
        }
    }

    private fun DestinationArgument.getArgumentTypeName(): TypeName {
        return try {
            type.java.asTypeName().javaToKotlinType()
        } catch (mte: MirroredTypeException) {
            mte.typeMirror.asTypeName().javaToKotlinType()
        }
    }

    override fun annotations(): Set<Class<out Annotation>> =
        setOf(ActivityNavigationDestination::class.java)
}