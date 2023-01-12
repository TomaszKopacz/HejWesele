package com.hejwesele.android.networking

import com.hejwesele.android.appinfo.AppInfo
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

internal class RetrofitApiProvider @Inject constructor(
    private val appInfo: AppInfo,
    private val interceptor: Interceptor
) : ApiProvider {

    companion object {
        private const val DEFAULT_TIMEOUT = 60L
    }

    private val moshi by lazy {
        Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
    }

    private val httpClient by lazy {
        OkHttpClient().newBuilder()
            .readTimeout(DEFAULT_TIMEOUT, SECONDS)
            .connectTimeout(DEFAULT_TIMEOUT, SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .certificatePinner(CertificatePinnerProvider.getCertificatePinner())
            .addInterceptor(interceptor)
            .apply { if (appInfo.isDebug) addInterceptor(HttpLoggingInterceptor().setLevel(BODY)) }
            .build()
    }

    override fun <T> provide(url: String, type: Class<T>): T {
        return Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(ApiResultConverterFactory)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .baseUrl(url)
            .build()
            .create(type)
    }
}
