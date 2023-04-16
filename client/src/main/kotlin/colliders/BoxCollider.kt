package colliders

import vision.gears.webglmath.Vec3

class BoxCollider(val corner1: Vec3, val corner2: Vec3): Collider() {

    override fun isColliding(collider: BoxCollider): Boolean {
        //Box vs Box
        return corner1.x < collider.corner2.x && corner2.x > collider.corner1.x &&
                corner1.y < collider.corner2.y && corner2.y > collider.corner1.y &&
                corner1.z < collider.corner2.z && corner2.z > collider.corner1.z
    }

    override fun getPenetration(collider: SphereCollider): Float {
        return collider.getPenetration(this)
    }

    override fun getPenetration(collider: BoxCollider): Float {
        val midPoint = (corner1 + corner2) * 0.5f
        val midPoint2 = (collider.corner1 + collider.corner2) * 0.5f
        val distance = midPoint.distance(midPoint2)
        return (corner2 - corner1).length() + (collider.corner2 - collider.corner1).length() - distance
    }

    override fun getCollisionNormal(ray: Vec3): Vec3 {
        val x = (corner2.x - corner1.x) * 0.5f
        val z = (corner2.z - corner1.z) * 0.5f

        val normal = Vec3(
            if (ray.x > 0) x else -x,
            0f,
            if (ray.z > 0) z else -z)
        normal.normalize()
        return normal
    }

    override fun isColliding(collider: SphereCollider): Boolean {
        //Box vs Sphere
        return collider.isColliding(this)
    }

}
