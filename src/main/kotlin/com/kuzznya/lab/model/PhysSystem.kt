package com.kuzznya.lab.model

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PhysSystem (
    val objects: MutableList<DrawablePhysObject>
) {
    fun compute(secondsPassed: Double) {
        objects.forEach { it.move(secondsPassed) }
    }

    suspend fun start() {
        var time = System.currentTimeMillis()
        repeat(Int.MAX_VALUE) {
            compute((System.currentTimeMillis() - time) / 1000.0)
            time = System.currentTimeMillis()
            delay(20)
        }
    }
}