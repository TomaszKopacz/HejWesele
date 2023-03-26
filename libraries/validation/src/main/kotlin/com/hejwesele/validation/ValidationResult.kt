package com.hejwesele.validation

sealed class ValidationResult<T> {
    class Valid<T> : ValidationResult<T>()
    class Invalid<T>(vararg val rules: Rule<T>) : ValidationResult<T>()

    val isValid: Boolean
        get() = this is Valid

    val isInvalid: Boolean
        get() = this is Invalid

    operator fun plus(other: ValidationResult<T>): ValidationResult<T> {
        return if (this is Valid && other is Valid) {
            Valid()
        } else {
            val formerRules = (this as? Invalid)?.rules?.toList() ?: emptyList()
            val latterRules = (other as? Invalid)?.rules?.toList() ?: emptyList()
            val rules = formerRules + latterRules

            require(rules.isNotEmpty())

            Invalid(*rules.toTypedArray())
        }
    }
}
