package com.kuzznya.lab.model

data class Point (
    var x: Double,
    var y: Double
) {
    fun move(v: Vector) {
        x += v.x
        y += v.y
    }

    operator fun plus(v: Vector) =
        Point(x + v.x, y + v.y)
}