package com.example.contrabossclone.model.Enemy;

import com.example.contrabossclone.model.Player;
import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.Stage.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import java.util.List;
import java.util.ArrayList;

// ⭐️ --- (1) เพิ่ม Imports ---
import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

public class Enemy extends Player {
    private boolean alive = true;
    private Player target;
    private int shootCooldown = 0;
    private static final int SHOOT_DELAY = 60; // Frames between shots
    // Bullets are now managed by GameModel

    // ⭐️ --- (2) เพิ่มตัวแปร Animation ---
    private transient Image spriteSheet;
    private Map<String, Rectangle2D[]> animations;
    private int animationFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 10;
    private String currentState = "IDLE";
    private double lastX; // ⭐️ (ใช้เช็คว่ากำลังเดินหรือไม่)
    private double groundLevel; // ⭐️ (ใช้เก็บค่าพื้น)

    // ⭐️ --- (3) เพิ่มตัวแปร Sprite กระสุน ---
    private transient Image bulletSpriteSheet;
    private Rectangle2D bulletFrame;

    // ⭐️ --- (4) อัปเดต Constructor ---
    public Enemy(double x, double y, Player target, String spriteSheetPath, Image bulletSheet, Rectangle2D bulletFrame) {
        super(x, y); // ⭐️ เรียก Player constructor (สำคัญ)
        this.target = target;
        this.setWidth(70);
        this.setHeight(70);
        this.setSpeed(0.5);
        this.lastX = x;
        this.groundLevel = y + 50; // (ค่าเริ่มต้นชั่วคราว)

        // ⭐️ (4.1) เก็บ Sprite กระสุน
        this.bulletSpriteSheet = bulletSheet;
        this.bulletFrame = bulletFrame;

        // ⭐️ (4.2) โหลด Sprite ของ Enemy
        try {
            this.spriteSheet = new Image(getClass().getResourceAsStream(spriteSheetPath));
        } catch (Exception e) {
            System.err.println("Error loading enemy sprite: " + spriteSheetPath);
            this.spriteSheet = null;
        }
        initializeAnimations();
    }

    // ⭐️ --- (5) เพิ่มเมธอดตั้งค่า Animation ---
    private void initializeAnimations() {
        this.animations = new HashMap<>();
        // ⭐️⭐️ (สำคัญ!) ใส่พิกัด Sprite ของ Enemy ที่นี่
        // (นี่คือตัวอย่าง คุณต้องแก้ให้ตรงกับไฟล์รูปของคุณ)
        animations.put("IDLE", new Rectangle2D[]{
                new Rectangle2D(0, 0, 160, 160) // Frame 1
        });
        animations.put("RUN", new Rectangle2D[]{
                new Rectangle2D(160, 0, 160, 160), // Frame 1
                new Rectangle2D(320, 0, 160, 160),  // Frame 2
                new Rectangle2D(0, 0, 160, 160)
        });
    }

    // ⭐️ --- (6) อัปเดต Render ---
    @Override
    public void render(GraphicsContext g) {
        if (!alive) return;

        // --- (6.1) วาด Sprite แทนสี่เหลี่ยมสีแดง ---
        if (spriteSheet != null && animations != null) {
            Rectangle2D[] frames = animations.get(currentState);
            if (frames == null) frames = animations.get("IDLE"); // Fallback

            if (frames != null) {
                if (animationFrame >= frames.length) animationFrame = 0;
                Rectangle2D frame = frames[animationFrame];

                double sX = frame.getMinX(), sY = frame.getMinY();
                double sW = frame.getWidth(), sH = frame.getHeight();

                // (6.2) ตรวจสอบทิศทาง
                boolean facingRight = (target.getX() > getX());

                // (6.3) วาด Sprite (ยืดให้พอดี hitbox 50x50)
                if (facingRight) {
                    g.drawImage(spriteSheet, sX, sY, sW, sH, getX(), getY(), getWidth(), getHeight());
                } else {
                    // กลับด้านรูป
                    g.drawImage(spriteSheet, sX, sY, sW, sH, getX() + getWidth(), getY(), -getWidth(), getHeight());
                }
            } else {
                renderFallback(g);
            }
        } else {
            renderFallback(g);
        }
    }

    // ⭐️ (6.4) เมธอดสำรอง (วาดสี่เหลี่ยมสีแดง)
    private void renderFallback(GraphicsContext g) {
        g.setFill(Color.RED);
        g.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }

    // ⭐️ --- (7) อัปเดต Update ---
    @Override
    public void update(List<Platform> platforms, double screenHeight) {
        if (!alive) return;

        // (7.1) เรียก Player.update() เพื่อให้แรงโน้มถ่วงทำงาน
        // (เราจะเพิกเฉยต่อ dx, dy, หรือ animation ของ Player)
        super.update(platforms, screenHeight);

        // (7.2) เรียก AI
        move();

        // (7.3) จัดการ Cooldown (โค้ดเดิม)
        if (shootCooldown > 0) {
            shootCooldown--;
        }

        // --- (7.4) Logic Animation ของ Enemy ---
        // (เช็คว่า X เปลี่ยนไปหรือไม่)
        if (Math.abs(getX() - lastX) > 0.1) {
            currentState = "RUN";
        } else {
            currentState = "IDLE";
        }
        this.lastX = getX(); // อัปเดต lastX

        // (7.5) ขยับ Frame (เหมือน SecondBoss)
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
        // Simple line of sight check
        return Math.abs(getY() - target.getY()) < 50;
    }

    // ⭐️ --- (8) อัปเดต Shoot ---
    @Override // ⭐️ (FIX 1.1) เพิ่ม Override
    // ⭐️ (FIX 1.2) เพิ่มพารามิเตอร์ (width, height)
    public List<Bullet> shoot(double screenWidth, double screenHeight) {
        List<Bullet> newBullets = new ArrayList<>();
        if (target == null || shootCooldown > 0 || !canSeeTarget())
            return newBullets;

        double angle = Math.atan2(target.getY() - getY(), target.getX() - getX());
        double bulletSpeed = 5.0;
        double velocityX = Math.cos(angle) * bulletSpeed;
        double velocityY = Math.sin(angle) * bulletSpeed;

        // (8.1) ⭐️ เรียก Constructor ใหม่ของ Bullet (แบบ Sprite)
        newBullets.add(new Bullet(getX() + getWidth() / 2, getY() + getHeight() / 2,
                velocityX, velocityY,
                bulletSpriteSheet, bulletFrame,
                10, 10, // ⭐️ ขนาดกระสุน (w, h)
                screenWidth, screenHeight)); // ⭐️ (ส่งต่อ)

        shootCooldown = SHOOT_DELAY;
        return newBullets;
    }

    private void move() {
        if (target == null) return;

        // Simple AI: move towards player if not too close
        double distance = Math.hypot(target.getX() - getX(), target.getY() - getY());
        if (distance > 50) { // Keep some distance
            double dx = target.getX() - getX();
            double dy = target.getY() - getY();
            double angle = Math.atan2(dy, dx);

            // Only move horizontally when on ground
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

    // Bullets are now managed by GameModel

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(getX(), getY(), getWidth(), getHeight());
    }

    // ⭐️ (FIX) ลบ @Override ออก
    public boolean isOnGround() {
        // ⭐️ (FIX 2.2) ใช้ groundLevel ที่เก็บไว้
        return getY() + getHeight() >= this.groundLevel + 5; // (เพิ่ม +5 buffer)
    }

}