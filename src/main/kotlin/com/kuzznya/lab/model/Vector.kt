package com.kuzznya.lab.model

import kotlin.math.pow
import kotlin.math.sqrt

data class Vector (
    var x: Double,
    var y: Double
) {
    fun value() = sqrt(x.pow(2) + y.pow(2))

    operator fun plus(b: Vector): Vector =
        Vector(x + b.x, y + b.y)

    operator fun times(k: Double): Vector =
        Vector(x * k, y * k)

    operator fun div(k: Double): Vector =
        Vector(x / k, y / k)
}