package com.hejwesele.validation

sealed class ValidationResult {
    object Valid : ValidationResult()
    class Invalid(vararg val rules: Rule<*>) : ValidationResult()

    val isValid: Boolean
        get() = this is Valid

    val isInvalid: Boolean
        get() = this is Invalid

    operator fun plus(other: ValidationResult): ValidationResult {
        return if (this is Valid && other is Valid) {
            Valid
        } else {
            val formerRules = (this as? Invalid)?.rules?.toList() ?: emptyList()
            val latterRules = (other as? Invalid)?.rules?.toList() ?: emptyList()
            val rules = formerRules + latterRules

            require(rules.isNotEmpty())

            Invalid(*rules.toTypedArray())
        }
    }
}
