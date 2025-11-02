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
                            long delay, double screenWidth, double screenHeight,
                            Image spriteSheet, Rectangle2D spriteFrame,
                            double width, double height) {
            // ใช้ velocityX, velocityY = 0 เพราะเราจะ update เอง
            super(x, y, 0, 0,
                    spriteSheet != null ? spriteSheet : null,
                    spriteFrame != null ? spriteFrame : null,
                    width, height,
                    screenWidth, screenHeight);
            // ถ้า spriteSheet == null, ใช้สี fallback
            if (spriteSheet == null) {
                setColor(Color.ORANGE);
            }

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
            if (tElapsed < delay) return;

            // Y ไหลลงตรง ๆ
            setY(getY() + speed);

            // X สั่นซ้ายขวา
            double xOffset = Math.cos(phase + spiralAngle * 0.005) * radius * 5; // ปรับ amplitude ได้
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
                        delay, screenWidth, screenHeight,
                        this.spriteSheet, this.spriteFrame,
                        10, 10));
            }
        }

        spiralAngle += 20; // หมุนชุดต่อไป
        return bullets;
    }

}
