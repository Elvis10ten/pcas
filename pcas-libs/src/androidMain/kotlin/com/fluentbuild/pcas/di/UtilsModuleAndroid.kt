package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.utils.AndroidTimeProvider

internal class UtilsModuleAndroid: UtilsModuleJvm() {

    override val timeProvider = AndroidTimeProvider()
}