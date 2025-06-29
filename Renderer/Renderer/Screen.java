import java.io.IOException;
import java.util.Random;

public class Screen extends Renderer {
    private Renderer tst;
    private Render3D render3d;
    public  double time = 0;
    public Screen(int width, int height) {
        super(width, height);
        tst = new Renderer(256, 256);
        render3d = new Render3D(width,height);
        Random rand = new Random();
        for ( int i = 0 ; i < (256*256); i++) {
            tst.pixels[i] = rand.nextInt() * (rand.nextInt(20) / 19);
        }
    }
    public static VectorMath.Mat3 lookAt(VectorMath.Vec3 from, VectorMath.Vec3 to, VectorMath.Vec3 upHint) {
        VectorMath.Vec3 forward = normalize(to.subtract(from));     // z axis
        VectorMath.Vec3 right = normalize(upHint.cross(forward));   // x axis
        VectorMath.Vec3 up = forward.cross(right);                  // y axis

        double[][] m = {
            { right.x, right.y, right.z },
            { up.x,    up.y,    up.z },
            { forward.x, forward.y, forward.z } // z axis points forward
        };
        return new VectorMath.Mat3(m);
    }

    private static VectorMath.Vec3 normalize(VectorMath.Vec3 v) {
        double len = Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z);
        return new VectorMath.Vec3(v.x/len, v.y/len, v.z/len);
    }
    public void Render(Game game) {
        try {
            ObjModelLoader.ModelData model = ObjModelLoader.load("male.obj");
            VectorMath.Vec3 min = new VectorMath.Vec3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            VectorMath.Vec3 max = new VectorMath.Vec3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

            for (VectorMath.Vec3 v : model.vertices) {
                min.x = Math.min(min.x, v.x);
                min.y = Math.min(min.y, v.y);
                min.z = Math.min(min.z, v.z);
                max.x = Math.max(max.x, v.x);
                max.y = Math.max(max.y, v.y);
                max.z = Math.max(max.z, v.z);
            }

            VectorMath.Vec3 modelCenter = new VectorMath.Vec3(
                (min.x + max.x) / 2,
                (min.y + max.y) / 2,
                (min.z + max.z) / 2
            );
            for (int i = 0; i < model.vertices.length; i++) {
                model.vertices[i] = model.vertices[i].subtract(modelCenter);
            }
            double radius = 3.0;
            double angle = System.currentTimeMillis() / 1000.0; // radians per second

            double camX = radius * Math.sin(angle);
            double camZ = radius * Math.cos(angle);
            VectorMath.Vec3 cameraPos = new VectorMath.Vec3(camX, 0, camZ);
            
            // long currentTime = System.currentTimeMillis();
            // double angle = currentTime / 1000.0; // 1 full rotation every ~6.28s

            double[][] rotationmatrix = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
            };
            for (int i = 0; i < width * height; i++) {
                pixels[i] = 0;
            }
            VectorMath.Mat3 cameraRot = lookAt(cameraPos, new VectorMath.Vec3(0, 0, 0), new VectorMath.Vec3(0, 1, 0));
            double focalLength = 250.0;
            render3d.Wireframe(
                    cameraRot,
                    model.vertices,
                    cameraPos,
                    focalLength,
                    model.edges,
                    model.surfaces
                );
            draw(render3d, 0 , 0);
        } catch (IOException ex) {
            System.err.println(ex);
        }
         
    }
    
}
