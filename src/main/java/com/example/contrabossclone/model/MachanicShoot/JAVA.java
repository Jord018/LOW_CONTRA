package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JAVA implements ShootingStrategy {
    private Image spriteSheet;
    private Rectangle2D spriteFrame;

    public JAVA(Image spriteSheet, Rectangle2D spriteFrame) {
        this.spriteSheet = spriteSheet;
        this.spriteFrame = spriteFrame;
    }
    private final Random random = new Random();

    @Override
    public List<Bullet> shoot(double x, double y, Player player,
                              double screenWidth, double screenHeight, double bulletSpeed) {

        List<Bullet> bullets = new ArrayList<>();

        double angleDeg = 90 + (random.nextDouble() * 180 - 90);
        double angle = Math.toRadians(angleDeg);

        double vx = Math.cos(angle) * bulletSpeed;
        double vy = Math.sin(angle) * bulletSpeed;

        Bullet bullet = new TrackingDropBullet(
                x, y, vx, vy, Color.ORANGE, screenWidth, screenHeight, player
        );
        bullets.add(bullet);

        return bullets;
    }

    public static class TrackingDropBullet extends Bullet {
        private final Player player;
        private double speed;
        private boolean hasDropped = false;

        public TrackingDropBullet(double x, double y, double velocityX, double velocityY,
                                  Color color, double screenWidth, double screenHeight,
                                  Player player) {
            super(x, y, velocityX, velocityY, color, screenWidth, screenHeight);
            this.speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            this.player = player;
        }
        @Override
        public void update() {
            if (!hasDropped) {
                setX(getX() + getVelocityX());
                setY(getY() + getVelocityY());

                // 🔹 เริ่ม drop เมื่ออยู่ใกล้ player ทาง X
                if (Math.abs(getX() - player.getX()) < 10) { // ปรับค่าตามความต้องการ
                    hasDropped = true;

                    // ตั้งให้ velocityX = 0 และ velocityY = speed ลงตรง
                    setVelocityX(0);
                    setVelocityY(speed);
                }
            } else {
                // ✅ ลงตรงในแนว Y
                setX(getX() + getVelocityX()); // velocityX = 0
                setY(getY() + getVelocityY()); // velocityY = speed
            }
        }



    }



}
