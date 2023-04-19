package com.hejwesele.validation

import com.hejwesele.validation.ValidationResult.Valid

class Validator<T> (private vararg val rules: Rule<T>) {

    fun validate(data: T): ValidationResult {
        return if (rules.isEmpty()) {
            Valid
        } else {
            rules.fold(
                initial = Valid as ValidationResult,
                operation = { result, rule -> result + rule.check(data) }
            )
        }
    }
}
