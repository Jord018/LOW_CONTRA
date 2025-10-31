package com.example.contrabossclone.model.Enemy;

import com.example.contrabossclone.model.Player;
import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.Stage.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import java.util.List;
import java.util.ArrayList;

public class Enemy extends Player {
    private boolean alive = true;
    private Player target;
    private int shootCooldown = 0;
    private static final int SHOOT_DELAY = 60; // Frames between shots
    // Bullets are now managed by GameModel

    public Enemy(double x, double y, Player target) {
        super(x, y);
        this.target = target;
        this.setWidth(50);
        this.setHeight(50);
        this.setSpeed(1.0);
    }

    @Override
    public void render(GraphicsContext g) {
        if (!alive) return;

        g.setFill(Color.RED);
        g.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());

        // Bullets are rendered by GameModel
    }

    public void update(List<Platform> platforms, double screenHeight) {
        if (!alive) return;

        super.update(platforms, screenHeight);
        move();
        // Shooting logic
        if (shootCooldown > 0) {
            shootCooldown--;
        }
    }

    private boolean canSeeTarget() {
        if (target == null) return false;
        // Simple line of sight check
        return Math.abs(getY() - target.getY()) < 50;
    }

    public List<Bullet> shoot() {
        List<Bullet> newBullets = new ArrayList<>();
        if (target == null || shootCooldown > 0 || !canSeeTarget()) 
            return newBullets;

        double angle = Math.atan2(target.getY() - getY(), target.getX() - getX());
        double bulletSpeed = 5.0;
        double velocityX = Math.cos(angle) * bulletSpeed;
        double velocityY = Math.sin(angle) * bulletSpeed;

        newBullets.add(new Bullet(getX() + getWidth()/2, getY() + getHeight()/2,
                velocityX, velocityY, Color.RED, 800, 600));
        
        shootCooldown = SHOOT_DELAY;
        return newBullets;
    }

    private void move() {
        if (target == null) return;

        // Simple AI: move towards player if not too close
        double distance = Math.hypot(target.getX() - getX(), target.getY() - getY());
        if (distance > 200) { // Keep some distance
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


    public boolean isOnGround() {
        return getY() + getHeight() >= 600; // Replace with actual screen height
    }

}