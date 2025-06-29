import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Render3D  extends Renderer {
    public Render3D(int width, int height) {
        super(width,height);
    }
    double time = 0;
    public void floor() {
        
        for(int y = 0 ; y < height; y++) {
            double ceiling =  (y - height / 2.0) / height;
            
            if ( ceiling < 0) {
                ceiling = -ceiling;
            }

            double z = 8.0 / ceiling; 
            time += 0.0005; 
            for(int x = 0; x < width; x++){
                double xDepth =  (x - width / 2.0) / height;
                xDepth *= z;
                int xx = (int) (xDepth - time ) & 15;
                int yy = (int) (z + time) & 15;
                pixels[x + y * width] = (xx * 16) | (yy * 16) << 8;
                
            }
        }
    }
    public void waterWave() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dx = x - width / 2;
                double dy = y - height / 2;
                double dist = Math.sqrt(dx * dx + dy * dy);
                int color = (int)(Math.sin(dist / 10.0 + System.currentTimeMillis() / 300.0) * 127 + 128);
                pixels[x + y * width] = color << 8 | color;  // green-blue gradient
            }
        }
    }
    public void crossangle() {
        double slope = 6.0/8.0;
        double inclination = Math.atan(slope);
        inclination = (180.0/Math.PI) * (inclination);
        for (int y = 0; y < height ; y++) {
            for (int x = 0; x < width; x++) {
                int xact = x - (width / 2) ;
                int ycoord1 = (int)((slope * xact) + height / 2);
                int ycoordinv = (int)((-slope * xact) + height / 2);
                if( y == ycoord1 || y == ycoordinv) {
                    pixels[x + y * width] = 0xFFFFFF;
                }
                else {
                    pixels[x + y * width] = 0x000000;
                }                
            }
        }  
        double theta = 0;   
        int ycoord = 0;
        int xcoord = 0;   
        while(theta < 360) {
            double angle = theta * Math.PI / 180.0;
                    if(theta < inclination) {
                        int radius = 100;
                        for(int r = 0 ; r < radius; r++) {
                            ycoord = (int)(Math.sin(angle) * r);
                            xcoord = (int)(Math.cos(angle) * r); 
                            pixels[(xcoord - width/2) + (ycoord + height/2) * width] = 0xFFFFFF;
                        }
                        //draw with radius 100
                    }
                    else if (theta < (180.0 - (inclination)) && theta > inclination) {
                        int radius = 50;
                        for(int r = 0 ; r < radius; r++) {
                            ycoord = (int)(Math.sin(angle) * r);
                            xcoord = (int)(Math.cos(angle) * r); 
                            pixels[(xcoord - width/2) + (ycoord + height/2) * width] = 0xFFFFFF;
                        }
                        //draw with radius 50
                    }
                    else if(theta < (180.0 + inclination) && theta > (180.0 - (inclination))) {
                        int radius = 100;
                        for(int r = 0 ; r < radius; r++) {
                            ycoord = (int)(Math.sin(angle) * r);
                            xcoord = (int)(Math.cos(angle) * r); 
                            pixels[(xcoord - width/2) + (ycoord + height/2) * width] = 0xFFFFFF;
                        }
                        
                        //draw with 100
                    }
                    else if (theta < (360.0 - inclination) && theta > (180.0 + inclination)) {
                        int radius = 50;
                        for(int r = 0 ; r < radius; r++) {
                            ycoord = (int)(Math.sin(angle) * r);
                            xcoord = (int)(Math.cos(angle) * r); 
                            pixels[(xcoord - width/2) + (ycoord + height/2) * width] = 0xFFFFFF;
                        }
                        //draw with 50
                    }
                    else if(theta < 360 && theta > (360.0 - inclination)) {
                        int radius = 100;
                        for(int r = 0 ; r < radius; r++) {
                            ycoord = (int)(Math.sin(angle) * r);
                            xcoord = (int)(Math.cos(angle) * r); 
                            pixels[(xcoord - width/2) + (ycoord + height/2) * width] = 0xFFFFFF;
                        }
                         //draw with 100
                    }
                    theta++;     
        } 
        
    }
    public void drawline(int x0, int y0, int x1, int y1, int color) {
        int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int err = dx + dy, e2;

        while (true) {
            if (x0 >= 0 && x0 < width && y0 >= 0 && y0 < height)
                pixels[x0 + y0 * width] = color;
            if (x0 == x1 && y0 == y1) break;
            e2 = 2 * err;
            if (e2 >= dy) { err += dy; x0 += sx; }
            if (e2 <= dx) { err += dx; y0 += sy; }
        }
    }   
    int[] toScreenCoords(double[] uv) {
        int x = (int)(width / 2 + uv[0]);
        int y = (int)(height / 2 - uv[1]);
        return new int[] {x, y};
    } 
    public List<int[]> getsurface(int i, int j, int[][] surfaces) {
        List<int[]> result = new ArrayList<>();
        for(int[] face : surfaces) {
            boolean foundI = false; boolean foundJ = false;
            for(int v : face) {
                if(v == i) foundI = true;
                if(v == j) foundJ = true;
            }
            if (foundI && foundJ) result.add(face);
        }
        return result;
    }
    public void Wireframe(VectorMath.Mat3 R, VectorMath.Vec3[] N, VectorMath.Vec3 T, double f,int[][] edges, int[][] surfaces) {
         for (int i = 0; i < width * height; i++) {
            pixels[i] = 0;
        } 
        ArrayList<double[]> points = new ArrayList<>();
        for (VectorMath.Vec3 N1 : N) {
            VectorMath.Vec3 camerapoint = R.multiply(N1.subtract(T));
            double u = f * camerapoint.x / camerapoint.z;
            double v = f * camerapoint.y / camerapoint.z;
            double[] point = {u,v};
            points.add(point);
        }
        //for visibility check
        // for (int[] edge : edges) {  //drawing lines without visibility check
        //     int[] a = toScreenCoords(points.get(edge[0]));
        //     int[] b = toScreenCoords(points.get(edge[1]));
        //     drawline(a[0], a[1], b[0], b[1], 0xFFFFFF); // white line
        // }
        for (int[] edge : edges) {  
            double[] a = points.get(edge[0]); // this is point 1
            double[] b = points.get(edge[1]); //this is point 2
            double[] midpoint_2D = {(a[0] + b[0]) / 2 , (a[1] + b[1]) / 2};

            VectorMath.Vec3 a3D = N[edge[0]];
            VectorMath.Vec3 b3D = N[edge[1]];
            
            List<int[]> nativeSurfaces = getsurface(edge[0], edge[1], surfaces);

            List<Double> depths = new ArrayList<>();
            List<Integer> surfaceIDs = new ArrayList<>();
            int p = 0;
            for(int[] surface : nativeSurfaces) {
                VectorMath.Vec3 p1 = N[surface[0]];
                VectorMath.Vec3 p2 = N[surface[1]];
                VectorMath.Vec3 p3 = N[surface[2]];

                VectorMath.Plane plane = new VectorMath.Plane(p1, p2, p3);

                double u = midpoint_2D[0];
                double v = midpoint_2D[1];
                
                double Zc = -plane.D / ((plane.A * u / f) + (plane.B * v / f) + plane.C);
                depths.add(Zc);
                surfaceIDs.add(p);
                p++;
            }  
            double minZ = Double.POSITIVE_INFINITY;
            int minSurfaceIndex = -1;

            for (int i = 0; i < depths.size(); i++) {
                if (depths.get(i) < minZ) {
                    minZ = depths.get(i);
                    minSurfaceIndex = i;
                }
            }

            // Check if min surface index is a native surface (we can use index in original surface list)
            int[] winningSurface = nativeSurfaces.get(minSurfaceIndex);
            boolean isVisible = false;
            for(int[] surface : nativeSurfaces) {
                if(Arrays.equals(surface, winningSurface)) {
                    isVisible = true;
                    break;
                }
            }
            
            if (isVisible) {
                int[] screenA = toScreenCoords(a);
                int[] screenB = toScreenCoords(b);
                drawline(screenA[0], screenA[1], screenB[0], screenB[1], 0x00FF00);
            }  
        }
    }
}
