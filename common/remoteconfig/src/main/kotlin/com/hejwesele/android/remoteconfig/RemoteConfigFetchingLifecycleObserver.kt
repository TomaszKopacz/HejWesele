package com.hejwesele.android.remoteconfig

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.Lazy
import javax.inject.Inject

fun RemoteConfigFetchingLifecycleObserver.attachItself() =
    ProcessLifecycleOwner.get().lifecycle.addObserver(this)

class RemoteConfigFetchingLifecycleObserver @Inject constructor(
    private val remoteConfig: Lazy<RemoteConfig>
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        remoteConfig.get().fetch()
    }
}
