package Main;

import GameState.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener {
	//dimensions
	public static int WIDTH = 320;
	public static int HEIGHT = 240;
	public static final int SCALE = 2;

	//Main.Game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	//image
	private BufferedImage image;
	private Graphics2D g;

	//game state manager
	private GameStateManager gameStateManager;

	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();

	}
	public void addNotify() {
		super.addNotify();
		if(thread==null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D)image.getGraphics();
		running = true;
		gameStateManager = new GameStateManager();

	}
	@Override
	public void run() {
		init();
		long start;
		long elapsed;
		long wait;

		while(running) {
			start = System.nanoTime();

			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if (wait < 0) {
				wait = 5;
			}
			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image,0,0,WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}

	private void draw() {
		gameStateManager.draw(g);
	}

	private void update() {
		gameStateManager.update();
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {

	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		gameStateManager.keyPressed(keyEvent);
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		gameStateManager.keyReleased(keyEvent);
	}
}
