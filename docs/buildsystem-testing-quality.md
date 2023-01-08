# Build system, testing, quality

[[_TOC_]]

## Build types

Project includes three build types: `debug`, `qa`, `release`:
- `debug` builds are used for day-to-day development,
- `qa` builds should be shared with testers; `qa` build type should be as similar to `release` as possible, but can include e.g.
  connecting to test environments,
- `release` builds are the final ones, they can be uploaded to the Play Store.

## Working with modules, project dependencies and versions

Version name, version code, compile/min/target SDK versions are kept in `gradle/versions.gradle`.

For keeping project dependencies and versions, we use
[version catalog](https://docs.gradle.org/current/userguide/platforms.html#sub:central-declaration-of-dependencies)
in a form of [libs.versions.toml](../gradle/libs.versions.toml) file.

To check if there are new versions of dependencies available, run `./gradlew depUp`. Only stable versions will be reported.

Updating dependencies can be made with the [Version catalog update plugin](https://github.com/littlerobots/version-catalog-update-plugin).
To update all dependencies included in `libs.versions.toml` file just execute `./gradlew versionCatalogUpdate`.
It is important to re-add all removed comments from `libs.versions.toml` file manually.

You can also use
[type-safe project accessors](https://docs.gradle.org/current/userguide/declaring_dependencies.html#sec:type-safe-project-accessors)
when defining dependencies between project's modules.

[Dependency analysis plugin](https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin) is added to the project.
It can be used to find unused dependency declarations, dependencies declared with wrong configuration (`api` vs `implementation`), and
transitive dependencies that should be explicitly declared in the module's `build.gradle`.

[Snyk](https://snyk.io/) is added to the project. It checks all the project modules for dependencies vulnerabilities. 
It runs on CI for every push, and if you want to run it locally, invoke `./gradlew snyk-test`.

## Static code analysis

To perform fast, basic Kotlin lint checks, use ktlint: `./gradlew ktlint`. Ktlint uses `gradle/.editorconfig` as its configuration.

It's also a good idea to [set up a git hook for running ktlint check on pre-commit](https://github.com/pinterest/ktlint#usage).

For more sophisticated Kotlin lint checks, use detekt: `./gradlew detekt`. Detekt uses `gradle/detekt-config.yml` as its configuration.

For Android-specific lint checks, use Android Lint: `./gradlew :app:lintDebug`. Excluded checks and other configuration options are kept in
`gradle/moduleSetup.gradle`.

## Unit tests

The template uses junit4 for running unit tests. For mocking, mockito-kotlin is used. Advanced assertion can be performed using
Google Truth (`assertThat(...)`).

To run unit tests for all modules, use `./gradlew unitTest`. For Android-flavored modules, this will run `debug` build type tests.

## Instrumented unit tests

You can prepare and run instrumented unit tests that are placed in [application module's androidTest source set](../app/src/androidTest).

Tests are run using Hilt-based test runner, so you can perform injections in tests. See [documentation](https://dagger.dev/hilt/testing) for more information.

Remember to create instrumented unit tests only if it's really needed; otherwise, use plain JUnit tests.

To run instrumented unit test on CI, annotate test class with `@InstrumentedUnitTest`.

## UI tests

You can prepare and run UI tests that are placed in [application module's androidTest source set](../app/src/androidTest).

To run UI test on CI, annotate test class with `@UiTest`.

## Test suites

You can run all instrumented unit tests using [InstrumentedUnitTestsSuite](../app/src/androidTest/kotlin/com/miquido/android/InstrumentedUnitTestsSuite.kt)
or all UI tests using [UiTestsSuite](../app/src/androidTest/kotlin/com/miquido/androidtemplate/UiTestsSuite.kt).

## Test code coverage

We can calculate unit tests code coverage by using JaCoCo Gradle Plugin.
In [gradle](../gradle) directory there are defined two tasks for code coverage calculations:
1. [jacocoModuleReport](../gradle/jacocoModule.gradle) calculating coverage for each gradle module separately,
1. [jacocoProjectReport](../gradle/jacocoProject.gradle) calculating coverage for whole projects.

Example of usage:

*Command:*
```
    ./gradlew jacocoProjectReport
```

*Result:*
- In every gradle module you will find coverage report in directory: `[module]/build/report/jacoco/jacocoModuleReport`.
- Additionally in root module there will be coverage report for whole project: `/build/report/jacoco/jacocoProjectReport`.

## LeakCanary

LeakCanary, a tool for memory leaks detection, is by default attached to debug builds.

## Licenses

[Licensee](https://github.com/cashapp/licensee) is added to the project and configured according to
[Miquido standards](https://miquido.atlassian.net/wiki/spaces/MIQ/pages/250773542/Software+licenses+overview).

Licenses check (which runs for debug build type dependencies) is attached to the `verification` CI job.
To run it by yourself, invoke `./gradlew :app:licenseeDebug`. You may also consider running licenses check for release build type 
from time to time, since release dependencies can sometimes be slightly different (depending on your setup). 

Licensee generates a JSON file with license information on every artifact in the dependency graph. This file can be copied to the 
application's assets, then parsed and presented in the application's settings. To copy release build type licenses information file 
to the application's assets, run `./gradlew :app:licensesCopy`. This is also invoked on CI before building and publishing release 
bundle to the Play Store - to ensure that the release build contains up-to-date licenses information.

## Build scans

Gradle Build Scans can help you to analyze what is going on when running your Gradle build locally or on CI. You can troubleshoot 
your build performance issues, inspect tasks graph, examine execution data, caching settings, infrastructure that was used to run the 
build etc.

To generate build scan for your build, run `./gradlew` command with `--scan` option. To create build scan for job running on CI, edit 
`.gitlab-ci.yml` and add `--scan` option to the selected `./gradlew` command. It is also a good idea to remove `--build-cache` flag, 
since running build with build cache enabled will give unmeasurable results.
