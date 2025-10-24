package com.example.contrabossclone.model;

import com.example.contrabossclone.model.Bullet;
import com.example.contrabossclone.model.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class Boss {

    protected double x, y;
    private double width = 100, height = 100;
    private int health = 100;
    private double speed = 2;
    private int shootCooldown = 0;
    private Player player;
    private ShootingStrategy shootingStrategy;

    public Boss(double x, double y, Player player, ShootingStrategy shootingStrategy) {
        this.x = x;
        this.y = y;
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
        // Base
        gc.setFill(Color.web("#A9A9A9"));
        gc.fillRect(x, y + 20, width, height - 20);

        // Gun
        gc.setFill(Color.web("#696969"));
        gc.fillRect(x + 40, y, 20, 40);

        // Health bar
        gc.setFill(Color.WHITE);
        gc.fillRect(x, y - 20, width, 10);
        gc.setFill(Color.RED);
        gc.fillRect(x, y - 20, width * (health / 100.0), 10);
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
