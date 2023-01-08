# Getting started

[[_TOC_]]

## How to start using the template

1. Create a repository for your project
1. Set up new remote called "upstream", pointing to `android-template` repository, syncing only main branch:
    ```
    git remote add -t main upstream git@gitlab.com:miquido/dev/android/android-template.git
    ```
1. Verify if remote was added correctly:
    ```
    $ git remote -v
    > origin    <your repository here> (fetch)
    > origin    <your repository here> (push)
    > upstream  git@gitlab.com:miquido/dev/android/android-template.git (fetch)
    > upstream  git@gitlab.com:miquido/dev/android/android-template.git (push)
    ```
1. Fetch changes from upstream:
    ```
    git fetch upstream
    ```
1. Checkout your local main branch and merge template's main to your main branch:
    ```
    git checkout main
    git merge --squash --allow-unrelated-histories upstream/main
    git commit -m "Merge Android Template"
    ```
1. Whenever you need to add some new changes from the template to your project's repository, fetch upstream and merge
   template's main again:
    ```
    git fetch upstream
    git merge --squash --allow-unrelated-histories upstream/main
    git commit -m "Update project with latest Android Template changes"
    ```

## Steps needed after merging template to your project

1. Change application id (`applicationId`) and application name (`app_name` resValue in `defaultConfig` and `debug`, `qa` build types) in [app/build.gradle](../app/build.gradle)
1. Change package structure from `com.miquido.androidtemplate` and `com.miquido.android` to the structure defined by client.
1. Generate Google Play App Signing certificate (name: `release.keystore`) and replace the current one in [app/certs](../app/certs) directory.
1. Setup GitLab CI configuration as described in the next section.
1. Setup Firebase platform accordingly to *Firebase* section below.
1. Configure Firebase App Distribution for qa build type as described in the next section.
1. Configure running instrumented unit tests and UI tests on Firebase Test Lab as described in the next section.
1. Configure Gradle Play Publisher for release build type accordingly to *Configuring Gradle Play Publisher* section

### Using GitLab CI configuration
Android Template includes ready to use GitLab CI configuration for your project. It's set up inside [.gitlab-ci.yml](../.gitlab-ci.yml),
[.gitlab-ci-templates.yml](../.gitlab-ci-templates.yml) and [.gitlab-ci-variables.yml](../.gitlab-ci-variables.yml) YAML files.

CI jobs run inside Docker container. The image is automatically built using [Dockerfile](../Dockerfile) and pushed to the project's 
[Container Registry](https://docs.gitlab.com/ee/user/packages/container_registry/). If you need to update core dependencies that are 
pre-downloaded (like Android SDK, Build Tools or Flank version), edit [.gitlab-ci-variables.yml](../.gitlab-ci-variables.yml) and push 
the changes - `build-image` CI job will be triggered and the new Docker image will be built and pushed to the registry. Same applies 
for changing the Dockerfile.

CI configuration includes:
* `verification` job which will perform checks for code style violations, smell analysis for Kotlin, Android lint and unit tests with code coverage when changes on any branch are being pushed,
* `dependencies-scan` job which runs [Snyk](https://snyk.io/) to check for dependencies vulnerabilities,
* `ui-tests(-scheduled)` and `instrumented-unit-tests(-scheduled)` jobs that run tests, on-demand or scheduled, using Flank and Firebase Test Lab,
* `main` job which will publish your app to App Distribution on each merge to the main branch,
* `release` job which will publish your app bundle to Play Store after each git tag in your project is being pushed,
* `qa` and `release` [GitLab environments](https://docs.gitlab.com/ee/ci/environments/)
* `gitlab-release` job which creates [GitLab release](https://docs.gitlab.com/ee/user/project/releases/)

For handling build numbers [gitlabBuildNumber.py](../gradle/gitlabBuildNumber.py) script exists. It uses GitLab variable created and managed by us.
You don't have to bother, everything works automatically, out of the box.

To complete the configuration for your project, you'll need to generate token to version the app by build number and gather release notes.
1. Go to **Settings** -> **Access Tokens**
1. Create a new token with `api` scope.
1. Copy the token
1. Go to **Settings** -> **CI/CD** -> **Variables** -> **Add Variable**
1. As a `Key` enter `GITLAB_ACCESS_TOKEN` and paste the copied token in the `Value` call. Confirm by clicking on `Add variable`.

### Configuring Firebase App Distribution

We use [Firebase App Distribution](https://firebase.google.com/docs/app-distribution) to publish QA build type apps and pass them to testers. To configure Firebase App Distribution:
1. Create Firebase project for your application in [Firebase Console](https://console.firebase.google.com/).
1. Add new Android app for QA builds. Make sure that its package name matches QA builds package name (e.g. com.miquido.androidtemplate.qa).
1. Enable App Distribution for the application: `Release & Monitor -> App Distribution -> Get Started`.
1. In `Testers & Groups` create group named `qa`. QA builds will be distributed to this group. You can add testers manually to this group, or use invite links. You can also specify other groups and change configuration in [app/build.gradle](../app/build.gradle) to publish to multiple groups.
1. In Firebase project settings, find your application's `App ID`. In [app/build.gradle](../app/build.gradle), replace template's app id with it.
1. In [Google Cloud console](https://console.cloud.google.com/identity/serviceaccounts) create service account that will be used to authenticate when uploading package. Assign `Firebase App Distribution Admin` role to it, generate and download private key (json format). **KEEP IT SAFE AND DO NOT SHARE IT WITH ANYONE**.
1. Encode private key file's content using base64 (`cat <private-key>.json | base64`).
1. In GitLab, go to **Settings** -> **CI/CD** -> **Variables** -> **Add Variable**. Paste your encoded private key inside a Value cell. The variable key must be named as `FIREBASE_SERVICE_KEY`.
1. Update Firebase App Distribution url in [.gitlab.ci.yml](../.gitlab-ci.yml) (`main` job, `environment` section)
1. Push changes to the main branch. Main build should be triggered and build should be uploaded to Firebase App Distribution.

### Configuring running tests with Firebase Test Lab

Android Template includes [Flank](https://flank.github.io/flank/) configuration that can be used to run instrumented unit tests and UI tests
on emulators and real devices managed by [Firebase Test Lab](https://firebase.google.com/docs/test-lab).

Note that running tests on Firebase Test Lab is a subject of charge; on free Firebase plan you are limited to running small number of tests each day.
If you want to run a significant number of tests, you need [Blaze plan](https://firebase.google.com/pricing) that includes flexible billing based on devices usage.

Flank configuration files are kept in [flank](../flank) directory. They keep configuration options that are consumed by Flank or passed to the Firebase Test Lab,
like emulator/device model or API version.

To configure running tests automatically every day at given hour:
1. In [Google Cloud console](https://console.cloud.google.com/identity/serviceaccounts) create service account that will be used to authenticate when running tests. Assign `Editor` role to it, generate and download private key (json format). **KEEP IT SAFE AND DO NOT SHARE IT WITH ANYONE**.
1. In [API Library](https://console.developers.google.com/apis/library), find and enable `Cloud Testing API` and `Cloud Tool Results API`.
1. Encode private key file's content using base64 (`cat <private-key>.json | base64`).
1. In GitLab, go to **Settings** -> **CI/CD** -> **Variables** -> **Add Variable**. Paste your encoded private key inside a Value cell. The variable key must be named as `TEST_LAB_SERVICE_KEY`.
1. In GitLab, go to *CI/CD* -> *Schedules*. Add schedule running every day at given hour for `main` branch, with variable `SCHEDULE_NAME` and value `scheduled-tests`.

You can also run tests manually, using the same pipeline that contains auto-triggered `verification` job.

### Configuring Gradle Play Publisher

[Quickstart guide](https://github.com/Triple-T/gradle-play-publisher#quickstart-guide)

We use [Gradle Play Publisher](https://github.com/Triple-T/gradle-play-publisher) to publish release build type bundle to your Google Play Console project. To configure Gradle Play Publisher:
1. Create project for your application in [Google Play Developer Console](https://play.google.com/console/u/0/developers/console/developer/) and perform [Initial Play Store upload](https://github.com/Triple-T/gradle-play-publisher#initial-play-store-upload)
1. Enable the [AndroidPublisher API](https://console.cloud.google.com/apis/library/androidpublisher.googleapis.com) for your Google Cloud project (same as for Firebase)
1. Link your [Google Play developer account](https://play.google.com/console/u/0/developers/api-access) to your GCP project.
1. In [Google Cloud console](https://console.cloud.google.com/identity/serviceaccounts) create service account that will be used to authenticate when uploading package to Google Play Store. Assign `Owner` role to it, generate and download private key (json format). **KEEP IT SAFE AND DO NOT SHARE IT WITH ANYONE**. Give your service account [permissions to publish apps](https://play.google.com/console/u/0/developers/users-and-permissions) on your behalf in Google Play Console (invite the `client_email` from your generated json)
1. Encode private key file's content using base64 (`cat <private-key>.json | base64`).
1. In GitLab:
    * Go to **Settings** -> **CI/CD** -> **Variables** -> **Add Variable**. Paste your encoded private key inside a Value cell.
      The variable key must be named as `PLAY_PUBLISHER_SERVICE_KEY`.
    * In a same way add **Variables** for your app keystore alias named `RELEASE_KEY_ALIAS`, app key password (`RELEASE_KEY_PASSWORD`) and app keystore password (`RELEASE_STORE_PASSWORD`).
1. Configure your release name by editing `projectVersionName` in `gradle/versions.gradle` file.
1. By default plugin is configured to publish your release with status `DRAFT`. This configuration doesn't require you to have fully reviewed and configured project in the Google Play Console. If your Google Play project is fully configured you can then use the `COMPLETED` status.
   If you want to change publishing with the `DRAFT` status find the `play` block in [app/build.gradle](../app/build.gradle) and in `releaseStatus` method replace the `DRAFT` with the status you want.
1. Update Play Console url in [.gitlab.ci.yml](../.gitlab-ci.yml) (`release` job, `environment` section)
1. Create and push git tag for the changes you want to be released. Release build should be triggered and your app bundle should be uploaded to Google Play Console project.
