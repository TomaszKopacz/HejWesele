package com.hejwesele.settings.appinfo

import com.google.common.truth.Truth.assertThat
import com.hejwesele.android.appinfo.AppInfo
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class AppInfoViewModelTest {

    private val appInfo: AppInfo = mock {
        on { appName } doReturn "Android Template"
        on { versionName } doReturn "1.0.0"
        on { versionCode } doReturn 1
    }

    private val viewModel = AppInfoViewModel(appInfo)

    @Test
    fun `show app information when initializing`() {
        assertThat(viewModel.states.value).isEqualTo(AppInfoUiState("Android Template 1.0.0 (1)"))
    }
}
