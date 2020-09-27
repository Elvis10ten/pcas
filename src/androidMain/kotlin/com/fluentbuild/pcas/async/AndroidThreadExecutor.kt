package com.fluentbuild.pcas.async

class AndroidThreadExecutor: ThreadExecutor {

    override fun onMain(action: () -> Unit): Cancellable {
        TODO("Not yet implemented")
    }

    override fun onBackground(action: () -> Unit): Cancellable {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }
}