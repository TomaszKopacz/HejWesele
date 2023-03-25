package com.hejwesele.encryption

import com.hejwesele.android.osinfo.OsInfo
import java.security.MessageDigest
import android.util.Base64 as AndroidBase64
import java.util.Base64 as JavaBase64

fun ByteArray.sha256(): ByteArray = MessageDigest.getInstance("SHA-256").digest(this)

fun ByteArray.base64(osInfo: OsInfo): ByteArray =
    if (osInfo.isOOrHigher) {
        JavaBase64.getEncoder().encode(this)
    } else {
        val flags = AndroidBase64.NO_WRAP or AndroidBase64.URL_SAFE
        AndroidBase64.encode(this, flags)
    }

fun ByteArray.fromBase64(osInfo: OsInfo): ByteArray =
    if (osInfo.isOOrHigher) {
        JavaBase64.getDecoder().decode(this)
    } else {
        val flags = AndroidBase64.NO_WRAP or AndroidBase64.URL_SAFE
        AndroidBase64.decode(this, flags)
    }

fun ByteArray.string(): String = String(this, Charsets.UTF_8)

fun String.bytes(): ByteArray = toByteArray(Charsets.UTF_8)
