import java.io.*;
import java.util.*;

public class ObjModelLoader {
    public static class ModelData {
        public VectorMath.Vec3[] vertices;
        public int[][] edges;
        public int[][] surfaces;
    }

    public static ModelData load(String path) throws IOException {
        List<VectorMath.Vec3> verts = new ArrayList<>();
        List<int[]> surfaceList = new ArrayList<>();
        Set<String> edgeSet = new HashSet<>();

        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("v ")) {
                String[] parts = line.split("\\s+");
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);
                verts.add(new VectorMath.Vec3(x, y, z));
            } else if (line.startsWith("f ")) {
                String[] parts = line.split("\\s+");
                int[] face = new int[parts.length - 1];
                for (int i = 1; i < parts.length; i++) {
                    String token = parts[i].split("/")[0]; // ignore texture/normal
                    face[i - 1] = Integer.parseInt(token) - 1; // 1-based index
                }

                // Triangulate quad if needed
                if (face.length == 3) {
                    surfaceList.add(new int[] {face[0], face[1], face[2]});
                } else if (face.length == 4) {
                    surfaceList.add(new int[] {face[0], face[1], face[2]});
                    surfaceList.add(new int[] {face[0], face[2], face[3]});
                }

                // Add edges
                for (int i = 0; i < face.length; i++) {
                    int a = face[i];
                    int b = face[(i + 1) % face.length];
                    String key = Math.min(a, b) + ":" + Math.max(a, b);
                    edgeSet.add(key);
                }
            }
        }

        br.close();

        // Convert edges
        List<int[]> edgeList = new ArrayList<>();
        for (String s : edgeSet) {
            String[] parts = s.split(":");
            edgeList.add(new int[] {
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1])
            });
        }

        ModelData data = new ModelData();
        data.vertices = verts.toArray(new VectorMath.Vec3[0]);
        data.edges = edgeList.toArray(new int[0][]);
        data.surfaces = surfaceList.toArray(new int[0][]);

        return data;
    }
}
