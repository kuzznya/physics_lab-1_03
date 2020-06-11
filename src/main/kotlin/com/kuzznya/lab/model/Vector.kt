package com.kuzznya.lab.model

import kotlin.math.pow
import kotlin.math.sqrt

data class Vector (
    var x: Double,
    var y: Double
) {
    fun value() = sqrt(x.pow(2) + y.pow(2))
}