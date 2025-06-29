public class VectorMath {
    public static class Vec3 {
        public double x,y,z;
        public Vec3(double x,double y,double z) {
            this.x = x; this.y= y; this.z = z;
        }
        public Vec3 subtract(Vec3 o) {
            return new Vec3(this.x - o.x,this.y-o.y,this.z-o.z);
        }
        public Vec3 add(Vec3 o) {
            return new Vec3(this.x + o.x,this.y + o.y,this.z + o.z);
        }
        public double dot(Vec3 o) {
            return (this.x * o.x + this.y * o.y + this.z * o.z);
        }
        public Vec3 cross(Vec3 o) {
            return new Vec3(this.y * o.z - this.z * o.y,
            this.z * o.x - this.x * o.z,
            this.x * o.y - this.y * o.x);
        }
    }
    public static class Mat3 {
        public double[][] m;
        public Mat3(double[][] m) {
            this.m = m;
        }
        public Vec3 multiply(Vec3 v) {
            return new Vec3(m[0][0]*v.x + m[0][1]*v.y + m[0][2]*v.z,
            m[1][0]*v.x + m[1][1]*v.y + m[1][2]*v.z,
            m[2][0]*v.x + m[2][1]*v.y + m[2][2]*v.z);
        }
    }
    public static class Plane {
        public double A,B,C,D;
        public Plane(Vec3 p1, Vec3 p2,Vec3 p3) {
            VectorMath.Vec3 v1 = p2.subtract(p1);
            VectorMath.Vec3 v2 = p3.subtract(p1);
            VectorMath.Vec3 normal = v1.cross(v2);
            A = normal.x;
            B = normal.y;
            C = normal.z;
            D = -(A * p1.x + B * p1.y + C * p1.z); 
        }
    }
}
