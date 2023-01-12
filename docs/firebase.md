# Firebase

Firebase is a platform which has build, release & monitor and engage features.

[[_TOC_]]

## Setting up Firebase

To use Firebase platform in your project you need to:
- Update [google-services file](../app/google-services.json) accordingly to your project requirements.
- In your Firebase project you need to add 3 apps [.debug, .qa, and one without suffix which will be release version].

## Firebase Analytics

Firebase Analytics is a tool which allows you to track app events data in Firebase.
To use analytics in your app you need to attach [analytics](../common/analytics) module to your desired module.

*Features:*
- You can turn on/off Firebase analytics just by calling `enable()`/`disable()` methods on Analytics object.
  Make sure you are starting user data sending after they accept analytics data sending agreements.
  At the same time `analytics.enable()` doesn't have to be used in `App` class as in the sample app.
- You can log analytics event with option parameters using `log()` method.
- You can also log user related constant data (for example `declined notifications` boolean marker) using `setUserProperty()` method.
- If you are using method above make sure to use `setUserId` method with unique id, to identify user in the Firebase database.
- In order to track screen entrances manually use `logScreenStarted` method. For Firebase Analytics it complies with a standard defined by Firebase for such events.

Keep in mind that Firebase Analytics may send events with delay of up to 30 minutes, if app is running.
Use DebugView in Firebase console to test events in convenient way and receive them faster

Useful link for this project analytics view:
[Firebase Analytics View](https://console.firebase.google.com/project/mq-android-template/analytics/app/android:com.miquido.androidtemplate.debug/overview/)

You can add some additional analytics libraries just by adding your implementation to [analytics package](../common/analytics/src/main/kotlin/com/hejwesele/android/analytics)
and implementing Analytics interface in your wrapper class.

## Firebase Crashlytics

Firebase Crashlytics is a tool which allows you to report crash data to Firebase.
To use crashlytics in your app you need to attach [crashlytics](../common/crashlytics) module to your desired module.

*Features:*
- You can turn on/off Firebase crashlytics just by calling `enable()`/`disable()` methods on FirebaseCrashlytics object.
- To log additional message to crash report call `log()` method.
- To log not fatal exception to Firebase call `reportException()` method.

Keep in mind that exceptions with `@Unreportable` annotations won't be logged into Firebase Crashlytics.

You can add some additional crash reporting libraries just by adding your implementation to [crashlytics package](../common/crashlytics/src/main/kotlin/com/hejwesele/android/crashlytics)
and implementing Crashlytics interface in your wrapper class.

## Firebase Performance Monitoring

Firebase Performance Monitoring is a service that collects insights about application and networking performance.

Performance monitoring is by default attached to the template's project. No additional setup is required.
Collecting data is disabled for debug builds.

## Firebase Remote Config

Firebase Remote Config provides remote key-value configuration service.

To use Firebase Remote Config, first set it up in the [Firebase console](https://console.firebase.google.com/) by adding some entries.
You can access Remote Config values by injecting `RemoteConfig` interface from [remoteconfig module](../common/remoteconfig).
All the initialization code is already called in the [default Application class](../app/src/main/kotlin/com/hejwesele/androidtemplate/App.kt).

Remote Config values will be fetched when the process starts and each time the first Activity of the already running process reaches
*Started* state. In other words, Remote Config values are fetched when app comes to the foreground.

To not overuse nullability, prefer using `get{String,Long,Boolean}(key, default)` methods over `...OrNull()` ones. This will enforce
you to supply non-null default value each time you use Remote Config, and will make your code cleaner.

## Firebase Cloud Messaging

Firebase Cloud Messaging allows to receive so-called "push" messages in the application.

You can initialize push messaging by retrieving FCM registration token using `getToken` method from `Messaging` interface provided by
[messaging module](../common/messaging). The typical flow is to pass the registration token to the backend service and/or subscribe to the topic.
You can also store the token using e.g. [preferences](../core/preferences) and retrieve it when you need it.

To watch for token changes, implement `TokenHandler` interface. To handle incoming push messages, implement `MessageHandler` interface.
```kotlin
internal class MyTokenHandler @Inject constructor() : TokenHandler {
    override fun onNewToken(token: String) {
        logcat { "Token received: $token" }
    }
}

internal class MyMessageHandler @Inject constructor() : MessageHandler {
    override fun onMessageReceived(data: Map<String, String>) {
       logcat { "Received message: $data" }
    }
}
```

Use Dagger multibindings (`@IntoSet`) to make them available for the `MessagingService`, which handles incoming communication:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
internal interface MyMessagingModule {

    @Binds
    @IntoSet
    @Singleton
    fun bindMyTokenHandler(handler: MyTokenHandler): TokenHandler

    @Binds
    @IntoSet
    @Singleton
    fun bindMyMessageHandler(handler: MyMessageHandler): MessageHandler
}
```
