package com.kuzznya.lab.physics

import com.kuzznya.lab.model.Vector

val g: Vector = Vector(0.0, -9.81)
const val GROUND_SPEED_CONSUME_COEFFICIENT: Double = 0.5
const val COR_ERROR = 0.005
const val MIN_Y_GROUND_SPEED = 0.8
const val MIN_COLLISION_SPEED_TO_LOG = 0.8
