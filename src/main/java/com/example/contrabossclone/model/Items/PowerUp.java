package com.example.contrabossclone.model.Items;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class PowerUp {

    public enum PowerUpType {
        MACHINE_GUN,
        SPREAD_GUN,
        LASER,
        FIRE,
        BARRIER
    }

    private double x, y;
    private double width = 30, height = 30;
    private PowerUpType type;

    private transient Image spriteSheet;
    private Rectangle2D spriteFrame;

    public PowerUp(double x, double y, PowerUpType type,
                   Image spriteSheet, Rectangle2D spriteFrame,
                   double width, double height) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.spriteSheet = spriteSheet;
        this.spriteFrame = spriteFrame;
        this.width = width;
        this.height = height;
    }

    public void render(GraphicsContext gc) {
        if (spriteSheet != null && spriteFrame != null) {
            double sX = spriteFrame.getMinX();
            double sY = spriteFrame.getMinY();
            double sW = spriteFrame.getWidth();
            double sH = spriteFrame.getHeight();
            gc.drawImage(spriteSheet, sX, sY, sW, sH, x, y, width, height);
        } else {
            gc.setFill(Color.GOLD);
            gc.fillRect(x, y, width, height);
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public PowerUpType getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
