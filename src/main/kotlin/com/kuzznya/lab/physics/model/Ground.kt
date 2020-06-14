package com.kuzznya.lab.physics.model

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.Scale
import com.kuzznya.lab.model.Vector
import javafx.scene.canvas.GraphicsContext

class Ground : DrawablePhysObject(
    6E24,
    Point(0.0, 0.0),
    Vector(0.0, 0.0),
    10000.0,
    1.0
) {

    override var speed: Vector
        get() = Vector(0.0, 0.0)
        set(_) {}

    override fun move(secondsPassed: Double) {}

    override fun move(secondsPassed: Double, acceleration: Vector) {}

    override fun intersects(point: Point): Boolean =
        point.y < position.y

    override fun intersects(body2: PhysObject): Boolean =
        (body2.position.y - body2.height / 2.0) <= position.y

    override fun render(ctx: GraphicsContext, scale: Scale) {
        val pos0 = scale.transform(position)
        ctx.strokeLine(0.0, pos0.y, ctx.canvas.width, pos0.y)
    }

    override fun onTheGround(ground: Ground): Boolean = true

    val normal: Vector
        get() = Vector(0.0, 1.0)
}