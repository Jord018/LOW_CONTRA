package com.example.contrabossclone.model.Enemy;

import com.example.contrabossclone.model.Player;
import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.Stage.Platform;
import com.example.contrabossclone.util.ResourceLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

public class Enemy extends Player {
    private boolean alive = true;
    private Player target;
    private int shootCooldown = 0;
    private static final int SHOOT_DELAY = 60;
    private transient Image spriteSheet;
    private Map<String, Rectangle2D[]> animations;
    private int animationFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 10;
    private String currentState = "IDLE";
    private double lastX;
    private double groundLevel;
    private transient Image bulletSpriteSheet;
    private Rectangle2D bulletFrame;

    // ✅ เพิ่ม field สำหรับคะแนน
    private int score;

    // ✅ Constructor ใหม่ (เพิ่ม parameter score)
    public Enemy(double x, double y, Player target, String spriteSheetPath,
                 Image bulletSheet, Rectangle2D bulletFrame, int score) {
        super(x, y);
        this.target = target;
        this.setWidth(70);
        this.setHeight(70);
        this.setSpeed(0.5);
        this.lastX = x;
        this.groundLevel = y + 50;
        this.bulletSpriteSheet = bulletSheet;
        this.bulletFrame = bulletFrame;
        this.score = score; // ✅ กำหนดคะแนน

        this.spriteSheet = ResourceLoader.loadImage(spriteSheetPath);
        initializeAnimations();
    }

    private void initializeAnimations() {
        this.animations = new HashMap<>();
        animations.put("IDLE", new Rectangle2D[]{
                new Rectangle2D(0, 0, 160, 160)
        });
        animations.put("RUN", new Rectangle2D[]{
                new Rectangle2D(160, 0, 160, 160),
                new Rectangle2D(320, 0, 160, 160),
                new Rectangle2D(0, 0, 160, 160)
        });
    }

    @Override
    public void render(GraphicsContext g) {
        if (!alive) return;

        if (spriteSheet != null && animations != null) {
            Rectangle2D[] frames = animations.get(currentState);
            if (frames == null) frames = animations.get("IDLE");

            if (frames != null) {
                if (animationFrame >= frames.length) animationFrame = 0;
                Rectangle2D frame = frames[animationFrame];

                double sX = frame.getMinX(), sY = frame.getMinY();
                double sW = frame.getWidth(), sH = frame.getHeight();

                boolean facingRight = (target.getX() > getX());

                if (facingRight) {
                    g.drawImage(spriteSheet, sX, sY, sW, sH, getX(), getY(), getWidth(), getHeight());
                } else {
                    g.drawImage(spriteSheet, sX, sY, sW, sH, getX() + getWidth(), getY(), -getWidth(), getHeight());
                }
            } else {
                renderFallback(g);
            }
        } else {
            renderFallback(g);
        }
    }

    private void renderFallback(GraphicsContext g) {
        g.setFill(Color.RED);
        g.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }

    @Override
    public void update(List<Platform> platforms, double screenHeight) {
        if (!alive) return;

        super.update(platforms, screenHeight);

        move();

        if (shootCooldown > 0) {
            shootCooldown--;
        }

        if (Math.abs(getX() - lastX) > 0.1) {
            currentState = "RUN";
        } else {
            currentState = "IDLE";
        }
        this.lastX = getX();

        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            Rectangle2D[] frames = animations.get(currentState);
            if (frames != null) {
                animationFrame = (animationFrame + 1) % frames.length;
            }
        }
    }

    private boolean canSeeTarget() {
        if (target == null) return false;
        return Math.abs(getY() - target.getY()) < 50;
    }

    @Override
    public List<Bullet> shoot(double screenWidth, double screenHeight) {
        List<Bullet> newBullets = new ArrayList<>();
        if (target == null || shootCooldown > 0 || !canSeeTarget())
            return newBullets;

        double angle = Math.atan2(target.getY() - getY(), target.getX() - getX());
        double bulletSpeed = 5.0;
        double velocityX = Math.cos(angle) * bulletSpeed;
        double velocityY = Math.sin(angle) * bulletSpeed;

        newBullets.add(new Bullet(getX() + getWidth() / 2, getY() + getHeight() / 2,
                velocityX, velocityY,
                bulletSpriteSheet, bulletFrame,
                10, 10,
                screenWidth, screenHeight));

        shootCooldown = SHOOT_DELAY;
        return newBullets;
    }

    private void move() {
        if (target == null) return;

        double distance = Math.hypot(target.getX() - getX(), target.getY() - getY());
        if (distance > 50) {
            double dx = target.getX() - getX();
            double dy = target.getY() - getY();
            double angle = Math.atan2(dy, dx);

            if (isOnGround()) {
                setX(getX() + Math.cos(angle) * getSpeed());
            }
        }
    }

    public void die() {
        this.alive = false;
        System.out.println("Enemy died at (" + getX() + ", " + getY() + ")");
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(getX(), getY(), getWidth(), getHeight());
    }

    public boolean isOnGround() {
        return getY() + getHeight() >= this.groundLevel + 5;
    }

    // ✅ Getter / Setter สำหรับ score
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
