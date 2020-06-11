package com.kuzznya.lab.model

import javafx.scene.canvas.GraphicsContext

class ObjectCompound(objects: List<DrawablePhysObject>, speed: Vector) :
    DrawablePhysObject(objects.sumByDouble { it.mass }, Point(
        objects.sumByDouble { it.mass * it.position.x } / objects.sumByDouble { it.mass },
        objects.sumByDouble { it.mass * it.position.y } / objects.sumByDouble { it.mass }
    ), speed) {

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
}