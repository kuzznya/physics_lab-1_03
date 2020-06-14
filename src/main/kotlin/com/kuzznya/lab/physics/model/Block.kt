package com.kuzznya.lab.physics.model

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.Scale
import com.kuzznya.lab.model.Vector
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.abs

class Block (
    mass: Double,
    position: Point,
    speed: Vector,
    width: Double,
    height: Double,
    var color: Color
) : DrawablePhysObject(mass, position, speed, width, height) {
    override fun render(ctx: GraphicsContext, scale: Scale) {
        ctx.fill = color
        val pos = scale.transform(
            Point(
                position.x - width / 2.0,
                position.y + height / 2.0
            )
        )
        ctx.fillRect(pos.x, pos.y, abs(scale.transformX(width)), abs(scale.transformY(height)))
    }
}