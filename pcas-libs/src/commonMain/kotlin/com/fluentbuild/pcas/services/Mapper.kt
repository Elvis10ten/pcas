package com.fluentbuild.pcas.services

fun interface Mapper<FromT, ToT> {

    fun map(from: FromT): ToT
}