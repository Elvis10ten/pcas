package com.fluentbuild.pcas.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.values.Observable

class FragmentAwareSubscription<ValueT>(
    fragment: Fragment,
    observable: Observable<ValueT>,
    observer: (ValueT) -> Unit
) {

    init {
        var cancellable: Cancellable = SentinelCancellable
        fragment.viewLifecycleOwnerLiveData.observe(fragment, {
            it.lifecycle.addObserver(object: LifecycleObserver {

                @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
                fun onCreated() {
                    cancellable = observable.subscribe(observer)
                }

                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroyed() {
                    cancellable.cancel()
                }
            })
        })
    }
}