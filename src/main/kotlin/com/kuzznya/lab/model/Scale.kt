package com.kuzznya.lab.model

import kotlin.math.abs

class Scale (
    private val fromLD: Point,
    private val fromRU: Point,
    private val toMin: Double,
    private val toMax: Double,
    private val reverseY: Boolean = true
) {
    fun transform(point: Point) = Point(
        transformX(point.x - fromLD.x) + toMin,
        if (!reverseY)
            transformY(point.y - fromLD.y) + toMin
        else
            toMax - transformY(point.y - fromLD.y)
    )

    fun transform(vector: Vector) = Vector(
        transformX(vector.x),
        transformY(vector.y) * (if (reverseY) -1.0 else 1.0)
    )

    fun transformX(x: Double) = x / (fromRU.x - fromLD.x) * (toMax - toMin)
    fun transformY(y: Double) = y / (fromRU.y - fromLD.y) * (toMax - toMin)
}