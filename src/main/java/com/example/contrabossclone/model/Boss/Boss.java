package com.example.contrabossclone.model.Boss;

import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.Player;
import com.example.contrabossclone.model.MachanicShoot.ShootingStrategy;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class Boss {

    protected double x, y;
    private double width, height;
    private int health = 100;
    private double speed = 2;
    private int shootCooldown = 0;
    private Player player;
    private ShootingStrategy shootingStrategy;

    public Boss(double x, double y, double width, double height, Player player, ShootingStrategy shootingStrategy) {
        this.x = x;
        this.y = y;
        this.width = width;   // 👈 2.1 กำหนดค่า
        this.height = height; // 👈 2.2 กำหนดค่า
        this.player = player;
        this.shootingStrategy = shootingStrategy;
    }

    public void update() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
    }

    public List<Bullet> shoot(double screenWidth, double screenHeight) { // <--- เพิ่มพารามิเตอร์
        if (shootCooldown <= 0) {
            shootCooldown = 60;
            return shootingStrategy.shoot(x + width / 2, y + height / 2, player, screenWidth, screenHeight); // <--- ส่งต่อ
        }
        return new ArrayList<>();
    }

    public void render(GraphicsContext gc) {

        // --- คำนวณสัดส่วน ---
        double baseHeight = height * 0.8;   // ฐานสูง 80%
        double baseOffsetY = height * 0.2;  // ฐานขยับลงมา 20%

        double gunWidth = width * 0.2;      // ปืนกว้าง 20%
        double gunHeight = height * 0.4;    // ปืนสูง 40%
        double gunOffsetX = (width - gunWidth) / 2; // ให้ปืนอยู่ตรงกลาง

        double healthBarHeight = 10;      // ให้หลอดเลือดสูง 10px คงที่
        double healthBarOffsetY = 20;     // ให้หลอดเลือดอยู่เหนือหัว 20px

        // --- วาด ---

        // Base
        gc.setFill(Color.web("#A9A9A9"));
        gc.fillRect(x, y + baseOffsetY, width, baseHeight);

        // Gun
        gc.setFill(Color.web("#696969"));
        gc.fillRect(x + gunOffsetX, y, gunWidth, gunHeight);

        // Health bar
        gc.setFill(Color.WHITE);
        gc.fillRect(x, y - healthBarOffsetY, width, healthBarHeight);
        gc.setFill(Color.RED);
        gc.fillRect(x, y - healthBarOffsetY, width * (health / 100.0), healthBarHeight);
    }

    public void hit() {
        health -= 10;
        if (health < 0) {
            health = 0;
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public boolean isDefeated() {
        return health <= 0;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getHealth() {
        return health;
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
