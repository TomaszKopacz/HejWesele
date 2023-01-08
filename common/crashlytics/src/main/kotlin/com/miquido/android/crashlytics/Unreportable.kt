package com.miquido.android.crashlytics

import java.lang.annotation.Inherited
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

@Inherited
@Target(CLASS)
@Retention(RUNTIME)
annotation class Unreportable
