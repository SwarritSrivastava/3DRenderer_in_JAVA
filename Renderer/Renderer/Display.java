
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class Display extends Canvas implements Runnable {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "shitpost";

    private Thread thread;
    private Renderer renderer;
    private Game game;
    private boolean running = false;
    private Screen screen;
    private BufferedImage img;
    private int[] pixels;
    public Display(){
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        screen = new Screen(WIDTH, HEIGHT);
        game = new Game();
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
    }

    private void start() { 
        if(running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() { 
        if(!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void run() {
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();
        double SecondsPerTick = 1 / 60.0;
        boolean ticked = false; 
        int tickcount = 0;
        while (running) {
            long currentime = System.nanoTime();
            long passedTime = currentime - previousTime;
            previousTime = currentime;
            unprocessedSeconds += passedTime / 1000000000.0;

            while(unprocessedSeconds > SecondsPerTick) {
                tick();
                unprocessedSeconds -= SecondsPerTick;
                ticked = true;
                tickcount ++;
                if(tickcount % 60 == 0) {
                    System.out.println("FPS : " + frames);
                    previousTime += 1000;
                    frames = 0;
                }
            }
            if(ticked) {
                render();
                frames++;
            }
            render();
            frames++;
         }

    }
    private void tick() {
        game.tick();
    }
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }
        screen.Render(game);
        for(int i = 0; i < WIDTH*HEIGHT; i++) {
            pixels[i] = screen.pixels[i];
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(img,0,0, WIDTH, HEIGHT, null);
        g.dispose();
        bs.show();
        tick();
     }
    public static void main(String[] args) {
        Display game = new Display();
        JFrame frame = new JFrame();
        frame.add(game);
        frame.pack();
        frame.setTitle(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);

        game.start();
    }
}
