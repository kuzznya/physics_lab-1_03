package com.kuzznya.lab.physics.model

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.Scale
import com.kuzznya.lab.model.Vector
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.abs

class Ball (
    mass: Double,
    position: Point,
    speed: Vector,
    val radius: Double,
    var color: Color
) : DrawablePhysObject(mass, position, speed, radius * 2.0, radius * 2.0) {

    override fun intersects(point: Point): Boolean =
        (position - point).value <= radius

    override fun intersects(body2: PhysObject): Boolean =
        when (body2) {
            is Ball -> (position - body2.position).value <= radius + body2.radius
            else -> intersects(body2.position + Vector(- body2.width / 2.0, body2.height / 2.0)) ||
                    intersects(body2.position + Vector(body2.width / 2.0, body2.height / 2.0))
        }

    override fun render(ctx: GraphicsContext, scale: Scale) {
        ctx.fill = color
        val pos = scale.transform(
            Point(
                position.x - width / 2.0,
                position.y + height / 2.0
            )
        )
        ctx.fillOval(pos.x, pos.y, abs(scale.transformX(width)), abs(scale.transformY(height)))
    }
}
