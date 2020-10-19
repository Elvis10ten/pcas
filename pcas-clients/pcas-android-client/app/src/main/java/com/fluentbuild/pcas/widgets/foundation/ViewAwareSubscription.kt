package com.fluentbuild.pcas.widgets.foundation

import android.view.View
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.values.Observable

class ViewAwareSubscription<ValueT>(
    view: View,
    observable: Observable<ValueT>,
    observer: (ValueT) -> Unit
) {

    init {
        var cancellable: Cancellable = SentinelCancellable
        view.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {

            override fun onViewAttachedToWindow(view: View) {
                cancellable = observable.subscribe(observer)
            }

            override fun onViewDetachedFromWindow(view: View) {
                cancellable.cancel()
            }
        })
    }
}