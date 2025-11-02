package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JAVA implements ShootingStrategy {
    private static Image spriteSheet;
    private Rectangle2D spriteFrame;
    private Rectangle2D[] frames;

    public JAVA(Image spriteSheet, Rectangle2D[] frames) {
        this.spriteSheet = spriteSheet;
        this.frames = frames;
    }
    private final Random random = new Random();

    @Override
    public List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight, double bulletSpeed) {

        List<Bullet> bullets = new ArrayList<>();

        double angleDeg = 90 + (random.nextDouble() * 180 - 90);
        double angle = Math.toRadians(angleDeg);

        double vx = Math.cos(angle) * 0.4;
        double vy = Math.sin(angle) * 0.4;

        Bullet bullet = new TrackingDropBullet(
                x, y, vx, vy,
                this.spriteSheet, this.frames, screenWidth, screenHeight, player
        );
        bullets.add(bullet);

        return bullets;
    }

    public static class TrackingDropBullet extends Bullet {
        private final Player player;
        private double speed;
        private boolean hasDropped = false;

        private Rectangle2D[] frames;
        private int animationFrame = 0;
        private int animationTick = 0;
        private int animationSpeed = 50; // (ความเร็ว Animation)

        public TrackingDropBullet(double x, double y, double velocityX, double velocityY,
                                  Image spriteSheet, Rectangle2D[] frames,
                                  double screenWidth, double screenHeight,
                                  Player player) {

            super(x, y, velocityX, velocityY,
                    spriteSheet, (frames != null && frames.length > 0) ? frames[0] : null,
                    40, 40,
                    screenWidth, screenHeight);

            this.speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            this.player = player;
            this.frames = frames;
        }

        @Override
        public void update() {
            super.update();
            if (isExploding() || isFinished()) {
                return;
            }

            if (!hasDropped) {
                setX(getX() + getVelocityX());
                setY(getY() + getVelocityY());

                if (Math.abs(getX() - player.getX()) < 10) {
                    hasDropped = true;
                    setVelocityX(0);
                    setVelocityY(speed);
                }
            } else {
                setX(getX() + getVelocityX());
                setY(getY() + getVelocityY());
            }

            animationTick++;
            if (animationTick >= animationSpeed) {
                animationTick = 0;
                if (frames != null) {
                    animationFrame = (animationFrame + 1) % frames.length;
                }
            }
        }

        public void render(GraphicsContext gc) {
            if (isFinished()) return;

            if (isExploding()) {
                super.render(gc);
                return;
            }

            if (frames == null || spriteSheet == null || animationFrame >= frames.length) {
                super.render(gc);
                return;
            }

            Rectangle2D frame = this.frames[animationFrame];
            double sX = frame.getMinX(), sY = frame.getMinY();
            double sW = frame.getWidth(), sH = frame.getHeight();

            gc.drawImage(spriteSheet, sX, sY, sW, sH,
                    getX(), getY(), getWidth(), getHeight());
        }
    }
}
