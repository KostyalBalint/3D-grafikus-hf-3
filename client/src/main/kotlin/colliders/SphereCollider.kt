package colliders

import vision.gears.webglmath.Vec3
import kotlin.math.max
import kotlin.math.min

class SphereCollider(val center: Vec3, val radius: Float): Collider() {

    private fun getClosesPoint(collider: BoxCollider): Vec3 {
        return Vec3(
            center.x.coerceIn(min(collider.corner1.x, collider.corner2.x), max(collider.corner1.x, collider.corner2.x)),
            center.y.coerceIn(min(collider.corner1.y, collider.corner2.y), max(collider.corner1.y, collider.corner2.y)),
            center.z.coerceIn(min(collider.corner1.z, collider.corner2.z), max(collider.corner1.z, collider.corner2.z)))
    }

    override fun isColliding(collider: SphereCollider): Boolean {
        //Sphere vs Sphere
        return center.distance(collider.center) < radius + collider.radius
    }

    override fun isColliding(collider: BoxCollider): Boolean {
        //Sphere vs Box
        return getClosesPoint(collider).distance(center) < radius
    }

    override fun getPenetration(collider: SphereCollider): Float {
        return radius + collider.radius - center.distance(collider.center)
    }

    override fun getPenetration(collider: BoxCollider): Float {
        return radius - getClosesPoint(collider).distance(center)
    }

    override fun getCollisionNormal(dir: Vec3): Vec3 {
        return dir.clone().normalize()
    }
}