package move

import GameObject
import kotlin.math.PI

fun GameObject.WheelMoveStrategy(stearable: Boolean = false) = object : GameObject.Motion(this) {
    var steeringAngle = 30f

    override operator fun invoke(
        dt: Float,
        t: Float,
        keysPressed: Set<String>,
        gameObjects: List<GameObject>
    ): Boolean {
        if(stearable){
            var turn = 0.0f
            if ("ArrowRight" in keysPressed) turn -= 1.0f
            if ("ArrowLeft" in keysPressed) turn += 1.0f
            val steerDirection = turn * deg2rad(steeringAngle)
            yaw = (steerDirection) % (2 * PI).toFloat()
        }

        val carSpeed = parent?.speed ?: 0f

        //Rotation
        pitch = (pitch + carSpeed / 950.0f) % (2 * PI).toFloat()
        return true
    }

    private fun deg2rad(angle: Float): Float {
        return (angle * PI / 180).toFloat()
    }
}
