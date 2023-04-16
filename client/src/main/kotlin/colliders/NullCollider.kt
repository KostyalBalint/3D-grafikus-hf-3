package colliders

import vision.gears.webglmath.Vec3

class NullCollider: Collider() {
    override fun isColliding(collider: SphereCollider): Boolean {
        return false
    }

    override fun isColliding(collider: BoxCollider): Boolean {
        return false
    }

    override fun getPenetration(collider: SphereCollider): Float {
        return 0f
    }

    override fun getPenetration(collider: BoxCollider): Float {
        return 0f
    }

    override fun getCollisionNormal(dir: Vec3): Vec3 {
        return Vec3(0f, 0f, 0f)
    }
}