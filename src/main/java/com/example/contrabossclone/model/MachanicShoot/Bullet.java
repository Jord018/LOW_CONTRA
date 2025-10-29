package com.example.contrabossclone.model.MachanicShoot;

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
        // ควรใช้ width และ height ที่รับเข้ามา ไม่ใช่ screenWidth/screenHeight
        return x < 0 || x > width || y < 0 || y > height;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Setters
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
