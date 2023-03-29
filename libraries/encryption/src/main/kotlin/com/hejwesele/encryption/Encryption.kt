package com.hejwesele.encryption

import java.security.MessageDigest
import java.util.Base64 as JavaBase64

fun ByteArray.sha256(): ByteArray = MessageDigest.getInstance("SHA-256").digest(this)

fun ByteArray.base64(): ByteArray = JavaBase64.getEncoder().encode(this)

fun ByteArray.fromBase64(): ByteArray = JavaBase64.getDecoder().decode(this)

fun ByteArray.string(): String = String(this, Charsets.UTF_8)

fun String.bytes(): ByteArray = toByteArray(Charsets.UTF_8)
