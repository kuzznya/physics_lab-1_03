package com.kuzznya.lab.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.runInterruptible
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

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
        val N: Vector = if (body.intersects(ground)) mg * -1.0 else Vector(0.0, 0.0)

        return (mg + N) / body.mass
    }

    private fun collide(body1: PhysObject, body2: PhysObject, elastic: Boolean = true) {
        if (body1 == body2)
            return

        val centersVector: Vector =
            when {
                body1 is Ground -> body1.normal * -1.0
                body2 is Ground -> body2.normal * -1.0
                else -> body2.position - body1.position
            }

        val body1Angle: Double = body1.speed.angle(centersVector)
        val body2Angle: Double = body2.speed.angle(centersVector)

        val v1c: Vector = if (body1 is Ground) body1.speed else body1.speed.turn(-body1Angle) * cos(body1Angle)
        val v1p: Vector = if (body1 is Ground) body1.speed else body1.speed.turn(PI / 2.0 - body1Angle) * sin(body1Angle)

        val v2c: Vector = if (body2 is Ground) body2.speed else body2.speed.turn(-body2Angle) * cos(body2Angle)
        val v2p: Vector = if (body2 is Ground) body2.speed else body2.speed.turn(PI / 2.0 - body2Angle) * sin(body2Angle)

        if (elastic) {
            body1.speed = (v1c * (body1.mass - body2.mass) + v2c * 2.0 * body2.mass) / (body1.mass + body2.mass) + v1p
            body2.speed = (v2c * (body2.mass - body1.mass) + v1c * 2.0 * body1.mass) / (body1.mass + body2.mass) + v2p

            if (body2 is Ground) body1.speed.y /= 2.0
            if (body1 is Ground) body2.speed.y /= 2.0

            body1.move(0.001)
            body2.move(0.001)
        }
        else {
            TODO("non-elastic collision")
        }
    }

    private fun checkAndCollide(body1: PhysObject, body2: PhysObject, elastic: Boolean = true) {
        if (
            body1 != body2 &&
            collided[Pair(body1, body2)] != true && collided[Pair(body2, body1)] != true &&
            !(body1 == ground && body2.onTheGround(ground)) &&
            !(body2 == ground && body1.onTheGround(ground)) &&
            (body1.intersects(body2) || body2.intersects(body1))
        ) {
            collide(body1, body2, elastic)
            println("Collision: $body1 $body2 ${body1.speed} ${body2.speed}")
            collided[Pair(body1, body2)] = true
        }
    }

    private fun checkForGround(body: PhysObject) {
        if (body is Ground)
            return
        if ((body.intersects(ground) || ground.intersects(body)) && body.speed.y < 0.0) {
            body.speed.y = 0.0
            body.position.y = ground.position.y + body.height / 2.0
        }
    }

    fun compute(secondsPassed: Double) {
        objects.forEach { body1 -> objects.forEach { body2 -> checkAndCollide(body1, body2) } }
        for ((i, body1) in objects.withIndex()) {
//            checkForGround(body1)
            for (body2 in objects.filterIndexed { index, _ -> index > i }) {
                checkAndCollide(body1, body2)
            }
        }
        objects.forEach {
            checkForGround(it)
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