package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.utils.TimeProviderAndroid

internal class UtilsModuleAndroid: UtilsModuleJvm() {

    override val timeProvider = TimeProviderAndroid()
}