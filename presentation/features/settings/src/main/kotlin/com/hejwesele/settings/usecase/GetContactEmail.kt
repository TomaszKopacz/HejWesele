package com.hejwesele.settings.usecase

import com.hejwesele.android.theme.Label
import javax.inject.Inject

class GetContactEmail @Inject constructor() {

    operator fun invoke() = Label.settingsContactEmailText
}
