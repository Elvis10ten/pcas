package com.fluentbuild.pcas.utils

fun interface Mapper<FromT, ToT> {

    fun map(from: FromT): ToT
}