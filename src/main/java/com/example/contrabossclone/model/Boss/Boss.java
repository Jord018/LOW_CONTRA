package com.example.contrabossclone.model.Boss;

import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.Player;
import com.example.contrabossclone.model.MachanicShoot.ShootingStrategy;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap; // (เพิ่ม Import)

public class Boss {

    protected double x, y;
    protected double width, height;
    private int health = 100;
    private int shootCooldown = 0;
    protected Player player;
    protected ShootingStrategy shootingStrategy;
    protected boolean allowShoot;
    protected int score;

    protected transient Image spriteSheet;
    protected Map<String, Rectangle2D[]> animations;
    protected int animationFrame = 0;
    protected int animationTick = 0;
    protected int animationSpeed = 10;
    protected String currentState = "IDLE";
    protected boolean isReadyToRemove = false;


    public Boss(double x, double y, double width, double height, Player player, ShootingStrategy shootingStrategy, boolean allowShoot, int score) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.player = player;
        this.shootingStrategy = shootingStrategy;
        this.allowShoot = allowShoot;
        this.score = score;

        initializeAnimations();
    }

    public Boss(double x, double y, double width, double height, Player player, ShootingStrategy shootingStrategy, int score) {
        this(x, y, width, height, player, shootingStrategy, true, score);
    }

    protected void initializeAnimations() {
        this.animations = new HashMap<>();
    }

    public void update() {

        if (currentState.equals("DEATH")) {
            animationTick++;
            if (animationTick >= animationSpeed) {
                animationTick = 0;
                animationFrame++;

                Rectangle2D[] frames = (animations != null) ? animations.get("DEATH") : null;
                if (frames != null && animationFrame >= frames.length) {
                    isReadyToRemove = true;
                    animationFrame = frames.length - 1;
                }
            }
            return;
        }

        if (shootCooldown > 0) {
            shootCooldown--;
        }

        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            Rectangle2D[] frames = (animations != null) ? animations.get("IDLE") : null;
            if (frames != null) {
                animationFrame = (animationFrame + 1) % frames.length;
            }
        }
    }

    public List<Bullet> shoot(double screenWidth, double screenHeight) {
        if (currentState.equals("DEATH") || !allowShoot) {
            return new ArrayList<>();
        }

        if (shootCooldown <= 0) {
            shootCooldown = 180;

            return shootingStrategy.shoot(x + width / 2, y + height / 2, player, screenWidth, screenHeight, 5.0);
        }
        return new ArrayList<>();
    }

    public void render(GraphicsContext gc) {

        String animKey = currentState;
        Rectangle2D[] frames = (animations != null) ? animations.get(animKey) : null;

        if (spriteSheet != null && frames != null && animationFrame < frames.length) {

            Rectangle2D frame = frames[animationFrame];
            double sX = frame.getMinX(), sY = frame.getMinY();
            double sW = frame.getWidth(), sH = frame.getHeight();

            gc.drawImage(spriteSheet, sX, sY, sW, sH, x, y, width, height);

            if (!currentState.equals("DEATH")) {
                gc.setFill(Color.WHITE);
                gc.fillRect(x, y - 20, width, 10);
                gc.setFill(Color.RED);
                gc.fillRect(x, y - 20, width * (health / 100.0), 10);
            }


        } else {

        }

    }

    public void hit() {
        if (currentState.equals("DEATH")) return;

        health -= 10;
        if (health < 0) health = 0;

        if (health <= 0) {
            currentState = "DEATH";
            animationFrame = 0;
            animationTick = 0;

            Rectangle2D[] deathFrames = (animations != null) ? animations.get("DEATH") : null;

            if (deathFrames == null || deathFrames.length == 0) {
                isReadyToRemove = true;
            }
        }
    }

    public Rectangle2D getBounds() {
        if (currentState.equals("DEATH")) {
            return Rectangle2D.EMPTY;
        }
        return new Rectangle2D(x, y, width, height);
    }

    public boolean isDefeated() {
        return isReadyToRemove;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public int getHealth() { return health; }
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public int getScore() { return score; }
}

