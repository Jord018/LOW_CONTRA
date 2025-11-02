package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JAVA implements ShootingStrategy {

    private final Random random = new Random();

    @Override
    public List<Bullet> shoot(double x, double y, Player player,
                              double screenWidth, double screenHeight, double bulletSpeed) {

        List<Bullet> bullets = new ArrayList<>();

        // 🔸 ยิงสุ่มมุม (ทุกทิศได้ แต่เน้นลงล่าง)
        double angleDeg = 90 + (random.nextDouble() * 180 - 90); // ยิงลงกว้าง ๆ
        double angle = Math.toRadians(angleDeg);

        double vx = Math.cos(angle) * bulletSpeed;
        double vy = Math.sin(angle) * bulletSpeed;

        Bullet bullet = new FloatingBullet(
                x, y, vx, vy, Color.ORANGE, screenWidth, screenHeight
        );
        bullets.add(bullet);

        return bullets;
    }

    /**
     * Bullet แบบลอยสุ่มวนไปเรื่อย ๆ (Floating / Wobble motion)
     */
    public static class FloatingBullet extends Bullet {
        private final Random rand = new Random();
        private double angle;
        private double speed;
        private double changeTimer = 0;
        private double targetAngle; // มุมปลายทางที่สุ่มเปลี่ยน
        private double rotationSpeed = Math.toRadians(1.5); // ความเร็วการหมุน (โค้งนุ่ม)

        public FloatingBullet(double x, double y, double velocityX, double velocityY,
                              Color color, double screenWidth, double screenHeight) {
            super(x, y, velocityX, velocityY, color, screenWidth, screenHeight);
            this.speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            this.angle = Math.atan2(velocityY, velocityX);
            this.targetAngle = angle;
        }

        public void update(double deltaTime) {
            // นับเวลาเพื่อเปลี่ยนทิศ
            changeTimer += deltaTime;

            // 🔹 ทุก ๆ 0.5-1.5 วิ เปลี่ยนมุมปลายทางแบบสุ่ม
            if (changeTimer > 0.5 + rand.nextDouble()) {
                changeTimer = 0;
                // สุ่มมุมใหม่ในช่วง ±90° จากทิศปัจจุบัน
                targetAngle = angle + Math.toRadians(rand.nextDouble() * 180 - 90);
            }

            // 🔹 ค่อยๆ หมุนมุมเข้าหา targetAngle (smooth turn)
            double diff = targetAngle - angle;
            diff = Math.atan2(Math.sin(diff), Math.cos(diff)); // normalize -π ถึง π

            if (diff > rotationSpeed) diff = rotationSpeed;
            if (diff < -rotationSpeed) diff = -rotationSpeed;

            angle += diff;

            // 🔹 อัปเดตความเร็วตามมุมใหม่
            setVelocityX(Math.cos(angle) * speed);
            setVelocityY(Math.sin(angle) * speed);

            // 🔹 เคลื่อนที่
            setX(getX() + getVelocityX() * deltaTime);
            setY(getY() + getVelocityY() * deltaTime);

            // 🔹 ถ้าออกนอกขอบ – เด้งกลับ
            if (getX() < 0 || getX() > 1280) {
                setVelocityX(-getVelocityX());
                angle = Math.atan2(getVelocityY(), getVelocityX());
            }
            if (getY() < 0 || getY() > 720) {
                setVelocityY(-getVelocityY());
                angle = Math.atan2(getVelocityY(), getVelocityX());
            }
        }
    }
}
