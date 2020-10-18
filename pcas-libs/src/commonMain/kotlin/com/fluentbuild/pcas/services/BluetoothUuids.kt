package com.fluentbuild.pcas.services

import com.fluentbuild.pcas.host.Uuid

/**
 * See Bluetooth Assigned Numbers document - SDP section, to get the values of UUIDs
 * for the various services.
 *
 * The following 128 bit values are calculated as: uuid * 2^96 + BASE_UUID
 */
val A2DP_UUID = Uuid(byteArrayOf(0, 0, 17, 11, 0, 0, 16, 0, -128, 0, 0, -128, 95, -101, 52, -5) /* 0000110B-0000-1000-8000-00805F9B34FB */)

val HSP_UUID = Uuid(byteArrayOf(0, 0, 17, 8, 0, 0, 16, 0, -128, 0, 0, -128, 95, -101, 52, -5) /* 00001108-0000-1000-8000-00805F9B34FB */)

val HFP_UUID = Uuid(byteArrayOf(0, 0, 17, 30, 0, 0, 16, 0, -128, 0, 0, -128, 95, -101, 52, -5) /* 0000111E-0000-1000-8000-00805F9B34FB */)

val HID_UUID = Uuid(byteArrayOf(0, 0, 17, 36, 0, 0, 16, 0, -128, 0, 0, -128, 95, -101, 52, -5) /* 00001124-0000-1000-8000-00805f9b34fb */)