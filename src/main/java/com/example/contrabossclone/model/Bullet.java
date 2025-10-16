package com.example.contrabossclone.model;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Bullet {

    private double x, y;
    private double width = 5, height = 10;
    private double velocityX, velocityY;
    private Color color;
    private double screenWidth, screenHeight;

    public Bullet(double x, double y, double velocityX, double velocityY, Color color, double screenWidth, double screenHeight) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.color = color;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public Bullet(double x, double y, double velocityX, double velocityY, Color color, double width, double height, double screenWidth, double screenHeight) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.color = color;
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void update() {
        x += velocityX;
        y += velocityY;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
    }

    public boolean isOutOfBounds(double width, double height) {
        return x < 0 || x > screenWidth || y < 0 || y > screenHeight;
    }
}
