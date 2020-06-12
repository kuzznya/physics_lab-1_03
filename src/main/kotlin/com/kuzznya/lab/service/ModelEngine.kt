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
        Trolley(1.0, Point(0.0, 10.0), Vector(5.0, 20.0), 5.0, 5.0, Color.DARKBLUE),
        Trolley(5.0, Point(50.0, 15.0), Vector(-10.0, 20.0), 10.0, 10.0, Color.DARKRED)
    ).toMutableList()

    val system: PhysSystem = PhysSystem(objects, Ground())

    suspend fun render() = Platform.runLater {
        val ctx = canvas.graphicsContext2D
        ctx.fill = Color.WHITE
        ctx.fillRect(0.0, 0.0, canvas.width, canvas.height)

        val scale = Scale(
            Point(-50.0, -10.0),
            Point(50.0, 90.0),
            0.0,
            min(canvas.width, canvas.height),
            true
        )

        ctx.fill = Color.BLACK
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