package com.kuzznya.lab.model

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sin

abstract class PhysObject (
    open var mass: Double,
    open var position: Point,
    open var speed: Vector,
    open var width: Double,
    open var height: Double
) {
    open fun move(secondsPassed: Double) {
        position += speed * secondsPassed
    }

    open fun move(secondsPassed: Double, acceleration: Vector) {
        move(secondsPassed)
        position += acceleration * secondsPassed.pow(2) / 2.0
        speed += acceleration * secondsPassed
    }

    fun turn(angle: Double) {
        speed = Vector(speed.x * cos(angle) - speed.y * sin(angle),
             speed.y * cos(angle) + speed.x * sin(angle))
    }

    open fun intersects(point: Point): Boolean =
        point.x > position.x - width / 2.0 &&
                position.x < position.x + width / 2.0 &&
                point.y > position.y - height / 2.0 &&
                position.y < position.y + height / 2.0

    abstract fun onTheGround(ground: Ground): Boolean
}