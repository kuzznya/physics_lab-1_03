package com.kuzznya.lab.model

import kotlinx.coroutines.delay

class PhysSystem (
    val objects: MutableList<DrawablePhysObject>,
    val ground: Ground,
    private val g: Vector = Vector(0.0, -9.81)
) {
    private val collided: MutableMap<Pair<PhysObject, PhysObject>, Boolean> = mutableMapOf()

    init {
        objects += ground
    }

    private val isObjectOnTheGround: MutableMap<PhysObject, Boolean> =
        objects.associateWith { it.onTheGround(ground) }.toMutableMap()

    private fun getAcceleration(body: PhysObject): Vector {
        if (body is Ground)
            return Vector(0.0, 0.0)

        val mg: Vector = g * body.mass
        val N: Vector = if (body.onTheGround(ground)) mg * -1.0 else Vector(0.0, 0.0)

        return (mg + N) / body.mass
    }

    private fun collide(body1: PhysObject, body2: PhysObject, elastic: Boolean = true) {
        if (elastic) {
            val v1x = ((body1.mass - body2.mass) * body1.speed.x + 2 * body2.mass * body2.speed.x) /
                    (body1.mass + body2.mass)
            val v1y = ((body1.mass - body2.mass) * body1.speed.y + 2 * body2.mass * body2.speed.y) /
                    (body1.mass + body2.mass)
            body1.speed = Vector(v1x, v1y)

            val v2x = ((body2.mass - body1.mass) * body2.speed.x + 2 * body1.mass * body1.speed.x) /
                    (body1.mass + body2.mass)
            val v2y = ((body2.mass - body1.mass) * body2.speed.y + 2 * body1.mass * body1.speed.y) /
                    (body1.mass + body2.mass)
            body2.speed = Vector(v2x, v2y)

        }
        else {
            TODO("non-elastic collision")
        }
    }

    private fun checkAndCollide(body1: PhysObject, body2: PhysObject, elastic: Boolean = true) {
        if (
            body1 != body2 && collided[Pair(body1, body2)] != true && collided[Pair(body2, body1)] != true && (
            body2.intersects(Point(body1.position.x - body1. width / 2.0, body1.position.y)) ||
            body2.intersects(Point(body1.position.x + body1. width / 2.0, body1.position.y)) ||

            body2.intersects(Point(body1.position.x, body1.position.y - body1.height / 2.0)) ||
            body2.intersects(Point(body1.position.x, body1.position.y + body1.height / 2.0)) ||

            body1.intersects(Point(body2.position.x - body2. width / 2.0, body2.position.y)) ||
            body1.intersects(Point(body2.position.x + body2. width / 2.0, body2.position.y)) ||

            body1.intersects(Point(body2.position.x, body2.position.y - body2.height / 2.0)) ||
            body1.intersects(Point(body2.position.x, body2.position.y + body2.height / 2.0)))
        ) {
            collide(body1, body2, elastic)
            collided[Pair(body1, body2)] = true
        }
    }

    fun compute(secondsPassed: Double) {
        objects.forEach { body1 -> objects.forEach { body2 -> checkAndCollide(body1, body2) } }
        objects.forEach {
            it.move(secondsPassed, getAcceleration(it)) }
        collided.clear()
    }

    suspend fun start() {
        var time = System.currentTimeMillis()
        repeat(Int.MAX_VALUE) {
            compute((System.currentTimeMillis() - time) / 1000.0)
            time = System.currentTimeMillis()
            delay(20)
        }
    }
}