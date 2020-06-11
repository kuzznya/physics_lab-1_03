package com.kuzznya.lab.model

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

abstract class PhysObject (
    open var mass: Double,
    open var position: Point,
    var speed: Vector
) {
    open fun move(secondsPassed: Double) {
        position.x += speed.x * secondsPassed
        position.y += speed.y * secondsPassed
    }

    open fun move(secondsPassed: Double, acceleration: Vector) {
        move(secondsPassed)

        position.x += acceleration.x.pow(2) * secondsPassed / 2
        position.y += acceleration.y.pow(2) * secondsPassed / 2

        speed.x += acceleration.x * secondsPassed
        speed.y += acceleration.y * secondsPassed
    }

    fun turn(angle: Double) {
        speed = Vector(speed.x * cos(angle) - speed.y * sin(angle),
             speed.y * cos(angle) + speed.x * sin(angle))
    }

    abstract fun intersects(point: Point): Boolean
}