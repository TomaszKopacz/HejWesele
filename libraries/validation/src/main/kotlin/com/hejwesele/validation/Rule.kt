package com.hejwesele.validation

import com.hejwesele.validation.ValidationResult.Invalid
import com.hejwesele.validation.ValidationResult.Valid

interface Rule<T> {
    val check: (T) -> ValidationResult
    val message: String
}

class StringNotEmpty(private val error: String) : Rule<String> {

    override val check: (String) -> ValidationResult = {
        if (it.isEmpty()) Invalid(StringNotEmpty(error)) else Valid
    }

    override val message: String = error
}

class CheckboxChecked(private val error: String) : Rule<Boolean> {
    override val check: (Boolean) -> ValidationResult = { checked ->
        if (checked) Valid else Invalid(CheckboxChecked(error))
    }

    override val message: String = error
}
