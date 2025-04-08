package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Coin;
import model.Platform;
import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {
    private Player player;
    private List<Platform> platforms = new ArrayList<>();
    private List<Coin> coins = new ArrayList<>();
    private Text scoreText;
    private int score = 0;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        // Set the background color to tan (#D2B48C) on the Pane
        root.setStyle("-fx-background-color: #D2B48C;");

        // Initialize score
        scoreText = new Text("Score: 0");
        scoreText.setX(10);
        scoreText.setY(30);
        root.getChildren().add(scoreText);

        // Create platforms with adjusted spacing
        platforms.add(new Platform(200, 500, 150, 20));
        platforms.add(new Platform(500, 400, 150, 20)); // Adjusted position for usual distance
        root.getChildren().addAll(platforms.stream().map(Platform::getShape).collect(Collectors.toList()));

        // Create coins
        coins.add(new Coin(250, 450));
        coins.add(new Coin(550, 350)); // Adjusted coin position to match new platform
        root.getChildren().addAll(coins.stream().map(Coin::getShape).collect(Collectors.toList()));

        // Create player
        player = new Player(200, 450);
        root.getChildren().add(player.getShape());

        // Handle key presses
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                player.jump();
            }
            if (event.getCode() == KeyCode.LEFT) {
                isLeftPressed = true;
            }
            if (event.getCode() == KeyCode.RIGHT) {
                isRightPressed = true;
            }
        });

        // Handle key releases
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                isLeftPressed = false;
                if (!isRightPressed) {
                    player.stopMoving();
                }
            }
            if (event.getCode() == KeyCode.RIGHT) {
                isRightPressed = false;
                if (!isLeftPressed) {
                    player.stopMoving();
                }
            }
        });

        // Animation timer for game loop
        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update player movement based on key states
                if (isLeftPressed) {
                    player.moveLeft();
                }
                if (isRightPressed) {
                    player.moveRight();
                }

                // Update player physics
                player.update();

                // Check for platform collisions
                for (Platform platform : platforms) {
                    if (player.getShape().getBoundsInParent().intersects(platform.getShape().getBoundsInParent())) {
                        if (player.getVelocityY() > 0) { // Player is falling
                            player.setY(platform.getShape().getY() - player.getShape().getFitHeight());
                            player.setVelocityY(0);
                            player.setJumping(false);
                        }
                    }
                }

                // Keep player on screen
                if (player.getY() > 600) {
                    player.setY(500);
                    player.setVelocityY(0);
                }

                // Update coins
                for (Coin coin : coins) {
                    coin.update();
                }

                // Coin collection
                coins.removeIf(coin -> {
                    if (!coin.isCollected() && coin.getShape().getBoundsInParent().intersects(player.getShape().getBoundsInParent())) {
                        score += 10;
                        scoreText.setText("Score: " + score);
                        coin.collect(); // Trigger the jump and drop animation
                        return false; // Don't remove the coin yet
                    }
                    // Remove the coin if it has fallen off the screen
                    return coin.isCollected() && coin.isOffScreen();
                });
            }
        }.start();

        primaryStage.setTitle("Platformer Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}