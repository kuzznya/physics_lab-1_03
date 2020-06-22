package com.kuzznya.lab.physics

import com.kuzznya.lab.model.PhysEvent
import com.kuzznya.lab.model.Vector
import com.kuzznya.lab.physics.model.DrawablePhysObject
import com.kuzznya.lab.physics.model.Ground
import com.kuzznya.lab.physics.model.PhysObject
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.cos

class PhysSystem (
    val objects: MutableList<DrawablePhysObject>,
    private val ground: Ground,
    private val elasticCollisions: Boolean = true
) {
    init {
        objects += ground
    }

    private var startTime: Long = 0
    val events: ObservableList<PhysEvent> = FXCollections.observableList(emptyList<PhysEvent>().toMutableList())

    private fun writeToLog(eventType: PhysEvent.Type, body: PhysObject, newSpeed: Vector) {
        events += PhysEvent(System.currentTimeMillis() - startTime,
            eventType,
            body, body.position.copy(), body.speed.copy(), newSpeed)
    }

    private fun getAcceleration(body: PhysObject, secondsPassed: Double): Vector {
        if (body is Ground)
            return Vector(0.0, 0.0)

        val mg: Vector = g * body.mass
        val N: Vector =
            if (body.onTheGround(ground)) mg * cos(mg.angle(ground.normal))
            else Vector(0.0, 0.0)

        val collisionForce: Vector =
            objects.fold(Vector(0.0, 0.0)) { acc, otherBody ->
                acc + getCollisionForce(body, otherBody, secondsPassed, elasticCollisions) }

        return (mg + collisionForce + N) / body.mass
    }

    private fun getCollisionForce(body1: PhysObject, body2: PhysObject,
                                  secondsPassed: Double, elastic: Boolean = true): Vector =
        if (body1 != body2 && !(body2 is Ground && body1.onTheGround(body2)) && secondsPassed > 0.0)
            getCollisionImpulse(body1, body2, elastic) / secondsPassed
        else
            Vector(0.0, 0.0)

    private fun getCollisionImpulse(body1: PhysObject, body2: PhysObject, elastic: Boolean): Vector {
        if (body1 == body2 || body1 is Ground ||
            !(body1.intersects(body2) || body2.intersects(body1)) ||
            body2 is Ground && body1.speed.y > 0.0
        )
            return Vector(0.0, 0.0)

        val centersVector: Vector =
            when (body2) {
                is Ground -> body2.normal * -1.0
                else -> body2.position - body1.position
            }

        val body1Angle: Double = body1.speed.angle(centersVector)
        val body2Angle: Double = body2.speed.angle(centersVector)

        if (cos(body1Angle) <= 0.0 && cos(body2Angle) >= 0.0)
            return Vector(0.0, 0.0)

        val v1c: Vector = centersVector / centersVector.value * body1.speed.value * cos(body1Angle)
        val v1p: Vector = body1.speed - v1c

        val v2c: Vector = if (body2 is Ground) body2.speed else
            centersVector / centersVector.value * body2.speed.value * cos(body2Angle)

        if (cos(body1Angle) > 0.0 && cos(body2Angle) > 0.0 && v2c.value >= v1c.value ||
            cos(body1Angle) < 0.0 && cos(body2Angle) < 0.0 && v1c.value >= v2c.value)
            return Vector(0.0, 0.0)

        var newSpeed: Vector =
            ((v2c - v1c) * (if (elastic) 1.0 - COR_ERROR else COR_ERROR) * body2.mass +
                    v1c * body1.mass + v2c * body2.mass) /
                    (body1.mass + body2.mass) + v1p * (if (elastic || body2 is Ground) 1.0 - COR_ERROR else COR_ERROR)

        if (body2 is Ground && elastic) newSpeed.y *= GROUND_SPEED_CONSUME_COEFFICIENT

        if (body1.speed.value >= MIN_COLLISION_SPEED_TO_LOG || body2.speed.value >= MIN_COLLISION_SPEED_TO_LOG)
            writeToLog(PhysEvent.Type.COLLISION, body1, newSpeed)

        return (newSpeed - body1.speed) * body1.mass
    }

    private fun checkForGround(body: PhysObject) {
        if (body is Ground)
            return
        if ((body.intersects(ground) || ground.intersects(body)) && abs(body.speed.y) < MIN_Y_GROUND_SPEED) {
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
        events.clear()
        var time = System.currentTimeMillis()
        startTime = time
        objects.forEach { checkForGround(it) }
        while (true) {
            compute((System.currentTimeMillis() - time) / 1000.0)
            time = System.currentTimeMillis()
            delay(1)
        }
    }
}