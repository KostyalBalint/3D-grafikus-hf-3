package move

import GameObject
import WebGL2RenderingContext
import vision.gears.webglmath.Vec3

fun GameObject.ballCollideStrategy(velocity: Vec3, gl: WebGL2RenderingContext) = object : GameObject.Motion(this) {
    override operator fun invoke(
        dt: Float,
        t: Float,
        keysPressed: Set<String>,
        gameObjects: List<GameObject>
    ): Boolean {
        speed = velocity.length()

        gameObjects.forEach { interactor ->
            if (interactor != gameObject){
                if (interactor.collider.isColliding(gameObject.collider)) {
                    val dir = gameObject.position - interactor.position
                    val normal = interactor.collider.getCollisionNormal(dir)
                    val penetration = gameObject.collider.getPenetration(interactor.collider)
                    val correctionForce = normal * penetration * 1.2f
                    velocity.reflect(normal) //Bounce
                    gameObject.position.set(gameObject.position + correctionForce)
                }
            }
        }

        //Drag
        //velocity *= 0.99f
        //if(velocity.length() < 0.01f) velocity.set(0.0f, 0.0f, 0.0f)

        gameObject.position.set(gameObject.position + velocity * dt)
        gameObject.roll += velocity.x * 0.4f * dt
        gameObject.pitch += velocity.z * 0.4f * dt
        return true
    }
}
