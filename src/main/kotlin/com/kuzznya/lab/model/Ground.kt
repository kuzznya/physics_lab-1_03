package com.kuzznya.lab.model

import javafx.scene.canvas.GraphicsContext

class Ground : DrawablePhysObject(6E24, Point(0.0, 0.0), Vector(0.0, 0.0), 10000.0, 1.0) {

    override var speed: Vector
        get() = Vector(0.0, 0.0)
        set(_) {}

    override fun move(secondsPassed: Double) {}

    override fun move(secondsPassed: Double, acceleration: Vector) {}

    override fun intersects(point: Point): Boolean =
        point.y < super.position.y

    override fun render(ctx: GraphicsContext, scale: Scale) {
        val pos0 = scale.transform(super.position)
        ctx.strokeLine(0.0, pos0.y, ctx.canvas.width, pos0.y)
    }

    override fun onTheGround(ground: Ground): Boolean = true
}