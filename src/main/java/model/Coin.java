package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Coin {
    private ImageView shape;
    private Image[] coinImages;
    private int frame = 0;
    private int frameCounter = 0;
    private static final int FRAME_DELAY = 5;
    private boolean isCollected = false; // Flag to indicate if the coin is collected
    private double velocityY = 0; // Vertical velocity for the jump and drop animation
    private static final double JUMP_SPEED = -8; // Upward velocity when collected
    private static final double GRAVITY = 0.5; // Gravity to make the coin fall

    public Coin(double x, double y) {
        // Load the coin animation images
        coinImages = new Image[9];
        for (int i = 1; i <= 9; i++) {
            String path = String.format("/goldCoin/goldCoin%d.png", i);
            System.out.println("Loading coin image: " + path + " from: " + getClass().getResource(path));
            if (getClass().getResource(path) == null) {
                throw new RuntimeException("Coin image not found: " + path);
            }
            coinImages[i - 1] = new Image(getClass().getResource(path).toExternalForm());
        }

        shape = new ImageView(coinImages[0]);
        shape.setFitWidth(20);
        shape.setFitHeight(20);
        shape.setX(x);
        shape.setY(y);
    }

    public ImageView getShape() {
        return shape;
    }

    public void update() {
        if (!isCollected) {
            // Normal animation when not collected
            frameCounter++;
            if (frameCounter >= FRAME_DELAY) {
                frameCounter = 0;
                frame = (frame + 1) % coinImages.length;
                shape.setImage(coinImages[frame]);
            }
        } else {
            // Jump and drop animation when collected
            velocityY += GRAVITY; // Apply gravity
            shape.setY(shape.getY() + velocityY); // Update Y position
        }
    }

    public void collect() {
        if (!isCollected) {
            isCollected = true;
            velocityY = JUMP_SPEED; // Give the coin an upward velocity to "jump"
        }
    }

    public boolean isCollected() {
        return isCollected;
    }

    public boolean isOffScreen() {
        // Check if the coin has fallen off the bottom of the screen (y > 600)
        return shape.getY() > 600;
    }
}