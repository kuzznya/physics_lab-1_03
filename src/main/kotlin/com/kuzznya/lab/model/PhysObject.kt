package com.kuzznya.lab.model

import kotlin.math.*

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
        speed = speed.turn(angle)
    }

    open fun intersects(point: Point): Boolean =
        point.x > position.x - width / 2.0 &&
                position.x < position.x + width / 2.0 &&
                point.y > position.y - height / 2.0 &&
                position.y < position.y + height / 2.0

    open fun intersects(body2: PhysObject): Boolean {
        val l1x = position.x - width / 2.0
        val l1y = position.y + height / 2.0
        val r1x = position.x + width / 2.0
        val r1y = position.y - height / 2.0
        val l2x = body2.position.x - body2.width / 2.0
        val l2y = body2.position.y + body2.height / 2.0
        val r2x = body2.position.x + body2.width / 2.0
        val r2y = body2.position.y - body2.height / 2.0
        return !(l1x >= r2x || l2x >= r1x) && !(l1y <= r2y || l2y <= r1y)
    }

    open fun onTheGround(ground: Ground): Boolean =
        (position.y - height / 2.0) - ground.position.y < 0.5 && abs(speed.y) < 0.8
}