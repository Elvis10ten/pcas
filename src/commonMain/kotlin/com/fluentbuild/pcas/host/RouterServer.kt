package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.io.Port

interface RouterServer {

    fun init(): Port

    fun close()
}