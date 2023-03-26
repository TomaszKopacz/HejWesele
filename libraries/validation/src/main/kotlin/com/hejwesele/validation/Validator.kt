package com.hejwesele.validation

import com.hejwesele.validation.ValidationResult.Valid

class Validator<T> (private vararg val rules: Rule<T>) {

    fun validate(data: T): ValidationResult<T> {
        return if (rules.isEmpty()) {
            Valid()
        } else {
            rules.fold(
                initial = Valid<T>() as ValidationResult<T>,
                operation = { result, rule -> result + rule.check(data) }
            )
        }
    }
}
