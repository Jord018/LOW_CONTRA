package com.example.contrabossclone.model;

import com.example.contrabossclone.model.Bullet;
import com.example.contrabossclone.model.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Boss {

    private double x, y;
    private double width = 100, height = 100;
    private int health = 100;
    private double speed = 2;
    private int shootCooldown = 0;
    private Player player;
    private boolean aimingShot = false;

    public Boss(double x, double y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public void update() {
        x += speed;
        if (x < 0 || x > 800 - width) {
            speed = -speed;
        }

        if (shootCooldown <= 0) {
            shootCooldown = 60; // Shoot every 60 frames
            aimingShot = !aimingShot; // Alternate between straight and aimed shots
        } else {
            shootCooldown--;
        }
    }

    public Bullet shoot() {
        if (shootCooldown == 1) { // To shoot when cooldown is 1, not 0
            if (aimingShot) {
                double playerX = player.getX() + player.getWidth() / 2;
                double playerY = player.getY() + player.getHeight() / 2;
                double bossCenterX = x + width / 2;
                double bossCenterY = y + height;

                double deltaX = playerX - bossCenterX;
                double deltaY = playerY - bossCenterY;
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                double bulletSpeed = 5;
                double velocityX = (deltaX / distance) * bulletSpeed;
                double velocityY = (deltaY / distance) * bulletSpeed;

                return new Bullet(bossCenterX - 2.5, bossCenterY, velocityX, velocityY, Color.RED);
            } else {
                return new Bullet(x + width / 2 - 2.5, y + height, 0, 5, Color.RED);
            }
        }
        return null;
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

    public int getHealth() {
        return health;
    }
}
