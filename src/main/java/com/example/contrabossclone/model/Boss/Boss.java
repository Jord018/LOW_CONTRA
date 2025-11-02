package com.example.contrabossclone.model.Boss;

import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.Player;
import com.example.contrabossclone.model.MachanicShoot.ShootingStrategy;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class Boss {

    protected double x, y;
    private double width, height;
    private int health = 100;
    private double speed = 2;
    private int shootCooldown = 0;
    private boolean allowShoot = true;
    private Player player;
    private ShootingStrategy shootingStrategy;

    public Boss(double x, double y, double width, double height, Player player, ShootingStrategy shootingStrategy) {
        this(x, y, width, height, player, shootingStrategy, true);
    }
    
    public Boss(double x, double y, double width, double height, Player player, ShootingStrategy shootingStrategy, boolean allowShoot) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.player = player;
        this.shootingStrategy = shootingStrategy;
        this.allowShoot = allowShoot;
    }

    public void update() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
    }

    public List<Bullet> shoot(double screenWidth, double screenHeight) {
        if (!allowShoot || shootCooldown > 0) return new ArrayList<>();
        shootCooldown = 60;

        double spawnX = getX() + muzzleOffsetX;
        double spawnY = getY() + muzzleOffsetY;

        return shootingStrategy.shoot(spawnX, spawnY, player, screenWidth, screenHeight, 3);
    }

    public void render(GraphicsContext gc) {


        double baseHeight = height * 0.8;
        double baseOffsetY = height * 0.2;

        double gunWidth = width * 0.2;
        double gunHeight = height * 0.4;
        double gunOffsetX = (width - gunWidth) / 2;

        double healthBarHeight = 10;
        double healthBarOffsetY = 20;

        // --- วาด ---

        // Base
        gc.setFill(Color.web("#A9A9A9"));
        gc.fillRect(x, y + baseOffsetY, width, baseHeight);



        // Health bar
        gc.setFill(Color.WHITE);
        gc.fillRect(x, y - healthBarOffsetY, width, healthBarHeight);
        gc.setFill(Color.RED);
        gc.fillRect(x, y - healthBarOffsetY, width * (health / 100.0), healthBarHeight);
    }

    public void hit() {
        health -= 10;
        if (health < 0) {
            health = 0;
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public boolean isDefeated() {
        return health <= 0;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getHealth() {
        return health;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public boolean isAllowShoot() {
        return allowShoot;
    }
    protected double muzzleOffsetX = 0;
    protected double muzzleOffsetY = 0;

    public void setMuzzleOffset(double offsetX, double offsetY) {
        this.muzzleOffsetX = offsetX;
        this.muzzleOffsetY = offsetY;
    }

    
    public void setAllowShoot(boolean allowShoot) {
        this.allowShoot = allowShoot;
    }
}
