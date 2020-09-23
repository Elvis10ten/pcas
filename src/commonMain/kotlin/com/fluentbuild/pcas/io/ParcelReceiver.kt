package com.fluentbuild.pcas.io

fun interface ParcelReceiver {

    fun onReceived(parcel: Parcel)
}