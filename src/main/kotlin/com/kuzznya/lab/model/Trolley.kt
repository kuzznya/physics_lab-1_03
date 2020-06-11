package com.kuzznya.lab.model

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class Trolley (
    mass: Double,
    position: Point,
    speed: Vector,
    var width: Double,
    var height: Double,
    var color: Color
) : DrawablePhysObject(mass, position, speed) {

    override fun intersects(point: Point): Boolean =
        point.x > position.x - width / 2.0 &&
                position.x < position.x + width / 2.0 &&
                point.y > position.y - height / 2.0 &&
                position.y < position.y + height / 2.0

    override fun render(ctx: GraphicsContext) {
        val prevColor = ctx.fill

        ctx.fill = color
        ctx.fillRect(position.x - width / 2.0, position.y - height / 2.0, width, height)

        ctx.fill = prevColor
    }
}