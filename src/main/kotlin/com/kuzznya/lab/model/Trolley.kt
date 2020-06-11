package com.kuzznya.lab.model

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.abs

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

    override fun render(ctx: GraphicsContext, scale: Scale) {
        ctx.fill = color
        val pos = scale.transform(Point(position.x - width / 2.0, position.y - height / 2.0))
//        println(pos)
//        println("${scale.transformX(width)}, ${scale.transformY(height)}")
        ctx.fillRect( pos.x, pos.y, abs(scale.transformX(width)), abs(scale.transformY(height)))
    }
}