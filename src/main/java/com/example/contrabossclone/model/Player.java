package com.example.contrabossclone.model;

import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.Stage.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;


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

    public double getSpeed() {
        return speed;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }

    private double speed = 5;
    private double dx = 0;
    private double velocityY = 0;
    private double gravity = 0.5;
    private boolean onGround = false;
    private boolean isPressingDown = false;
    private double aimAngle = 90.0;

    private int maxHealth = 100;
    private static final Logger logger = LogManager.getLogger(Player.class);

    public int getScore() {
        logger.info("Player score: " + score);
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int score = 0;
    private int health = maxHealth;

    public void setLives(int lives) {
        this.lives = lives;
    }

    private int lives = 3;
    private int fireRate = 30; // Lower is faster
    private int fireCooldown = 0;
    private boolean isInvincible = false;
    private int invincibilityTimer = 0;
    private double respawnX, respawnY;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.respawnX = x;
        this.respawnY = 10;
    }

    public void moveLeft() {
        dx = -speed;
        logger.debug("Player moved left");
    }

    public void moveRight() {
        dx = speed;
        logger.debug("Player moved right");
    }

    public void stop() {
        dx = 0;
    }

    public void jump() {
        if (onGround) {
            if (isPressingDown) {
                // Fall through platform
                y += 1;
                logger.debug("Player fell through platform");
            } else {
                velocityY = -15; // Jump strength
                logger.debug("Player jumped");
            }
            onGround = false;
        }
    }

    public void setPressingDown(boolean pressingDown) {
        isPressingDown = pressingDown;
    }

    public void setRespawnPosition(double x, double y) {
        this.respawnX = x;
        this.respawnY = y;
    }

    public void update(List<Platform> platforms, double screenHeight) {
        x += dx;

        // Apply gravity
        velocityY += gravity;
        y += velocityY;

        onGround = false;

        // Check for ground collision (bottom of the screen)
        if (y + height > screenHeight) {
            y = screenHeight - height;
            velocityY = 0;
            onGround = true;
            logger.info("Player landed on ground");
        }

        // Check for platform collisions
        for (Platform platform : platforms) {
            if (getBounds().intersects(platform.getBounds())) {
                // If falling and hit top of platform
                if (velocityY > 0 && y + height - velocityY <= platform.getY()) {
                    y = platform.getY() - height;
                    velocityY = 0;
                    onGround = true;
                    logger.info("Player landed on platform");
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
        logger.trace("Player position updated to " + x + " " + y);
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

    public List<Bullet> shoot(double screenWidth, double screenHeight) {
        fireCooldown = fireRate;
        List<Bullet> bullets = new ArrayList<>();
        double bulletSpeed = 10;
        double velocityX = Math.cos(Math.toRadians(aimAngle)) * bulletSpeed;
        double velocityY = -Math.sin(Math.toRadians(aimAngle)) * bulletSpeed;

        switch (weaponType) {
            case NORMAL:
                logger.info("Weapon type NORMAL");
                bullets.add(new Bullet(x + width / 2 - 2.5, y, velocityX, velocityY, Color.YELLOW, screenWidth, screenHeight));
                break;
            case MACHINE_GUN:
                logger.info("Weapon type MACHINE_GUN");
                bullets.add(new Bullet(x + width / 2 - 2.5, y, velocityX, velocityY, Color.YELLOW, screenWidth, screenHeight));
                break;
            case SPREAD_GUN:
                logger.info("Weapon type SPREAD_GUN");
                double angle1 = aimAngle - 15;
                double angle2 = aimAngle + 15;
                double velocityX1 = Math.cos(Math.toRadians(angle1)) * bulletSpeed;
                double velocityY1 = -Math.sin(Math.toRadians(angle1)) * bulletSpeed;
                double velocityX2 = Math.cos(Math.toRadians(angle2)) * bulletSpeed;
                double velocityY2 = -Math.sin(Math.toRadians(angle2)) * bulletSpeed;
                bullets.add(new Bullet(x + width / 2 - 2.5, y, velocityX1, velocityY1, Color.YELLOW, screenWidth, screenHeight));
                bullets.add(new Bullet(x + width / 2 - 2.5, y, velocityX, velocityY, Color.YELLOW, screenWidth, screenHeight));
                bullets.add(new Bullet(x + width / 2 - 2.5, y, velocityX2, velocityY2, Color.YELLOW, screenWidth, screenHeight));
                break;
            case LASER:
                logger.info("Weapon type LASER");
                // For now, laser will be a fast, long bullet
                bullets.add(new Bullet(x + width / 2 - 1, y, velocityX * 2, velocityY * 2, Color.RED, 2, 100, screenWidth, screenHeight));
                break;
            case FIRE:
                logger.info("Weapon type FIRE");
                bullets.add(new Bullet(x + width / 2 - 5, y, velocityX, velocityY, Color.ORANGE, 10, 10, screenWidth, screenHeight));
                break;
        }
        logger.debug("Player fired bullet: " + bullets.size());
        return bullets;

    }

    public void setAimAngle(double aimAngle) {
        this.aimAngle = aimAngle;
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
            if (health <= 0) {
                lives--;
                if (lives > 0) {
                    respawn();
                } else {
                    health = 0;
                }
            }
        }
        logger.info("Player hit");
    }

    public void respawn() {
        health = maxHealth;
        x = respawnX;
        y = respawnY;
        isInvincible = true;
        invincibilityTimer = 180; // 3 seconds of invincibility after respawning
        logger.info("Player respawned");
    }

    public boolean isDefeated() {
        return lives <= 0;
    }

    public int getLives() {
        return lives;
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
