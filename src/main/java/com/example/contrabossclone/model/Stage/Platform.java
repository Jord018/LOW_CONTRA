package com.example.contrabossclone.model.Stage;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Platform {

    private double x, y;
    private double width, height;


    private boolean isSolid;


    public Platform(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isSolid = false;
    }

    public Platform(double x, double y, double width, double height, boolean isSolid) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isSolid = isSolid;
    }

/*
    public void render(GraphicsContext gc) {

        if (isSolid) {
            gc.setFill(Color.DARKSLATEGRAY);
        } else {
            gc.setFill(Color.GRAY);
        }
        gc.fillRect(x, y, width, height);
    }
*/

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public boolean isSolid() {
        return isSolid;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}

