package com.discoveryone.processor

import com.discoveryone.annotations.FragmentRoute
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import kotlinx.metadata.ClassName
import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import kotlinx.metadata.KmClassVisitor
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

object SafeArgsExtensionGenerator {

    fun generateSafeArgExtensionsForFragment(
        env: ProcessingEnvironment,
        typeElement: TypeElement
    ) {
        val packageName = typeElement.asClassName().packageName
        val annotation =
            typeElement.getAnnotation(FragmentRoute::class.java)
        val arguments = annotation.arguments.toList()

        if (arguments.isEmpty()) {
            return
        }

        arguments.fold(
            FileSpec.builder(packageName, "${typeElement.simpleName}Extensions.kt")
        ) { builder, routeArgument ->
            builder.apply {
                generateCommonSafeArgExtensions(
                    receiverTypeElement = typeElement,
                    argumentName = routeArgument.name,
                    argumentTypeName = routeArgument.getArgumentTypeName(),
                    extensionContent = "return requireArguments().get(\"${routeArgument.name}\") as %T"
                )
            }
        }.build().writeTo(env.filer)
    }

    fun generateSafeArgExtensionsForActivity(
        env: ProcessingEnvironment,
        typeElement: TypeElement
    ) {
        val packageName = typeElement.asClassName().packageName
        val annotation =
            typeElement.getAnnotation(FragmentRoute::class.java)
        val arguments = annotation.arguments.toList()

        if (arguments.isEmpty()) {
            return
        }

        arguments.fold(
            FileSpec.builder(packageName, "${typeElement.simpleName}Extensions.kt")
        ) { builder, routeArgument ->
            builder.apply {
                generateCommonSafeArgExtensions(
                    receiverTypeElement = typeElement,
                    argumentName = routeArgument.name,
                    argumentTypeName = routeArgument.getArgumentTypeName(),
                    extensionContent = "return intent.extras!!.get(\"${routeArgument.name}\") as %T"
                )
            }
        }.build().writeTo(env.filer)
    }

    private fun FileSpec.Builder.generateCommonSafeArgExtensions(
        receiverTypeElement: TypeElement,
        argumentName: String,
        argumentTypeName: TypeName,
        extensionContent: String
    ) {
        val getterFunSpec = FunSpec.builder("get()")
            .receiver(receiverTypeElement.asClassName())
            .addStatement(extensionContent, argumentTypeName)
            .build()
        addProperty(
            PropertySpec.builder(argumentName, argumentTypeName)
                .receiver(receiverTypeElement.asClassName())
                .addModifiers(*receiverTypeElement.getKModifiers().toTypedArray())
                .getter(getterFunSpec)
                .build()
        )
    }

    private fun TypeElement.getKModifiers(): List<KModifier> =
        if (isInternal()) listOf(KModifier.INTERNAL) else emptyList()

    private fun TypeElement.isInternal(): Boolean {
        var isInternal = false
        val metadataAnnotation = getAnnotation(Metadata::class.java)
        val header = KotlinClassHeader(
            kind = metadataAnnotation.kind,
            data1 = metadataAnnotation.data1,
            data2 = metadataAnnotation.data2,
            bytecodeVersion = metadataAnnotation.bytecodeVersion,
            packageName = metadataAnnotation.packageName,
            extraString = metadataAnnotation.extraString,
            extraInt = metadataAnnotation.extraInt,
            metadataVersion = metadataAnnotation.metadataVersion
        )

        val metadata = KotlinClassMetadata.read(header) as KotlinClassMetadata.Class
        metadata.accept(object : KmClassVisitor() {
            override fun visit(flags: Flags, name: ClassName) {
                isInternal = Flag.IS_INTERNAL(flags)
            }
        })

        return isInternal
    }
}