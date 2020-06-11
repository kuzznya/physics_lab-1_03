package com.kuzznya.lab.model

class Scale (
    private val fromLD: Point,
    private val fromRU: Point,
    private val toLD: Point,
    private val toRU: Point,
    private val reverseY: Boolean = false
) {
    fun transform(point: Point) = Point(
        transformX(point.x - fromLD.x) + toLD.x,
        transformY(point.y - fromLD.y) + toLD.y
    )

    fun transform(vector: Vector) = Vector(
        transformX(vector.x),
        transformY(vector.y)
    )

    fun transformX(x: Double) = x / (fromRU.x - fromLD.x) * (toRU.x - toLD.x)
    fun transformY(y: Double) = y / (fromRU.y - fromLD.y) * (toRU.y - toLD.y)
}