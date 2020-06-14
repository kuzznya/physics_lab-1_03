package com.kuzznya.lab.physics.model

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.Scale
import com.kuzznya.lab.model.Vector
import javafx.scene.canvas.GraphicsContext

class ObjectCompound(objects: List<DrawablePhysObject>, speed: Vector) :
    DrawablePhysObject(objects.sumByDouble { it.mass }, Point(
        objects.sumByDouble { it.mass * it.position.x } / objects.sumByDouble { it.mass },
        objects.sumByDouble { it.mass * it.position.y } / objects.sumByDouble { it.mass }
    ), speed,
        objects.maxBy { it.position.x + it.width / 2.0 }!!
            .let {it.position.x + it.width / 2.0}
                - objects.minBy { it.position.x - it.width / 2.0 }!!
            .let {it.position.x - it.width / 2.0},
        objects.maxBy { it.position.y + it.height / 2.0 }!!
            .let {it.position.y + it.height / 2.0}
                - objects.minBy { it.position.y - it.height / 2.0 }!!
            .let {it.position.y - it.height / 2.0}) {

    private lateinit var objects: List<DrawablePhysObject>

    init {
        objects.forEach { it.speed = super.speed }
    }

    fun centerOfMass(): Point = Point(
        objects.sumByDouble { it.mass * it.position.x } / super.mass,
        objects.sumByDouble { it.mass * it.position.y } / super.mass
    )

    override fun move(secondsPassed: Double) {
        super.move(secondsPassed)
        objects.forEach { it.position = super.position; it.speed = super.speed }
    }

    override fun move(secondsPassed: Double, acceleration: Vector) {
        super.move(secondsPassed, acceleration)
        objects.forEach { it.position = super.position; it.speed = super.speed }
    }

    override fun intersects(point: Point): Boolean {
        objects.forEach { if (it.intersects(point)) return true }
        return false
    }

    override fun render(ctx: GraphicsContext, scale: Scale) =
        objects.forEach { it.render(ctx, scale) }

    override fun onTheGround(ground: Ground): Boolean = objects.any { it.onTheGround(ground) }
}