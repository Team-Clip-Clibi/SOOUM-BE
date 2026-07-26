package com.clip.global.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NoBlankElementsValidator::class])
annotation class NoBlankElements(
    val message: String = "must not contain blank elements",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class NoBlankElementsValidator : ConstraintValidator<NoBlankElements, Collection<String>?> {
    override fun isValid(value: Collection<String>?, context: ConstraintValidatorContext): Boolean =
        value == null || value.all { it.isNotBlank() }
}
