package com.fluentbuild.pcas.adapters

import java.util.*

inline val Enum<*>.formattedName get() = name.toLowerCase(Locale.US).capitalize(Locale.US)