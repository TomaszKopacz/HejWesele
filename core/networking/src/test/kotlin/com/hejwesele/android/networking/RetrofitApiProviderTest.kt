package com.hejwesele.android.networking

import com.google.common.truth.Truth.assertThat
import com.hejwesele.android.appinfo.AppInfo
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.slack.eithernet.ApiResult
import com.slack.eithernet.ApiResult.Failure.HttpFailure
import com.slack.eithernet.ApiResult.Success
import com.slack.eithernet.DecodeErrorBody
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.http.GET

class RetrofitApiProviderTest {

    private val appInfo: AppInfo = mock {
        on { isDebug }.thenReturn(true)
    }

    private val doNothingInterceptor: Interceptor = mock {
        on { intercept(any()) }.thenAnswer {
            val chain = (it.arguments[0] as Interceptor.Chain)
            chain.proceed(chain.request())
        }
    }

    private val server = MockWebServer()

    @JsonClass(generateAdapter = true)
    data class Response(val testResponseString: String)

    @JsonClass(generateAdapter = true)
    data class Error(val testErrorString: String)

    private interface Api {
        @GET("/endpoint")
        suspend fun getEndpoint(): Response

        @DecodeErrorBody
        @GET("/endpoint/eithernet")
        suspend fun getEithernetEndpoint(): ApiResult<Response, Error>
    }

    @Before
    fun setUp() {
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `test simple api call`() {
        RetrofitApiProvider(appInfo, doNothingInterceptor).testApiCall()
    }

    @Test
    fun `test simple api call with debug mode off`() {
        val appInfo = object : AppInfo {
            override val appName: String
                get() = "Android Template"
            override val versionName: String
                get() = "1.0.0"
            override val versionCode: Int
                get() = 1
            override val isDebug: Boolean
                get() = false
        }
        RetrofitApiProvider(appInfo, doNothingInterceptor).testApiCall()
    }

    @Test
    fun `test eithernet success api call`() {
        val body = "{\"testResponseString\":\"success\"}"
        val mockResponse = MockResponse().setBody(body)
        val expected = Response(testResponseString = "success")

        RetrofitApiProvider(appInfo, doNothingInterceptor)
            .testEithernetApiCall(mockResponse) { apiResult: Success<Response> ->
                assertThat(apiResult.value).isEqualTo(expected)
            }
    }

    @Test
    fun `test eithernet error api call`() {
        val body = "{\"testErrorString\":\"error\"}"
        val mockResponse = MockResponse().setResponseCode(500).setBody(body)
        val expected = Error(testErrorString = "error")

        RetrofitApiProvider(appInfo, doNothingInterceptor)
            .testEithernetApiCall(mockResponse) { apiResult: HttpFailure<Error> ->
                assertThat(apiResult.code).isEqualTo(500)
                assertThat(apiResult.error).isEqualTo(expected)
            }
    }

    private fun ApiProvider.testApiCall() {
        val api: Api = provide(server.url("/").toString())
        val body = "{\"testResponseString\":\"success\"}"
        val expected = Response(testResponseString = "success")
        val actual: Response

        server.enqueue(MockResponse().setBody(body))
        runBlocking { actual = api.getEndpoint() }
        assertThat(actual).isEqualTo(expected)
    }

    @Suppress("unchecked_cast")
    private fun <T : ApiResult<Response, Error>> ApiProvider.testEithernetApiCall(
        mockResponse: MockResponse,
        assertion: (T) -> Unit
    ) {
        val api: Api = provide(server.url("/").toString())
        val actual: ApiResult<Response, Error>

        server.enqueue(mockResponse)
        runBlocking { actual = api.getEithernetEndpoint() }
        assertion.invoke(actual as T)
    }
}
