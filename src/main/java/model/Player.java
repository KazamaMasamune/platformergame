package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {
    private ImageView shape;
    private Image[] runImages;
    private Image[] jumpImages;
    private int runFrame = 0;
    private int jumpFrame = 0;
    private double x, y;
    private double velocityY;
    private boolean isJumping;
    private boolean isMoving;
    private static final double GRAVITY = 0.5;
    private static final double JUMP_SPEED = -12;
    private static final int FRAME_DELAY = 5;
    private int frameCounter = 0;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;

        // Load the run animation images from the Player/Run subfolder (13 images: Run_000.png to Run_012.png)
        runImages = new Image[13];
        for (int i = 0; i < 13; i++) {
            String path = String.format("/Player/Run/Run_%03d.png", i);
            System.out.println("Loading run image: " + path + " from: " + getClass().getResource(path));
            if (getClass().getResource(path) == null) {
                throw new RuntimeException("Run image not found: " + path);
            }
            runImages[i] = new Image(getClass().getResource(path).toExternalForm());
        }

        // Load the jump animation images from the Player/Jump subfolder (3 images: Jump_000.png to Jump_002.png)
        jumpImages = new Image[3];
        for (int i = 0; i < 3; i++) {
            String path = String.format("/Player/Jump/Jump_%03d.png", i);
            System.out.println("Loading jump image: " + path + " from: " + getClass().getResource(path));
            if (getClass().getResource(path) == null) {
                throw new RuntimeException("Jump image not found: " + path);
            }
            jumpImages[i] = new Image(getClass().getResource(path).toExternalForm());
        }

        // Use Player/Run/Run_000.png as the default image
        System.out.println("Loading default player image (Run_000.png) from: " + getClass().getResource("/Player/Run/Run_000.png"));
        if (getClass().getResource("/Player/Run/Run_000.png") == null) {
            throw new RuntimeException("Default player image (Run_000.png) not found in classpath");
        }
        Image playerImage = new Image(getClass().getResource("/Player/Run/Run_000.png").toExternalForm());
        this.shape = new ImageView(playerImage);
        this.shape.setFitWidth(30);
        this.shape.setFitHeight(40);
        this.shape.setX(x);
        this.shape.setY(y);

        this.velocityY = 0;
        this.isJumping = false;
        this.isMoving = false;
    }

    public void moveLeft() {
        x -= 5;
        shape.setX(x);
        isMoving = true;
        shape.setScaleX(-1);
    }

    public void moveRight() {
        x += 5;
        shape.setX(x);
        isMoving = true;
        shape.setScaleX(1);
    }

    public void stopMoving() {
        isMoving = false;
        shape.setImage(new Image(getClass().getResource("/Player/Run/Run_000.png").toExternalForm()));
    }

    public void jump() {
        if (!isJumping) {
            velocityY = JUMP_SPEED;
            isJumping = true;
            jumpFrame = 0;
        }
    }

    public void update() {
        velocityY += GRAVITY;
        y += velocityY;
        shape.setY(y);

        frameCounter++;
        if (frameCounter >= FRAME_DELAY) {
            frameCounter = 0;

            if (isJumping) {
                if (jumpFrame < jumpImages.length) {
                    shape.setImage(jumpImages[jumpFrame]);
                    jumpFrame++;
                }
            } else if (isMoving) {
                runFrame = (runFrame + 1) % runImages.length;
                shape.setImage(runImages[runFrame]);
            }
        }
    }

    public ImageView getShape() {
        return shape;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        shape.setY(y);
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }
}