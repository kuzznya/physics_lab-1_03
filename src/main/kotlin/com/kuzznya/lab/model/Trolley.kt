package com.kuzznya.lab.model

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.abs

class Trolley (
    mass: Double,
    position: Point,
    speed: Vector,
    width: Double,
    height: Double,
    var color: Color
) : DrawablePhysObject(mass, position, speed, width, height) {
    override fun render(ctx: GraphicsContext, scale: Scale) {
        ctx.fill = color
        val pos = scale.transform(Point(position.x - width / 2.0, position.y))
        ctx.fillRect(pos.x, pos.y, abs(scale.transformX(width)), abs(scale.transformY(height)))
    }

    override fun onTheGround(ground: Ground): Boolean =
        (position.y - height / 2.0) - ground.position.y < 0.1
}