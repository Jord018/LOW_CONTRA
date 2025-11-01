package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color; // ⭐️ (เก็บไว้สำหรับ Fallback)

import java.util.ArrayList;
import java.util.List;

public class AimShoot implements ShootingStrategy {

    // ⭐️ --- (1) เพิ่มตัวแปรสำหรับ Sprite ---
    private Image spriteSheet;
    private Rectangle2D spriteFrame;

    // ⭐️ --- (2) เพิ่ม Constructor ใหม่ ---
    // (Constructor นี้รับ Sprite เข้ามา)
    public AimShoot(Image spriteSheet, Rectangle2D spriteFrame) {
        this.spriteSheet = spriteSheet;
        this.spriteFrame = spriteFrame;
    }

    // --- (3) Constructor เก่า (เผื่อไว้) ---
    // (ถ้าเรียกตัวนี้ กระสุนจะเป็นสีส้ม)
    public AimShoot() {
        this.spriteSheet = null;
        this.spriteFrame = null;
    }

    @Override
    public List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight, double bulletSpeed) {
        List<Bullet> bullets = new ArrayList<>();

        // Logic เดิม: ยิงตรงไปหา Player
        double angle = Math.atan2(player.getY() - y, player.getX() - x);
        double velocityX = Math.cos(angle) * bulletSpeed;
        double velocityY = Math.sin(angle) * bulletSpeed;

        double bulletWidth = 10;
        double bulletHeight = 10;

        // ⭐️ --- (4) ตรวจสอบว่ามี Sprite หรือไม่ ---
        if (spriteSheet != null && spriteFrame != null) {
            // ⭐️ (4.1) ถ้ายิงแบบ Sprite
            bullets.add(new Bullet(x, y, velocityX, velocityY,
                    spriteSheet, spriteFrame,
                    bulletWidth, bulletHeight,
                    screenWidth, screenHeight));
        } else {
            // ⭐️ (4.2) ถ้ายิงแบบสี (Fallback)
            bullets.add(new Bullet(x, y, velocityX, velocityY,
                    Color.RED, // สีแดง
                    bulletWidth, bulletHeight,
                    screenWidth, screenHeight));
        }

        return bullets;
    }
}

