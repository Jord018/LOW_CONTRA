package com.example.contrabossclone.model.MachanicShoot;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class Bullet {

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private double x, y;
    private double width = 5, height = 10;
    private double velocityX, velocityY;
    private Color color;
    private double screenWidth, screenHeight;
    private Image spriteSheet;
    private Rectangle2D spriteFrame;
    private boolean useSprite = false;

    private boolean isExploding = false;
    private boolean isFinished = false;
    private int animationFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 5;

    private static transient Image explosionSpriteSheet;
    private static Rectangle2D[] explosionFrames;


    public Bullet(double x, double y, double velocityX, double velocityY, Color color, double screenWidth, double screenHeight) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.color = color;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.width = 10;
        this.height = 10;
        this.useSprite = false;
    }

    public Bullet(double x, double y, double velocityX, double velocityY, Color color, double width, double height, double screenWidth, double screenHeight) {
        this(x, y, velocityX, velocityY, color, screenWidth, screenHeight);
        this.width = width;
        this.height = height;
    }

    public Bullet(double x, double y, double velocityX, double velocityY,
                  Image spriteSheet, Rectangle2D spriteFrame,
                  double width, double height,
                  double screenWidth, double screenHeight) {
        this(x, y, velocityX, velocityY, Color.TRANSPARENT, width, height, screenWidth, screenHeight);
        this.spriteSheet = spriteSheet;
        this.spriteFrame = spriteFrame;
        this.useSprite = true;
    }

    public static void loadExplosionSprite(Image sheet, Rectangle2D[] frames) {
        explosionSpriteSheet = sheet;
        explosionFrames = frames;
    }

    public void explode() {
        if (isExploding) return;

        this.isExploding = true;
        this.velocityX = 0;
        this.velocityY = 0;
        this.animationFrame = 0;
        this.animationTick = 0;

    }

    public void update() {
        if (isExploding) {
            animationTick++;
            if (animationTick >= animationSpeed) {
                animationTick = 0;
                animationFrame++;
                if (explosionFrames != null && animationFrame >= explosionFrames.length) {
                    isFinished = true;
                }
            }
        } else {
            x += velocityX;
            y += velocityY;
        }
    }

    public void render(GraphicsContext gc) {
        if (isFinished) return;

        if (isExploding) {
            if (explosionSpriteSheet != null && explosionFrames != null && animationFrame < explosionFrames.length) {
                Rectangle2D frame = explosionFrames[animationFrame];
                double sX = frame.getMinX(), sY = frame.getMinY();
                double sW = frame.getWidth(), sH = frame.getHeight();

                double destWidth = 32;
                double destHeight = 32;

                double drawX = this.x + (this.width / 2) - (destWidth / 2);
                double drawY = this.y + (this.height / 2) - (destHeight / 2);

                gc.drawImage(explosionSpriteSheet, sX, sY, sW, sH, x, y, destWidth, destHeight);
            } else {
                gc.setFill(Color.RED);
                gc.fillRect(x, y, 32, 32);
            }
            } else {
            if (useSprite && spriteSheet != null && spriteFrame != null) {
                double sX = spriteFrame.getMinX(), sY = spriteFrame.getMinY();
                double sW = spriteFrame.getWidth(), sH = spriteFrame.getHeight();
                gc.drawImage(spriteSheet, sX, sY, sW, sH, x, y, width, height);
            } else {
                gc.setFill(color != null ? color : Color.MAGENTA);
                gc.fillRect(x, y, width, height);
            }
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isExploding() {
        return isExploding;
    }

    public boolean isOutOfBounds(double width, double height) {
        return x < 0 || x > width || y < 0 || y > height;
    }
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    // Getters
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
    // Setters
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

}
