package com.kuzznya.lab.model

import com.kuzznya.lab.physics.model.PhysObject
import java.text.DecimalFormat

data class PhysEvent (
    val time: Long,
    val type: Type,
    val body: PhysObject,
    val position: Point,
    val speedBefore: Vector,
    val speedAfter: Vector
) {
    enum class Type {
        COLLISION,
        DESTRUCTION
    }

    override fun toString(): String =
        "[" + { value: Long ->
            val formatter = DecimalFormat()
            formatter.applyPattern("###")
            (value / 1000).toString() + "." + formatter.format(value % 1000)
        }(time) + "] " +
                when(type) {
                    Type.COLLISION -> "COLLISION"
                    Type.DESTRUCTION -> "DESTRUCTION"
                } + ": ${body::class.simpleName} at (${position.x}; ${position.y}); " +
                "speed before: $speedBefore, speed after: $speedAfter\n"
}