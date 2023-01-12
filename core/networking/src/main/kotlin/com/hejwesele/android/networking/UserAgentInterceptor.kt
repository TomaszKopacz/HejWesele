package com.hejwesele.android.networking

import com.hejwesele.android.appinfo.ClientName
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject

internal class UserAgentInterceptor @Inject constructor(
    private val clientName: ClientName
) : Interceptor {

    override fun intercept(chain: Chain): Response {
        val request = chain.request()
            .newBuilder()
            .header(USER_AGENT, clientName.name)
            .build()

        return chain.proceed(request)
    }

    companion object {
        private const val USER_AGENT = "User-Agent"
    }
}
