package com.hejwesele.validation

import com.hejwesele.validation.ValidationResult.Invalid
import com.hejwesele.validation.ValidationResult.Valid

interface Rule<T> {
    val check: (T) -> ValidationResult<T>
    val message: String
}

class StringNotEmpty(private val error: String) : Rule<String> {

    override val check: (String) -> ValidationResult<String> = {
        if (it.isEmpty()) Invalid(StringNotEmpty(error)) else Valid()
    }

    override val message: String = error
}
