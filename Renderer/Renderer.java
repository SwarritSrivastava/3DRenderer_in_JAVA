

public class Renderer {
    public final int width;
    public final int height;
    public final int[] pixels;


    public Renderer(int width, int height) {
        this.height = height;
        this.width = width;
        pixels = new int[width * height];
    }

    public void draw(Renderer renderer, int xOffset, int yOffset) {
        for (int y = 0; y < renderer.height; y++) {
            int yPix = y + yOffset;
            if(yPix < 0 || yPix >= height) {
                continue;
            }
            for(int x = 0; x < renderer.width; x++) {
                int xPix = x +xOffset;
                if(xPix < 0 || xPix >= width) {
                    continue;
                }
                int alpha  = renderer.pixels[x + y * renderer.width];
                if(alpha > 0 ) {
                    pixels[xPix + yPix*width]  = alpha; 
                } 
            }
        }
        
    }
}
