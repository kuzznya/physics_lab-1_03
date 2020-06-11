package com.kuzznya.lab.service

import com.kuzznya.lab.model.*
import javafx.application.Platform
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ModelEngine (
    private val canvas: Canvas
) {
    val objects: MutableList<DrawablePhysObject> = listOf(
        Ground(),
        Trolley(1.0, Point(-20.0, 10.0), Vector(1.0, 1.0), 10.0, 10.0, Color.RED)
    ).toMutableList()

    val system: PhysSystem = PhysSystem(objects)

    var i = 0

    suspend fun render() = Platform.runLater {
        val ctx = canvas.graphicsContext2D
        ctx.fill = Color.WHITE
        ctx.fillRect(0.0, 0.0, canvas.width, canvas.height)
        system.objects.forEach {
            it.render(ctx,
                Scale(
                    Point(-50.0, -20.0),
                    Point(50.0, 20.0),
                    Point(0.0, canvas.height),
                    Point(canvas.width, 0.0)
                )
            )
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