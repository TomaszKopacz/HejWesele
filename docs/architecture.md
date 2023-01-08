# Architecture

Android Template comes with suggested app architecture setup, which is highly modular and based on Jetpack Compose, MVVM 
(AndroidX ViewModel), Coroutines and AndroidX Navigation.

>>>
Since the template's architecture is currently under heavy development, some significant architecture parts may be missing.  
However, the setup should be stable at any time and it always can be extended with commonly used patterns,
or replaced with other solution.
>>>

[[_TOC_]]

## Single Activity

Application module contains [single main Activity](../app/src/main/kotlin/com/miquido/androidtemplate/MainActivity.kt),
which is the entry point of the app, and serves as a minimal container for all Composables.
This means that in most of the cases you won't need to add any other Activity class to your project.

## Navigation

All navigation features in the Android Template are based on the Navigation architecture component and
[its support for Jetpack Compose](https://developer.android.com/jetpack/compose/navigation).

### Global navigation

Global navigation is handled by the mechanism implemented in the [navigation module](../common/navigation).
By using it, we can navigate between features (feature modules) without having them to depend on each other.

>>>
Please note that this mechanism should only be used for global navigation. Navigation *within* features
should be internal - you shouldn't be able to navigate to the feature's inner screens from any other module.
See "Nested navigation" section for more information.
>>>

Navigation module introduces following concepts:
* [Direction](../common/navigation/src/main/kotlin/com/miquido/android/navigation/Direction.kt),
  which is a definition of where the user can navigate. It defines route (with navigation argument placeholders, if any)
  and its arguments.
* [Destination](../common/navigation/src/main/kotlin/com/miquido/android/navigation/Destination.kt),
  which contains an actual route where the user can navigate (route with filled navigation arguments, built upon `Direction`)
  and additional navigation options (like `popUpTo`).

Globally available directions and destinations are kept in the
[Directions](../common/navigation/src/main/kotlin/com/miquido/android/navigation/Directions.kt) and
[Destinations](../common/navigation/src/main/kotlin/com/miquido/android/navigation/Destinations.kt) files, respectively.

To navigate from one feature to another, inject
[Navigator](../common/navigation/src/main/kotlin/com/miquido/android/navigation/Navigator.kt) interface
(in most cases to your `ViewModel`) and call `navigate(Destination)` method.

[Main Activity](../app/src/main/kotlin/com/miquido/androidtemplate/MainActivity.kt) utilizes
[Navigation](../common/navigation/src/main/kotlin/com/miquido/android/navigation/Navigation.kt) interface
to collect navigation events and pass them to the `NavController`. Finally, in the main Activity, within `NavHost`,
you should define your globally available Composables and bind them to your Directions.

### Bottom navigation

Bottom navigation is defined in the [main module](../features/main) which is the entry point for the app after authentication.

Items are defined as a [BottomNavigationItem](../features/main/src/main/kotlin/com/miquido/androidtemplate/main/BottomNavigation.kt) sealed class
containing a route, an icon, and a label. They are used for bottom navigation entries definition.

Bottom navigation definition itself can be found in the [Main composable](../features/main/src/main/kotlin/com/miquido/androidtemplate/main/Main.kt).
It contains logic for the correct behavior of a back button to not create a large stack of destinations when users select items.
It also gives a possibility to restore the state between tabs.

Separate `NavHost` has to be created for bottom navigation with entries that are navigated to when clicking on the tab.
Inside it, entry feature Composables are defined. They are placed in separated features modules.

### Nested navigation

Feature modules usually contain multiple screens that together form a closed flow. You can use
[nested navigation](https://developer.android.com/jetpack/compose/navigation#nested-nav) to implement navigation between module
screens. It's a good way to modularize your navigation and to handle NavHost's tendency to grow over time. Also, this will allow you
to mark your module screens as `internal` and, as a result, hide them from other modules. Other modules won't be able to navigate to
your feature's inner screens; however, it will be possible to navigate to the feature itself.

First, define routes that will be used internally in a simple object. Then, add `NavGraphBuilder` extension method that constructs
a nested NavGraph. Define its route, start destination, and finally add some Composables to it. Call your extension method from
the NavHost. To navigate to the feature from the outside of the feature module, use nested graph's route.

For an example of nested navigation implementation, see [settings](../features/settings) feature module.

### Transitions between screens

In order to add transition animation between screens, `enterTransition` and/or `exitTransition` parameters need to be provided
for Composables definitions. Global transitions for the whole app (e.g. animation time is defined there) can be found
in the [Transitions object](../common/theme/src/main/kotlin/com/miquido/android/theme/Transitions.kt) in the theme module.
They should be reused across the app.

Additionally, default transitions can be provided for a whole `AnimatedNavHost` or nested navigation graph in the `navigation`
definition using the same parameters as mentioned above. However, transitions provided for a particular Composable take
precedence over global ones defined for the whole (sub)graph.

For an example of transitions definition, look at the [MainActivity](../app/src/main/kotlin/com/miquido/androidtemplate/MainActivity.kt).

## Screen architecture

Android Template comes with some utilities to quickly build screens based on AndroidX-Lifecycle ViewModel and Composable.

To see an example of a simple screen based on this architecture, refer to the [authentication](../features/authentication) feature module.

### ViewModel and constructor injection

When creating Composable, you can use `hiltViewModel()` extension method to obtain ViewModel scoped to the navigation graph.
Then, annotate your ViewModel with `@HiltViewModel` and add constructor annotated with `@Inject`. This will allow you to inject
ViewModel dependencies (e.g. use cases, repositories) via constructor injection.

### Coroutines in ViewModel

In your ViewModel, you can safely use `viewModelScope` when you need to launch coroutine scoped to the ViewModel lifecycle.
Please note that `viewModelScope` is bound to `Dispatchers.Main.immediate`.

With Flow things are a little bit different. Hot flows produce values regardless of the presence of subscribers, while cold flows 
execute the producer block on-demand, when a new subscriber collects. You don't want to produce values in hot flows when your 
screen is, for example, in background, as they shouldn't be collected.

The easiest way to not waste resources and produce values only when the Composable is present is to return cold flow from the 
ViewModel (to see how to wrap any flow with the cold flow, check 
[ThemeInfoViewModel](../features/settings/src/main/kotlin/com/miquido/androidtemplate/settings/theme/ThemeInfoViewModel.kt)). 
To avoid multiple invocations of flow construction after each recomposition, you can use `remember` method with ViewModel as a key. 

You can also use `flowWithLifecycle` method to make your flow lifecycle-aware, but this requires additional amount of code in the 
Composable. It can sometimes be a better solution, though (e.g. if your screen is purely UI-based and you don't use ViewModel).
You can see how this is done in theme observing in [MainActivity](../app/src/main/kotlin/com/miquido/androidtemplate/MainActivity.kt).

>>>
Further reading on Flows, ViewModel and Composables:
- [Better handling states between ViewModel and Composable](https://proandroiddev.com/better-handling-states-between-viewmodel-and-composable-7ca14af379cb)
- [A safer way to collect flows from Android UIs](https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda)
>>>

Your ViewModels should be unit tested. If your ViewModel implementation uses `viewModelScope`, first you need to override main
dispatcher by calling `Dispatchers.setMain(StandardTestDispatcher())`, and then execute your test code in the test coroutine
using `runTest` method.

### Single UI state and handling UI actions

ViewModels usually have to do two things: pass an object representing UI state to the Composable and emit one-time actions to be
consumed by the Composable. Setting up both of these can be simplified by using base ViewModels and utility classes provided by the
[mvvm](../common/mvvm) module.

Use `StateViewModel` if you need to handle UI state only, `ActionsViewModel` if you need to handle UI actions only,
and `StateActionsViewModel` if you need to handle both of them.

When extending from state-enabled ViewModel, first you need to specify initial state via base ViewModel constructor. Then call
`setState` or `updateState` method whenever you need to pass new state value to the Composable. To collect states, in your Composable
combine Flow exposed by the base ViewModel, property delegation and `collectAsState` method:
```kotlin
val uiState by viewModel.states.collectAsState()
```

One-time UI actions can be emitted by calling `emitAction` method. To handle them in `Composable`, use `ActionsEffect`:
```kotlin
val coroutineScope = rememberCoroutineScope()
...
ActionsEffect(viewModel, coroutineScope) { action ->
    ...
}
```

## Coroutines

[coroutines](../core/coroutines) core module provides Hilt-based utilities that can simplify writing and testing coroutines code, 
especially use cases/repositories.

### Injecting dispatchers

One of the best practices when working with Coroutines-based code is to 
[inject dispatchers](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#inject-dispatchers). This way you can 
properly test your code by replacing dispatchers with test implementations.

Android Template provides a way to inject dispatchers with Hilt to any place in your code. Annotate the dispatcher with 
`@DefaultDispatcher`, `@IoDispatcher,` `@MainDispatcher` or `@MainImmediateDispatcher` to obtain a specific type of it. 
In unit tests, if you use constructor injection, when creating the object under test, you can simply pass `StandardTestDispatcher` as 
a dispatcher. In instrumented tests, there's already implemented 
[TestDispatchersModule](../app/src/androidTest/kotlin/com/miquido/android/coroutines/di/TestDispatchersModule.kt) that automatically 
replaces dispatcher implementations with the ones that are suitable for instrumented tests.

### Application-scoped CoroutineScope

Sometimes you may want to run a job that needs to outlive the caller's lifecycle. For example, when scheduling some background update 
that needs to be performed even if user moves away from the screen, it's not a good idea to use `viewModelScope`. `GlobalScope` could 
be used in such case, however, 
[it should be avoided](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#global-scope).

If you encounter such case, simply inject `@ApplicationScope`-annotated `CoroutineScope` that is provided by the `coroutines` module, 
and use it to launch your coroutine.

## UI

### Theming

Application theme is implemented in the [theme](../common/theme) module.
It is based on the Material Design theme and provides colors (for light and dark mode), typography and shapes setup.
All Composables that are rendered within main Activity have this theme automatically applied.

You can read more about theming in Jetpack Compose [here](https://developer.android.com/jetpack/compose/themes).

### Theme switching

In application settings, there's a possibility to switch between system default (available for Android 10 and greater)/light/dark 
theme. The mechanism is provided by the [thememanager](../common/thememanager) module. User-selected option is saved using 
[preferences](../core/preferences) functionality; then the information is consumed by the AppCompatDelegate API when the application 
is created, and also it is observed in the main Activity to make the Compose theme use correct colors palette.

Note: when user has selected light or dark theme, and the operating system is switched to the opposite one (using system settings 
available on Android 10+), splash screen will use system-selected theme. This is caused by the fact that the AppCompatDelegate API 
must be called in the Application's onCreate method, which is after the splash screen is rendered.

### Common UI

In real-world projects, usually some generic UI components (like headers, list items and others) are reused across the application 
to make the experience consistent. If you need to add your own, custom reusable UI components, you can place them in the 
[common UI](../common/ui) module. Whenever you need to use one of them, just attach the common UI module dependency to your feature 
module.

When creating Composables that are reusable UI components, remember that it's a good pattern to allow to specify additional modifiers 
in the place of the usage. For example, a developer that prepares an application screen may want to add additional padding to the 
component to increase spacing between screen elements. For how to handle additional modifiers, see basic components implementation 
that the template provides.

### Splash screen

SplashScreen API introduced in Android 12 is used in the project. It gives the possibility to easily define how splash screen
looks like and it's compatible with lower Android versions.

Basic configuration can be found in the [themes.xml](../app/src/main/res/values/themes.xml) file.
Things such as screen background or icon can be customized there. For all configuration options please refer to
[documentation](https://developer.android.com/guide/topics/ui/splash-screen).

For backward compatibility with lower APIs, `initializeSplashScreen()` needs to be invoked in the `Activity`.
Inside this method, there is a logic responsible for exit animation. Thanks to that, we have more control over the
disappearance of a splash screen. It can also be adjusted if necessary.

### Going edge-to-edge

To create immersive experience, main Activity's content is drawn behind the system bars.

By default, system bars are transparent, dark icons are used in light mode, and light icons are used in dark mode.
In case you need to change this behavior for some of the screens (when top or bottom part of the screen is lighter or darker),
use [SystemUiController](https://google.github.io/accompanist/systemuicontroller/) provided by the Accompanist and call one of the
`setSystemBarsColor`, `setStatusBarsColor` or `setNavigationBarsColor` methods. Remember to test the behavior with both
gesture and button navigation, on pre- and post-API 29 (Android 10) devices, in light and dark mode.

All Composables that are placed within main Activity are wrapped with function providing window insets values. You can use
[modifiers](https://google.github.io/accompanist/insets/#modifiers) or
[padding values](https://google.github.io/accompanist/insets/#paddingvalues) to make your Composables respect system bars.

Unfortunately, Compose Material's layouts do not support the use of content padding, which means that passing window insets modifiers
or padding values to them won't produce the desired effect. Whenever you need to have `Scaffold`, `TopAppBar` or `BottomNavigation`
in your Composable, use [inset-aware replacements](https://google.github.io/accompanist/insets/#inset-aware-layouts-insets-ui) of that
layouts provided by the Accompanist.

## Testing Composables

You can prepare UI tests for your Composables in the `androidTest` source set of the application module.

To use Composable within empty Activity for testing purposes, add `AndroidComposeTestRule` that operates on a `TestActivity` 
to your test class. Then, in `@Before`-annotated method, call `setContent` method on the rule to create your Composable.

If your feature module hides Composables behind `internal` keyword, but exposes `NavGraphBuilder` extension method (see 
"Nested navigation" section), you can use `TestAnimatedNavHost` Composable to create simple navigation structure within TestActivity 
and add your nested navigation graph to it.

You can use `TestNavigation` object to verify global navigation from Composable under test.

Note that `TestActivity` is Hilt-enabled. All of the tests that use it need to be annotated with `@HiltAndroidTest`, have 
`HiltAndroidRule` added and call `hiltRule.inject()` in the `@Before`-annotated method (before calling any method on the 
`AndroidComposeTestRule`).
