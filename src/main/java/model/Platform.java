package model;

import javafx.scene.shape.Rectangle;

public class Platform {
    private Rectangle shape;
    private double x, y, width, height;

    public Platform(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.shape = new Rectangle(x, y, width, height);
        this.shape.setFill(javafx.scene.paint.Color.GRAY);
    }

    public Rectangle getShape() {
        return shape;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}