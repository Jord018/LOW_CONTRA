package com.example.contrabossclone.model.Boss;

import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.MachanicShoot.ShootingStrategy;
import com.example.contrabossclone.model.Player;
import java.util.List;
import java.util.ArrayList;
import javafx.geometry.Rectangle2D; // ⭐️ เพิ่ม
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class SecondBoss extends Boss {

    private transient Image spriteSheet;
    private Rectangle2D[] frames;
    private int animationFrame = 0;
    private int animationTick = 0;

    public int getShootCooldown() {
        return shootCooldown;
    }

    public void setShootCooldown(int shootCooldown) {
        this.shootCooldown = shootCooldown;
    }

    private int shootCooldown = 0;
    private int animationSpeed = 200; // ความเร็ว Animation (ยิ่งน้อย ยิ่งเร็ว)
    // ⭐️ (ลบ currentState)


    public SecondBoss(double x, double y, double width, double height, Player player, ShootingStrategy shootingStrategy, String spriteSheetPath,int score) {
        super(x, y, width, height, player, shootingStrategy,score);
        try {
            this.spriteSheet = new Image(getClass().getResourceAsStream(spriteSheetPath));
        } catch (Exception e) {
            System.err.println("!!! Error loading boss sprite sheet: " + spriteSheetPath);
            this.spriteSheet = null;
        }

        initializeAnimations();
    }

    protected void initializeAnimations() {

        this.frames = new Rectangle2D[]{
                new Rectangle2D(0, 0, 113, 113), // Frame 1
                new Rectangle2D(113, 0, 113, 113)  // Frame 2
        };
    }

    @Override
    public void update() {

        super.update();

        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;

            if (frames != null) {
                animationFrame = (animationFrame + 1) % frames.length;
            }
        }
    }



    @Override
    public void render(GraphicsContext gc) {
        if (spriteSheet != null && frames != null) {

            if (animationFrame >= frames.length) animationFrame = 0;
            Rectangle2D frame = frames[animationFrame];

            double sX = frame.getMinX(), sY = frame.getMinY();
            double sW = frame.getWidth(), sH = frame.getHeight();

            gc.drawImage(spriteSheet, sX, sY, sW, sH, getX(), getY(), getWidth(), getHeight());

        } else {
            renderFallback(gc);
        }


        gc.setFill(Color.WHITE);
        gc.fillRect(getX(), getY() - 20, getWidth(), 10);
        gc.setFill(Color.RED);
        gc.fillRect(getX(), getY() - 20, getWidth() * (getHealth() / 100.0), 10);
    }

    // เมธอดสำรอง
    private void renderFallback(GraphicsContext gc) {
        gc.setFill(Color.PURPLE);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
    }


}

