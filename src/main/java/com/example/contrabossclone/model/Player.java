package com.example.contrabossclone.model;

import com.example.contrabossclone.model.Bullet;
import com.example.contrabossclone.model.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.List;

public class Player {

    public enum WeaponType {
        NORMAL,
        MACHINE_GUN,
        SPREAD_GUN,
        LASER,
        FIRE
    }

    private WeaponType weaponType = WeaponType.NORMAL;

    private double x, y;
    private double width = 40, height = 60;
    private double speed = 5;
    private double dx = 0;
    private double velocityY = 0;
    private double gravity = 0.5;
    private boolean onGround = false;
    private boolean isPressingDown = false;

    private int health = 100;
    private int fireRate = 30; // Lower is faster
    private int fireCooldown = 0;
    private boolean isInvincible = false;
    private int invincibilityTimer = 0;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void moveLeft() {
        dx = -speed;
    }

    public void moveRight() {
        dx = speed;
    }

    public void stop() {
        dx = 0;
    }

    public void jump() {
        if (onGround) {
            if (isPressingDown) {
                // Fall through platform
                y += 1;
            } else {
                velocityY = -15; // Jump strength
            }
            onGround = false;
        }
    }

    public void setPressingDown(boolean pressingDown) {
        isPressingDown = pressingDown;
    }

    public void update(List<Platform> platforms) {
        x += dx;

        // Apply gravity
        velocityY += gravity;
        y += velocityY;

        onGround = false;

        // Check for ground collision (bottom of the screen)
        if (y + height > 600) {
            y = 600 - height;
            velocityY = 0;
            onGround = true;
        }

        // Check for platform collisions
        for (Platform platform : platforms) {
            if (getBounds().intersects(platform.getBounds())) {
                // If falling and hit top of platform
                if (velocityY > 0 && y + height - velocityY <= platform.getY()) {
                    y = platform.getY() - height;
                    velocityY = 0;
                    onGround = true;
                }
            }
        }

        if (fireCooldown > 0) {
            fireCooldown--;
        }
        if (invincibilityTimer > 0) {
            invincibilityTimer--;
            if (invincibilityTimer == 0) {
                isInvincible = false;
            }
        }
    }

    public void render(GraphicsContext gc) {
        // Body
        gc.setFill(Color.web("#0000FF"));
        gc.fillRect(x, y, width, height - 20);

        // Head
        gc.setFill(Color.web("#FFDAB9"));
        gc.fillRect(x + 10, y - 20, 20, 20);

        // Health bar
        gc.setFill(Color.WHITE);
        gc.fillRect(x, y - 30, width, 10);
        gc.setFill(Color.GREEN);
        gc.fillRect(x, y - 30, width * (health / 100.0), 10);

        if (isInvincible) {
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(3);
            gc.strokeRect(x - 5, y - 5, width + 10, height + 10);
        }
    }

    public boolean canShoot() {
        return fireCooldown <= 0;
    }

    public List<Bullet> shoot() {
        fireCooldown = fireRate;
        List<Bullet> bullets = new ArrayList<>();
        switch (weaponType) {
            case NORMAL:
                bullets.add(new Bullet(x + width / 2 - 2.5, y, 0, -10, Color.YELLOW));
                break;
            case MACHINE_GUN:
                bullets.add(new Bullet(x + width / 2 - 2.5, y, 0, -10, Color.YELLOW));
                break;
            case SPREAD_GUN:
                bullets.add(new Bullet(x + width / 2 - 2.5, y, -2, -10, Color.YELLOW));
                bullets.add(new Bullet(x + width / 2 - 2.5, y, 0, -10, Color.YELLOW));
                bullets.add(new Bullet(x + width / 2 - 2.5, y, 2, -10, Color.YELLOW));
                break;
            case LASER:
                // For now, laser will be a fast, long bullet
                bullets.add(new Bullet(x + width / 2 - 1, y, 0, -20, Color.RED, 2, 100));
                break;
            case FIRE:
                bullets.add(new Bullet(x + width / 2 - 5, y, 0, -8, Color.ORANGE, 10, 10));
                break;
        }
        return bullets;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
        if (weaponType == WeaponType.MACHINE_GUN) {
            fireRate = 10;
        } else {
            fireRate = 30;
        }
    }



    public void activateBarrier() {
        isInvincible = true;
        invincibilityTimer = 300; // 5 seconds of invincibility (60 frames per second)
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public void hit() {
        if (!isInvincible) {
            health -= 20;
            if (health < 0) {
                health = 0;
            }
        }
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
