package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class SpiralShoot implements ShootingStrategy {

    private Image spriteSheet;
    private Rectangle2D spriteFrame;

    private long lastShotTime = 0;
    private double spiralAngle = 0; // มุมหมุนรอบแกน

    private final long cooldown = 5000; // 5 วินาที
    private final int numBullets = 12;  // กระสุนต่อชั้น
    private final int numLayers = 3;    // จำนวนชั้นซ้อน
    private final double layerRadiusStep = 15; // รัศมีแต่ละชั้น

    public SpiralShoot(Image spriteSheet, Rectangle2D spriteFrame) {
        this.spriteSheet = spriteSheet;
        this.spriteFrame = spriteFrame;
    }

    // ---------------- SpiralBullet ----------------
    public class SpiralBullet extends Bullet {
        private final double radius;
        private final double speed;
        private final double phase;
        private final double startX;
        private final long startTime;
        private final long delay;


        public SpiralBullet(double x, double y, double bulletSpeed, double radius, double phase,
                            long delay, double screenWidth, double screenHeight, Color color,
                            double width, double height) {
            super(x, y, 0, 0, color, width, height, screenWidth, screenHeight);
            this.radius = radius;
            this.speed = bulletSpeed;
            this.phase = phase;
            this.startX = x;
            this.startTime = System.currentTimeMillis();
            this.delay = delay;
        }

        @Override
        public void update() {
            long tElapsed = System.currentTimeMillis() - startTime;
            if(tElapsed < delay) return; // รอ delay ก่อน

            // Y ไหลลงตรง ๆ
            setY(getY() + speed);

            // X สั่นเล็กน้อยตาม cosine
            double xOffset = Math.cos(phase + spiralAngle * 0.005) * radius * 5; // amplitude ปรับขนาดสั่น
            setX(startX + xOffset);
        }




    }
    @Override
    public List<Bullet> shoot(double x, double y, Player player,
                              double screenWidth, double screenHeight, double bulletSpeed) {

        List<Bullet> bullets = new ArrayList<>();
        long currentTime = System.currentTimeMillis();

        // spawn กระสุนต่อเนื่องทีละชุดทุก cooldown
        if (currentTime - lastShotTime < cooldown) return bullets;
        lastShotTime = currentTime;

        int numLayers = 1;
        int numBullets = 50;      // กระสุนต่อชั้น
        double layerRadiusStep = 12;

        for (int layer = 0; layer < numLayers; layer++) {
            double radius = layerRadiusStep * (layer + 1);
            for (int i = 0; i < numBullets; i++) {
                double phase = i * (2 * Math.PI / numBullets);

                // กระสุนแต่ละตัว delay ตาม index เพื่อไหลลงทีละนัด
                long delay = i * 50;

                bullets.add(new SpiralBullet(x, y, bulletSpeed, radius, phase,
                        delay, screenWidth, screenHeight, Color.ORANGE, 10, 10));
            }
        }

        spiralAngle += 20; // หมุนชุดต่อไป
        return bullets;
    }

}

