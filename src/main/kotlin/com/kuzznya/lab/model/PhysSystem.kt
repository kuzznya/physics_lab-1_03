package com.kuzznya.lab.model

import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class PhysSystem (
    val objects: MutableList<DrawablePhysObject>,
    val ground: Ground,
    private val g: Vector = Vector(0.0, -9.81)
) {
    init {
        objects += ground
    }

    private fun getAcceleration(body: PhysObject, secondsPassed: Double): Vector {
        if (body is Ground)
            return Vector(0.0, 0.0)

        val mg: Vector = g * body.mass
        val N: Vector = if (body.onTheGround(ground)) mg * cos(mg.angle(ground.normal)) else Vector(0.0, 0.0)

        val collisionForce: Vector =
            objects.fold(Vector(0.0, 0.0)) { acc, otherBody ->
                acc + getCollisionForce(body, otherBody, secondsPassed) }

        return (mg + collisionForce + N) / body.mass
    }

    private fun getCollisionForce(body1: PhysObject, body2: PhysObject, secondsPassed: Double, elastic: Boolean = true): Vector {
        return if (body1 != body2 && !(body2 is Ground && body1.onTheGround(body2)) && secondsPassed > 0.0)
            getCollisionImpulse(body1, body2, elastic) / secondsPassed * 1.01
        else
            Vector(0.0, 0.0)

    }

    private fun getCollisionImpulse(body1: PhysObject, body2: PhysObject, elastic: Boolean = true): Vector {
        if (body1 == body2 || body1 is Ground || !(body1.intersects(body2) || body2.intersects(body1)) || body2 is Ground && body1.speed.y > 0.0)
            return Vector(0.0, 0.0)

        val centersVector: Vector =
            when (body2) {
                is Ground -> body2.normal
                else -> body2.position - body1.position
            }

        val body1Angle: Double = body1.speed.angle(centersVector)
        val body2Angle: Double = body2.speed.angle(centersVector)

        if (cos(body1Angle) <= 0.0 && cos(body2Angle) >= 0.0)
            return Vector(0.0, 0.0)

        val v1c: Vector = if (body1 is Ground) body1.speed else
            centersVector / centersVector.value() * body1.speed.value() * cos(body1Angle)
        val v1p: Vector = if (body1 is Ground) body1.speed else
            body1.speed - v1c

        val v2c: Vector = if (body2 is Ground) body2.speed else
            centersVector / centersVector.value() * body2.speed.value() * cos(body2Angle)

        if (cos(body1Angle) > 0.0 && cos(body2Angle) > 0.0 && v2c.value() >= v1c.value() ||
            cos(body1Angle) < 0.0 && cos(body2Angle) < 0.0 && v1c.value() >= v2c.value())
            return Vector(0.0, 0.0)

        if (elastic) {
            val newSpeed: Vector = (v1c * (body1.mass - body2.mass) + v2c * 2.0 * body2.mass) /
                    (body1.mass + body2.mass) + v1p

            if (body2 is Ground) newSpeed.y /= 2.0

            println("Collision: $body1 $body2 $newSpeed")

            return (newSpeed - body1.speed) * body1.mass
        }
        else {
            TODO("non-elastic collision")
        }
    }

    private fun checkForGround(body: PhysObject) {
        if (body is Ground)
            return
        if ((body.intersects(ground) || ground.intersects(body)) && abs(body.speed.y) < 0.8) {
            body.speed.y = 0.0
            body.position.y = ground.position.y + body.height / 2.0
        }
    }

    fun compute(secondsPassed: Double) {
        val accelerations = objects.fold(emptyList<Vector>().toMutableList()) { list, body ->
            (list + getAcceleration(body, secondsPassed)).toMutableList() }.toList()

        objects.zip(accelerations).forEach { pair ->
            pair.first.move(secondsPassed, pair.second)
            checkForGround(pair.first)
        }
    }

    suspend fun start() {
        var time = System.currentTimeMillis()
        objects.forEach { checkForGround(it) }
        repeat(Int.MAX_VALUE) {
            compute((System.currentTimeMillis() - time) / 1000.0)
            time = System.currentTimeMillis()
            delay(20)
        }
    }
}