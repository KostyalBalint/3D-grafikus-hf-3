package colliders

import vision.gears.webglmath.Vec3

abstract class Collider {

    internal abstract fun isColliding(collider: SphereCollider): Boolean
    internal abstract fun isColliding(collider: BoxCollider): Boolean

    fun isColliding(collider: Collider): Boolean {
        return when(collider) {
            is SphereCollider -> isColliding(collider)
            is BoxCollider -> isColliding(collider)
            is NullCollider -> false
            else -> {
                throw Exception("Collider type not supported")}
        }
    }

    internal abstract fun getPenetration(collider: SphereCollider): Float
    internal abstract fun getPenetration(collider: BoxCollider): Float

    fun getPenetration(collider: Collider): Float{
        return when(collider) {
            is SphereCollider -> getPenetration(collider)
            is BoxCollider -> getPenetration(collider)
            is NullCollider -> 0f
            else -> {
                throw Exception("Collider type not supported")}
        }
    }

    abstract fun getCollisionNormal(dir: Vec3): Vec3
}