package com.kuzznya.lab.service

import com.kuzznya.lab.model.*
import javafx.application.Platform
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

class ModelEngine (
    private val canvas: Canvas
) {
    val objects: MutableList<DrawablePhysObject> = listOf(
        Ground(),
        Trolley(1.0, Point(0.0, 10.0), Vector(1.0, 1.0), 10.0, 10.0, Color.DARKBLUE)
    ).toMutableList()

    val system: PhysSystem = PhysSystem(objects)

    var i = 0

    suspend fun render() = Platform.runLater {
        val ctx = canvas.graphicsContext2D
        ctx.fill = Color.WHITE
        ctx.fillRect(0.0, 0.0, canvas.width, canvas.height)

        val scale = Scale(
            Point(-5.0, -25.0),
            Point(45.0, 25.0),
            0.0,
            min(canvas.width, canvas.height),
            true
        )

        ctx.fill = Color.DARKRED
        val pos0 = scale.transform(Point(0.0, 0.0))
        ctx.fillOval(pos0.x - 2.0, pos0.y - 2.0, 4.0, 4.0)

        system.objects.forEach {
            it.render(ctx, scale)
        }
    }

    suspend fun start() = GlobalScope.launch {
        launch { system.start() }
        repeat(Int.MAX_VALUE) {
            render()
            delay(50)
        }
    }
}