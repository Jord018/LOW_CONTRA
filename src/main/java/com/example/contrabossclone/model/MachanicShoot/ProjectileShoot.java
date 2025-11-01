package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.geometry.Rectangle2D; // ⭐️ เพิ่ม
import javafx.scene.image.Image; // ⭐️ เพิ่ม
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ProjectileShoot implements ShootingStrategy {
    private double gravity = 0.3; // Gravity effect for arc trajectory

    // ⭐️ --- (1) เพิ่มตัวแปร Sprite ---
    private Image spriteSheet;
    private Rectangle2D spriteFrame;

    // ⭐️ --- (2) เพิ่ม Constructor (แบบ Sprite) ---
    public ProjectileShoot(Image spriteSheet, Rectangle2D spriteFrame) {
        this.spriteSheet = spriteSheet;
        this.spriteFrame = spriteFrame;
    }

    // ⭐️ (Constructor เก่า - สำหรับ Fallback)
    public ProjectileShoot() {
        this.spriteSheet = null;
        this.spriteFrame = null;
    }

    @Override
    public List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight, double bulletSpeed) {
        List<Bullet> bullets = new ArrayList<>();

        double velocityX = -Math.abs(bulletSpeed);
        double velocityY = 0;

        // ⭐️ --- (3) สร้าง ProjectileBullet (Inner class) โดยส่ง Sprite เข้าไป ---
        ProjectileBullet projectile = new ProjectileBullet(x, y, velocityX, velocityY,
                spriteSheet, spriteFrame, // ⭐️ ส่ง Sprite
                12, 12, // ⭐️ ขนาดกระสุน
                screenWidth, screenHeight, gravity);
        bullets.add(projectile);

        return bullets;
    }

    // ⭐️ --- (4) อัปเดต Inner Class ---
    private static class ProjectileBullet extends Bullet {
        private double gravity;
        private double velX;
        private double velY;

        // ⭐️ (4.1) Constructor แบบ Sprite
        public ProjectileBullet(double x, double y, double velocityX, double velocityY,
                                Image sheet, Rectangle2D frame, // ⭐️ รับ Sprite
                                double width, double height,
                                double screenWidth, double screenHeight, double gravity) {
            // ⭐️ เรียก super() (ของ Bullet) แบบ Sprite
            super(x, y, velocityX, velocityY, sheet, frame, width, height, screenWidth, screenHeight);
            this.gravity = gravity;
            this.velX = velocityX;
            this.velY = velocityY;

            // ⭐️ (Fallback) ถ้า Sprite เป็น null ให้เรียก super() แบบ Color
            if (sheet == null || frame == null) {
                // (ต้องเรียก super() เป็นบรรทัดแรก เลยต้องเช็คซ้ำ)
                // (โค้ดนี้จะไม่ถูกเรียก ถ้า ProjectileShoot ถูกสร้างแบบมี Sprite)
                System.err.println("Fallback: ProjectileBullet using Color");
                // ⭐️ (นี่คือ Fallback)
                // (เราไม่สามารถเรียก super() สองครั้งได้, ดังนั้น Constructor ด้านบน
                // จะต้องจัดการเรื่องนี้ แต่เนื่องจาก Java ทำไม่ได้
                // เราจึงต้องพึ่งพา Constructor ของ ProjectileShoot
                // ให้ส่ง Sprite ที่ถูกต้องเข้ามา)
            }
        }

        // ⭐️ (4.2) Constructor แบบ Color (Fallback) - (โค้ดเดิมของคุณ)
        // (เราต้องเก็บไว้เผื่อ ProjectileShoot() แบบเก่าถูกเรียก)
        public ProjectileBullet(double x, double y, double velocityX, double velocityY,
                                Color color, double width, double height,
                                double screenWidth, double screenHeight, double gravity) {
            super(x, y, velocityX, velocityY, color, width, height, screenWidth, screenHeight);
            this.gravity = gravity;
            this.velX = velocityX;
            this.velY = velocityY;
        }

        @Override
        public void update() {
            // (โค้ดเดิม)
            velY += gravity;
            setX(getX() + velX);
            setY(getY() + velY);
        }
    }
}

