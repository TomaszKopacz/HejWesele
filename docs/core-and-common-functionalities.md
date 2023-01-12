# Core and common functionalities

Android Template comes with a set of modules/implementations that should cover basic needs of all standard projects.
Depending on your project's needs you can use them as they are, modify them, or remove some or all of them.

[[_TOC_]]

## App name and version, OS version

[appinfo](../core/appinfo) module provides an interface to gather information about app name, version number, and version code. 
It can be used in any module that needs it. Its implementation is placed inside the app module based on application data from 
resources and build config.

[osinfo](../core/osinfo) module provides an interface to check the version of the operating system running the application. This can 
be used if you need to call an API that is available from the Android version that is higher than minSdk; using the wrapper allows to 
mock OS version in tests and verify if the API is called on the given OS version or not.

## Test environments configuration

[config](../core/config) is set of modules which provides a possibility to switch between backend environments configuration for debug and qa build variants.

Switcher usage:
1. Attach a switcher entry Composable `ConfigurationSwitcher#Switcher` somewhere in your application
1. Ensure your `Application` class skips initialization when `ConfigurationSwitcher#isSwitching()` returns true

You can add other properties to `Configuration` depending on project needs, but don't forget to update [config.test](../core/config/test) to provide a modification possibilities for new properties.

## Dependency injection (Hilt)

Dependency injection is implemented by Google Hilt which provides wide support for Android application.
Hilt is built on top of Dagger library for reducing boilerplate code related to configuration.
Library provides set of predefined components and scopes which are bind to Android lifecycles.

More information related to Hilt components are in [documentation](https://dagger.dev/hilt/components)

Hilt provides support for following Android entry points: Activity, Fragment, View, Service, BroadcastReceiver.
To be able to use injections in this classes annotate it with `@AndroidEntryPoint` annotation e.g.:
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity()
```
More information about entry points are in [documentation](https://dagger.dev/hilt/android-entry-point)

Hilt automatically detects modules in the source code and assigns it to the DI graph based on `@InstallIn` annotation.
To be able to add new module you have to specify component by adding it to `@InstallIn` annotation
and set binding type (`@Binds` or `@Provides`) e.g.:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
internal interface NewModule {

    @Binds
    fun bindsFoo(impl: FooImpl): Foo
}
```
Above module will be added to `SingletonComponent` component, more information are in [documentation](https://dagger.dev/hilt/modules)

You can use Hilt injection in instrumented unit tests. See [documentation](https://dagger.dev/hilt/testing) for more information.

## Networking (OkHttp + Retrofit + EitherNet)

[networking](../core/networking) core module provides basic networking stack based on [OkHttp](https://square.github.io/okhttp/) and [Retrofit](https://square.github.io/retrofit/).

The implementation uses Moshi for JSON parsing. Coroutines are supported out of the box.
To better model network responses, [EitherNet](https://github.com/slackhq/EitherNet) is used.

To create an implementation of the API interface, inject and use `ApiProvider`. Here is an example of providing API implementation from Dagger/Hilt module and using it somewhere in the code,
along with EitherNet's `ApiResult` usage:
```kotlin
interface Api {
    @DecodeErrorBody
    @GET("/endpoint")
    suspend fun getEndpoint(): ApiResult<Response, Error>
}

// In the Dagger/Hilt module
@Provides
fun provideApi(apiProvider: ApiProvider): Api = apiProvider.provide("https://www.example.com/api/")

// Somewhere in the code
class Test @Inject constructor(private val api: Api) {

    suspend fun test() {
        logcat {
            when (val apiResult = api.getEithernetEndpoint()) {
                is Success -> "Success! Response: ${apiResult.value}"
                is NetworkFailure -> "No internet connection!"
                is HttpFailure -> "Http failure, code: ${apiResult.code}, body: ${apiResult.error}"
                is ApiFailure, is UnknownFailure -> "Error with parsing body or unknown error: ${apiResult.error}"
            }
        }
    }
}
```

[Application module](../app) contains [network security configuration](https://developer.android.com/training/articles/security-config).
For debug and qa builds, cleartext traffic is permitted, and user-added CA certificates are trusted (to enable using e.g. Charles).
For release builds, cleartext traffic is forbidden, and only system CA certificates are trusted.

To configure SSL certificate pinning, update pinning information (hostname, pin hash) in
[CertificatePinnerProvider](../core/networking/src/main/kotlin/com/hejwesele/android/networking/CertificatePinnerProvider.kt).

To change the content of the User-Agent header that your application sends, see
[UserAgentInterceptor](../core/networking/src/main/kotlin/com/hejwesele/android/networking/UserAgentInterceptor.kt).
At the same time it's the example of how to add OkHttp interceptor that extends the request with additional header(s).

## JSON (de)serialization (Moshi)

[json](../core/json) core module provides JSON serialization and deserialization functionality, based on [Moshi](https://github.com/square/moshi).

You can inject `Json` interface to your code and simply use `toJson`/`fromJson` synchronous methods and inline extension methods.

All data classes you want to serialize/deserialize using Moshi need to be annotated with `@JsonClass(generateAdapter = true)`.
To process them and generate adapters, add `dev.zacsweers.moshix` Gradle plugin to your module.

By using `dev.zacsweers.moshix` plugin you can also take advantage of [MoshiX Sealed](https://github.com/ZacSweers/MoshiX/tree/main/moshi-sealed/).
This can be especially useful if the backend of your application returns different response structure in some cases (e.g. success vs error).

To generate Moshi adapters for sealed classes that use MoshiX Sealed generator, opt-in to enable moshi-sealed:
```groovy
moshi {
    enableSealed.set(true)
}
```

## Encrypted local database (SqlDelight + SqlCipher + AndroidX Security-Crypto)

[database](../core/database) module provides local SQL database functionality, based on [SqlDelight](https://cashapp.github.io/sqldelight/android_sqlite/).
[SqlCipher](https://github.com/sqlcipher/android-database-sqlcipher) is used to encrypt the database. Encryption passphrase is generated using `SecureRandom`
instance and securely stored in file encrypted by [AndroidX Security-Crypto](https://developer.android.com/topic/security/data).

SQL statements are stored under [src/main/sqldelight](../core/database/src/main/sqldelight) directory. SqlDelight generates type-safe API based on
`.sq` files. Coroutines are supported by using `asFlow()` extension method. Refer to the [documentation](https://cashapp.github.io/sqldelight/android_sqlite/)
for more information about using SqlDelight.

[DatabaseModule](../core/database/src/main/kotlin/com/hejwesele/android/database/di/DatabaseModule.kt) provides `Database` interface implementation.
By default it uses SqlCipher-based provider, but if you don't need to have your database encrypted, you can replace `SqlDelightSqlCipherDatabaseProvider`
with `SqlDelightDatabaseProvider` and remove SqlCipher and AndroidX Security-Crypto usage.

**IMPORTANT:** encrypted database initialization should be invoked on the non-UI thread - it operates on the file system to store/retrieve passphrase.
Since `Database` interface is provided using Dagger, whenever you need to access the database, you need to [lazily inject](https://dagger.dev/dev-guide/#lazy-injections)
`Database` instance and call `get()` method on the `Lazy<Database>` instance within non-UI thread.

## Plain local key-value storage (AndroidX DataStore)
DataStore preferences is a data storage solution that allows you to store key-value pairs.
To use preferences in your app you need to attach [preferences](../core/preferences) module to your desired module.

*Features:*
- Read/write values `get()`/`put()`
- Transactional updates `update()`
- Observer value changes `observer()`. Returned `Flow` emits `Optional<T>` values when start collecting and on every write to data store.

Supported types od data which can be store is limited to `Int`, `Long`, `Float`, `Double`, `Boolean`, `Strings`, `Set<Strings>`.

Keep in mind that every `Prefences` with unique name should be provided as single instance to handle concurrent modifications.

## Schedule work (AndroidX WorkManager)

WorkManager is an API that makes it easy to schedule reliable, asynchronous tasks that are expected to run even if the app exits
or the device restarts.

Current features:
- schedule one-time work,
- schedule periodic work,
- find work info by TAG.

Example of usage:
```kotlin
 @Inject
 lateinit var workManager: WorkManager
 
 val enqueueWork = workManager
    .scheduleOneTimeWorkAsync(...)
    .await()
```

Annotate your `Worker` implementation with `@HiltWorker` to make it possible to inject dependencies to it. Annotate the constructor
with `@AssistedInject `to perform assisted injection of `Context` and `WorkerParameters`. You can use only `@Singleton` or
unscoped bindings in `Worker` objects.

```kotlin
@HiltWorker
class ExampleWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    workerDependency: WorkerDependency
) : Worker(appContext, workerParams) { ... }
```

To process `@HiltWorker`-annotated classes, attach `kapt libs.androidx.hilt.compiler` dependency to the module.

## Custom Tabs

Custom Tabs is a convenient way of launching web content within the Android application.

[Custom Tabs module](../common/customtabs), built using AndroidX Browser library, exposes simple API for launching Custom Tabs.
To use it, inject `CustomTabs` interface (note: you need to be in `ActivityComponent` scope), or, in Composable, obtain the instance 
via `LocalCustomTabs` Composition Local. Then simply call (from the main thread) `launch` method.

Note that this functionality uses Chrome (or other installed browser that supports Custom Tabs).
If there's no browser that supports Custom Tabs available, simple `ACTION_VIEW` Intent will be launched.

You can customize colors, animations and other features of Custom Tabs by changing the method providing `CustomTabsIntent` in
[AndroidXCustomTabs class](../common/customtabs/src/main/kotlin/com/hejwesele/android/customtabs/AndroidXCustomTabs.kt)
(`buildCustomTabsIntent` method).

## DateTime provider and formatter (kotlinx-datetime)

[DateTime](../core/datetime) core module provides Clock interface for retrieving current datetime and time zone.
To get current `Instant` call `now()` and to get `TimeZone` call `zone()` on a `Clock` object.
If `LocalDateTime` or `LocalDate` is needed instead of `Instant` use `localDateTime()` and `localDate()` methods on a `Clock` object.

[DateTimeFormatter](../common/datetimeformatter) common module provides functionality of formatting datetime objects of types: `Instant`, `LocalDateTime` and `LocalDate`.
Use Hilt to inject an object of type `DateTimeFormatter` and call `format(dateTime, format)` on it with dateTime, you want to format, and an instance of the `Format` sealed class.

Use provided `Format`, `TimePattern` and `DatePattern` subclasses or create your own subclasses in the [Format](../common/datetimeformatter/src/main/kotlin/com/hejwesele/android/datetimeformatter/Format.kt) file.

`Format` class includes parameters:
* `closeDateRange: Int?`: range in which a given date is treated as 'close'. Pass this value to `Format` subclasses that accepts it to achieve formats like: "yesterday", "in 10 days"
* `time: TimePattern?`: used to format time
* `currentYear: DatePattern?`: used to format date if date to be formatted is in current year
* `otherYear: DatePattern?`: used to format date if date to be formatted was in previous or next years.
* `dateTimeSeparetor: String`: used to separate date from time if both `DatePattern`s and `TimePattern` are provided.

## Square Logcat

[Square Logcat](https://github.com/square/logcat) simplifies logging info to Logcat.

Logging all priorities in debuggable builds is already set up in the [Application class](../app/src/main/kotlin/com/hejwesele/androidtemplate/App.kt).
No logs will be printed in other build types.
To use Square Logcat in any module, add `implementation libs.logcat` dependency and then use `logcat()` function.
