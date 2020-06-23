package com.kuzznya.lab.model

import kotlin.math.*

data class Vector (
    var x: Double,
    var y: Double
) {
    val value: Double
        get() = sqrt(x.pow(2) + y.pow(2))

    operator fun plus(b: Vector): Vector =
        Vector(x + b.x, y + b.y)

    operator fun minus(b: Vector): Vector =
        this + b * -1.0

    operator fun times(k: Double): Vector =
        Vector(x * k, y * k)

    operator fun div(k: Double): Vector =
        Vector(x / k, y / k)

    fun turn(angle: Double): Vector =
        Vector(x * cos(angle) - y * sin(angle),
            y * cos(angle) + x * sin(angle)
        )

    fun angle(v: Vector): Double =
        if (this == Vector(0.0, 0.0) || v == Vector(0.0, 0.0))
            0.0
        else {
            acos(
                max(min(
                    (x * v.x + y * v.y) /
                            sqrt(x.pow(2) + y.pow(2)) / sqrt(v.x.pow(2) + v.y.pow(2)),
                    1.0), -1.0)
            )
        }
}