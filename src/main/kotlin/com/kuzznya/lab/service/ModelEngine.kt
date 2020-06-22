package com.kuzznya.lab.service

import com.kuzznya.lab.model.*
import com.kuzznya.lab.physics.model.DrawablePhysObject
import com.kuzznya.lab.physics.model.Ground
import com.kuzznya.lab.physics.PhysSystem
import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

class ModelEngine (
    objects: List<DrawablePhysObject>,
    private val canvas: Canvas,
    elasticCollisions: Boolean = true
) {
    private val system: PhysSystem =
        PhysSystem(objects.toMutableList(), Ground(), elasticCollisions)

    val eventLog: ObservableList<PhysEvent>
        get() = system.events

    private fun render() = Platform.runLater {
        val ctx = canvas.graphicsContext2D
        ctx.fill = Color.WHITE
        ctx.fillRect(0.0, 0.0, canvas.width, canvas.height)

        val scale = Scale(
            Point(-100.0, -60.0),
            Point(100.0, 140.0),
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

    val computation = GlobalScope.launch(start = CoroutineStart.LAZY) {
        launch { system.start() }
        while (true) {
            render()
            delay(1)
        }
    }
}