package com.fluentbuild.pcas.widgets.foundation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.values.Observable

class LifecycleAwareSubscription<ValueT>(
    lifecycle: Lifecycle,
    observable: Observable<ValueT>,
    observer: (ValueT) -> Unit
) {

    init {
        var cancellable: Cancellable = SentinelCancellable
        lifecycle.addObserver(object: LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreated() {
                cancellable = observable.subscribe(observer)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroyed() {
                cancellable.cancel()
            }
        })
    }
}