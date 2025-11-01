package com.example.contrabossclone.model.MachanicShoot;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image; // ⭐️ เพิ่ม
import javafx.scene.paint.Color;


public class Bullet {

    private double x, y;
    private double width = 5, height = 5;
    private double velocityX, velocityY;
    private Color color; // ⭐️ (เก็บไว้สำหรับ bullet แบบเก่า)
    private double screenWidth, screenHeight;

    // ⭐️ --- (1) เพิ่มตัวแปรสำหรับ Sprite ---
    private transient Image spriteSheet;
    private Rectangle2D spriteFrame;
    private boolean useSprite = false; // ⭐️ Flag สำหรับเลือกว่าจะวาดสี หรือวาดรูป

    // --- Constructor 1 (แบบเก่า - ใช้สี) ---
    public Bullet(double x, double y, double velocityX, double velocityY, Color color, double screenWidth, double screenHeight) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.color = color;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.useSprite = false; // ⭐️
    }

    // --- Constructor 2 (แบบเก่า - ใช้สี + กำหนดขนาด) ---
    public Bullet(double x, double y, double velocityX, double velocityY, Color color, double width, double height, double screenWidth, double screenHeight) {
        this(x, y, velocityX, velocityY, color, screenWidth, screenHeight); // ⭐️ เรียกตัวบน
        this.width = width;
        this.height = height;
        this.useSprite = false; // ⭐️
    }

    // ⭐️ --- (2) เพิ่ม Constructor 3 (แบบใหม่ - ใช้ Sprite) ---
    public Bullet(double x, double y, double velocityX, double velocityY,
                  Image spriteSheet, Rectangle2D spriteFrame,
                  double width, double height,
                  double screenWidth, double screenHeight) {

        // ⭐️ เรียก Constructor ตัวบน (ส่ง Color.TRANSPARENT ไปเป็น placeholder)
        this(x, y, velocityX, velocityY, Color.TRANSPARENT, width, height, screenWidth, screenHeight);

        // ⭐️ ตั้งค่า Sprite
        this.spriteSheet = spriteSheet;
        this.spriteFrame = spriteFrame;
        this.useSprite = true; // ⭐️⭐️ บอกให้ render() วาดรูป
    }


    public void update() {
        x += velocityX;
        y += velocityY;
    }

    // ⭐️ --- (3) อัปเดตเมธอด render() ---
    public void render(GraphicsContext gc) {
        if (useSprite && spriteSheet != null && spriteFrame != null) {
            // ⭐️ --- 3.1: ถ้าใช้ Sprite ---
            double sX = spriteFrame.getMinX();
            double sY = spriteFrame.getMinY();
            double sW = spriteFrame.getWidth();
            double sH = spriteFrame.getHeight();

            // วาด Sprite (sX,sY,sW,sH) ลงบนตำแหน่ง (x,y) โดยยืดให้มีขนาด (width, height)
            gc.drawImage(spriteSheet, sX, sY, sW, sH, x, y, width, height);

        } else {
            // ⭐️ --- 3.2: ถ้าไม่ใช้ Sprite (โค้ดเดิม) ---
            gc.setFill(color);
            gc.fillRect(x, y, width, height);
        }
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

