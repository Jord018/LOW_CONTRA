package com.example.contrabossclone.model;

import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.Stage.Platform;
import com.example.contrabossclone.util.ResourceLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public enum WeaponType {
        NORMAL,
        MACHINE_GUN,
        SPREAD_GUN,
        LASER,
        FIRE
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    private WeaponType weaponType = WeaponType.NORMAL;

    private double x, y;
    private double width = 40, height = 60;
    private final double PRONE_WIDTH = 40;
    private final double PRONE_HEIGHT = 30;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    private double speed = 2;
    private double dx = 0;

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    private double velocityY = 0;
    private double gravity = 0.15;

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    private boolean onGround = false;
    private boolean isPressingDown = false;
    private double aimAngle = 90.0;

    private boolean onSolidPlatform = false;

    private transient Image spriteSheet; // Sprite ของ Player
    private Map<String, Rectangle2D[]> animations;
    private int animationFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 15;
    private boolean facingRight = true;
    private String currentState = "STAND";

    private transient Image bulletSpriteSheet;
    private Rectangle2D bulletFrame; // Frame สำหรับกระสุนปกติ

    public int getMaxHealth() {
        return maxHealth;
    }

    private int maxHealth = 1;
    private static final Logger logger = LogManager.getLogger(Player.class);

    private int score = 0;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    private int health = maxHealth;



    private int lives = 3;
    private int fireRate = 30;
    private int fireCooldown = 0;

    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    private boolean isInvincible = false;
    private int invincibilityTimer = 0;
    private double respawnX, respawnY;


    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.respawnX = 10;
        this.respawnY = 10;

        this.spriteSheet = ResourceLoader.loadImage("/GameAssets/Character2.png");
        this.bulletSpriteSheet = ResourceLoader.loadImage("/GameAssets/PlayerBullet.png");
        this.bulletFrame = new Rectangle2D(0, 0, 25, 25); // (sX, sY, sW, sH)

        initializeAnimations();
    }

    private void initializeAnimations() {
        animations = new HashMap<>();

        animations.put("STAND_R", new Rectangle2D[] { new Rectangle2D(256,128 , 128, 128) });
        animations.put("RUN_R", new Rectangle2D[] {
                new Rectangle2D(0, 128, 128, 128),
                new Rectangle2D(128, 128, 128, 128),
                new Rectangle2D(256, 128, 128, 128),
                new Rectangle2D(384, 128, 128, 128),
                new Rectangle2D(512, 128, 128, 128),
                new Rectangle2D(640, 128, 128, 128)
        });

        animations.put("JUMP_R", new Rectangle2D[] {
                new Rectangle2D(384, 512, 128, 128),
                new Rectangle2D(512, 512, 128, 128),
                new Rectangle2D(640, 512, 128, 128),
                new Rectangle2D(0, 640, 128, 128)
        });

        animations.put("AIM_UP_R", new Rectangle2D[] { new Rectangle2D(0, 256, 128, 128) }); // ยืนเงยหน้า

        animations.put("AIM_DIAG_R", new Rectangle2D[] {
                new Rectangle2D(384, 256, 128, 128),
                new Rectangle2D(512, 256, 128, 128),
                new Rectangle2D(640, 256, 128, 128),
                new Rectangle2D(0, 384, 128, 128),
                new Rectangle2D(128, 384, 128, 128),
                new Rectangle2D(256, 384, 128, 128)
        });

        animations.put("RUN_AIM_DIAG_R", new Rectangle2D[] { // วิ่งยิงเฉียง
                new Rectangle2D(384, 256, 128, 128),
                new Rectangle2D(512, 256, 128, 128),
                new Rectangle2D(640, 256, 128, 128),
                new Rectangle2D(0, 384, 128, 128),
                new Rectangle2D(128, 384, 128, 128),
                new Rectangle2D(256, 384, 128, 128)
        });

        animations.put("CROUCH_R", new Rectangle2D[] { new Rectangle2D(256, 256, 128, 128) }); // หมอบ

        animations.put("STAND_L", new Rectangle2D[] { new Rectangle2D(256, 128, 128, 128) });

        animations.put("RUN_L", new Rectangle2D[] {
                new Rectangle2D(0, 128, 128, 128),
                new Rectangle2D(128, 128, 128, 128),
                new Rectangle2D(256, 128, 128, 128),
                new Rectangle2D(384, 128, 128, 128),
                new Rectangle2D(512, 128, 128, 128),
                new Rectangle2D(640, 128, 128, 128)
        });

        animations.put("JUMP_L", new Rectangle2D[] {
                new Rectangle2D(384, 512, 128, 128),
                new Rectangle2D(512, 512, 128, 128),
                new Rectangle2D(640, 512, 128, 128),
                new Rectangle2D(0, 640, 128, 128)
        });

        animations.put("AIM_UP_L", new Rectangle2D[] { new Rectangle2D(0, 256, 128, 128) });

        animations.put("AIM_DIAG_L", new Rectangle2D[] {
                new Rectangle2D(384, 256, 128, 128),
                new Rectangle2D(512, 256, 128, 128),
                new Rectangle2D(640, 256, 128, 128),
                new Rectangle2D(0, 384, 128, 128),
                new Rectangle2D(128, 384, 128, 128),
                new Rectangle2D(256, 384, 128, 128)
        });
        animations.put("RUN_AIM_DIAG_L", new Rectangle2D[] {
                new Rectangle2D(384, 256, 128, 128),
                new Rectangle2D(512, 256, 128, 128),
                new Rectangle2D(640, 256, 128, 128),
                new Rectangle2D(0, 384, 128, 128),
                new Rectangle2D(128, 384, 128, 128),
                new Rectangle2D(256, 384, 128, 128)
        });

        animations.put("RUN_AIM_DOWN_R", new Rectangle2D[] {
                new Rectangle2D(384, 384, 128, 128),
                new Rectangle2D(512, 384, 128, 128),
                new Rectangle2D(640, 384, 128, 128),
                new Rectangle2D(0, 512, 128, 128),
                new Rectangle2D(128, 512, 128, 128),
                new Rectangle2D(256, 512, 128, 128)
        });
        animations.put("RUN_AIM_DOWN_L", new Rectangle2D[] {
                new Rectangle2D(384, 384, 128, 128),
                new Rectangle2D(512, 384, 128, 128),
                new Rectangle2D(640, 384, 128, 128),
                new Rectangle2D(0, 512, 128, 128),
                new Rectangle2D(128, 512, 128, 128),
                new Rectangle2D(256, 512, 128, 128)
        });
        animations.put("CROUCH_L", new Rectangle2D[] { new Rectangle2D(256, 256, 128, 128) });
    }

    public void moveLeft() {
        dx = -speed;
        facingRight = false;
        logger.debug("Player moving left - Position: (x: {}, y: {})", x, y);
    }

    public void moveRight() {
        dx = speed;
        facingRight = true;
        logger.debug("Player moving right - Position: (x: {}, y: {})", x, y);
    }

    public void stop() {
        dx = 0;
        logger.debug("Player stopped - Position: (x: {}, y: {})", x, y);
    }


    public void jump() {
        if (onGround) {
            if (isPressingDown && !onSolidPlatform) {
                y += 1;
                logger.debug("Player fell through platform at (x: {}, y: {})", x, y);
            } else {
                velocityY = -7; // Jump strength
                logger.debug("Player jumped from position (x: {}, y: {})", x, y);
            }
            onGround = false;
            onSolidPlatform = false;
        } else {
            logger.debug("Jump attempted but player is not on ground");
        }
    }

    public void setPressingDown(boolean pressingDown) {
        if (this.isPressingDown != pressingDown) {
            double bottomY = y + (isPressingDown ? PRONE_HEIGHT : height);

            this.isPressingDown = pressingDown;
            y = bottomY - (pressingDown ? PRONE_HEIGHT : height);
            logger.info("Action: Set Pressing Down | New State: {} | Position adjusted to y={}", pressingDown, y);
        }
    }


    public void setRespawnPosition(double x, double y) {
        this.respawnX = x;
        this.respawnY = y;
        logger.info("Action: Set Respawn Position | RespawnX: {} | RespawnY: {}", x, y);
    }
    private boolean isAngleNear(double angle, double target) {
        angle = (angle + 360) % 360;
        target = (target + 360) % 360;
        double diff = Math.abs(angle - target);
        return diff <= 5 || diff >= 355; // รองรับ 0°/360°
    }


    public void update(List<Platform> platforms, double screenHeight) {
        x += dx;

        // Apply gravity
        velocityY += gravity;
        y += velocityY;

        onGround = false;
        onSolidPlatform = false;

        // Check for ground collision
        if (getBounds().getMaxY() > screenHeight) {
            y = screenHeight - getBounds().getHeight();
            if (isPressingDown) {
                y = screenHeight - PRONE_HEIGHT;
            } else {
                y = screenHeight - height;
            }

            velocityY = 0;
            onGround = true;
            onSolidPlatform = true;
            logger.info("Player landed on ground");
        }

        // Check for platform collisions
        for (Platform platform : platforms) {
            Rectangle2D playerBounds = getBounds();
            Rectangle2D platformBounds = platform.getBounds();

            if (playerBounds.intersects(platformBounds)) {

                if (velocityY > 0 && (playerBounds.getMaxY() - velocityY) <= platformBounds.getMinY()) {
                    y = platformBounds.getMinY() - playerBounds.getHeight();
                    velocityY = 0;
                    onGround = true;

                    if (platform.isSolid()) {
                        this.onSolidPlatform = true;
                    }

                    logger.info("Player landed on platform");
                }

                else if (platform.isSolid() && velocityY < 0 && (playerBounds.getMinY() - velocityY) >= platformBounds.getMaxY()) {
                    y = platformBounds.getMaxY();
                    velocityY = 0;
                }

                else if (platform.isSolid()) {
                    if (dx > 0 && (playerBounds.getMaxX() - dx) <= platformBounds.getMinX()) {
                        x = platformBounds.getMinX() - playerBounds.getWidth();
                        dx = 0;
                    }
                    else if (dx < 0 && (playerBounds.getMinX() - dx) >= platformBounds.getMaxX()) {
                        x = platformBounds.getMaxX();
                        dx = 0;
                    }
                }
            }
        }

        //Animation state logic
        String newState = "STAND";

        if (isPressingDown) {
            if (dx > 0) {
                newState = "RUN_AIM_DOWN";
                aimAngle = 315;
            } else if (dx < 0) {
                newState = "RUN_AIM_DOWN";
                aimAngle = 225;
            } else {
                newState = "CROUCH";
                aimAngle = 270;
            }
        }
        else if (!onGround) {
            newState = "JUMP";
        } else if (dx != 0) {
            if (aimAngle == 45 || aimAngle == 135) {
                newState = "RUN_AIM_DIAG";
            } else {
                newState = "RUN";
            }
        } else {
            if (aimAngle == 90) {
                newState = "AIM_UP";
            } else if (aimAngle == 45 || aimAngle == 135) {
                newState = "AIM_DIAG";
            } else {
                newState = "STAND";
            }
        }

        if (!newState.equals(currentState)) {
            currentState = newState;
            animationFrame = 0;
            animationTick = 0;
        } else {
            animationTick++;
            if (animationTick >= animationSpeed) {
                animationTick = 0;
                String animKey = currentState + (facingRight ? "_R" : "_L");
                Rectangle2D[] frames = animations.get(animKey);
                if (frames != null) {
                    animationFrame = (animationFrame + 1) % frames.length;
                }
            }
        }

        if (fireCooldown > 0) {
            fireCooldown--;
        }
        if (invincibilityTimer > 0) {
            invincibilityTimer--;
            if (invincibilityTimer == 0) {
                isInvincible = false;
            }
        }
        logger.trace("Player position updated to " + x + " " + y);
    }

    public void render(GraphicsContext gc) {

        if (spriteSheet == null || animations == null) {
            renderFallback(gc);
            return;
        }

        String animKey = currentState + (facingRight ? "_R" : "_L");
        Rectangle2D[] frames = animations.get(animKey);

        if (frames == null) {
            if (currentState.equals("RUN")) {
                animKey = "RUN_" + (facingRight ? "_R" : "_L");
            } else {
                animKey = "STAND_" + (facingRight ? "_R" : "_L");
            }
            frames = animations.get(animKey);
        }

        if (frames == null) { renderFallback(gc); return; }

        if (animationFrame >= frames.length) animationFrame = 0;
        Rectangle2D frame = frames[animationFrame];

        double sX = frame.getMinX(), sY = frame.getMinY();
        double sW = frame.getWidth(), sH = frame.getHeight();

        Rectangle2D hitbox = getBounds();

        double dX = hitbox.getMinX() + (hitbox.getWidth() - sW) / 2;
        double dY = hitbox.getMinY() + (hitbox.getHeight() - sH);

        if (facingRight) {
            gc.drawImage(spriteSheet, sX, sY, sW, sH, dX, dY, sW, sH);
        } else {
            gc.drawImage(spriteSheet, sX, sY, sW, sH, dX + sW, dY, -sW, sH);
        }



    }

    private void renderFallback(GraphicsContext gc) {
        gc.setFill(Color.CYAN);
        Rectangle2D bounds = getBounds();
        gc.fillRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
        logger.warn("Rendering fallback rectangle for player.");
    }

    public boolean canShoot() {
        return fireCooldown <= 0;
    }

    public List<Bullet> shoot(double screenWidth, double screenHeight) {
        fireCooldown = fireRate;
        logger.info("Player fired {} shot at angle {}° - Position: (x: {}, y: {})",
                weaponType, aimAngle, x, y);
        List<Bullet> bullets = new ArrayList<>();
        double bulletSpeed = 10;

        Rectangle2D hitbox = getBounds();
        double fireX = hitbox.getMinX() + hitbox.getWidth() / 2;
        double fireY = isPressingDown
                ? hitbox.getMinY() + hitbox.getHeight() * 0.6
                : hitbox.getMinY() + hitbox.getHeight() * 0.4;

        double velocityX = Math.cos(Math.toRadians(aimAngle)) * bulletSpeed;
        double velocityY = -Math.sin(Math.toRadians(aimAngle)) * bulletSpeed;

        double bulletWidth = 10;
        double bulletHeight = 10;

        if (bulletSpriteSheet == null || bulletFrame == null) {
            logger.warn("Bullet sprite not loaded! Using fallback color.");
            return shootFallback(screenWidth, screenHeight, fireX, fireY, velocityX, velocityY, bulletSpeed);
        }

        switch (weaponType) {
            case NORMAL:
            case MACHINE_GUN:
                bullets.add(new Bullet(fireX, fireY, velocityX, velocityY,
                        bulletSpriteSheet, bulletFrame,
                        bulletWidth, bulletHeight,
                        screenWidth, screenHeight));
                break;
            case SPREAD_GUN:
                bullets.add(new Bullet(fireX, fireY, Math.cos(Math.toRadians(aimAngle - 15)) * bulletSpeed, -Math.sin(Math.toRadians(aimAngle - 15)) * bulletSpeed,
                        bulletSpriteSheet, bulletFrame, bulletWidth, bulletHeight, screenWidth, screenHeight));
                bullets.add(new Bullet(fireX, fireY, velocityX, velocityY,
                        bulletSpriteSheet, bulletFrame, bulletWidth, bulletHeight, screenWidth, screenHeight));
                bullets.add(new Bullet(fireX, fireY, Math.cos(Math.toRadians(aimAngle + 15)) * bulletSpeed, -Math.sin(Math.toRadians(aimAngle + 15)) * bulletSpeed,
                        bulletSpriteSheet, bulletFrame, bulletWidth, bulletHeight, screenWidth, screenHeight));
                break;
            case LASER:

                bullets.add(new Bullet(fireX, fireY, velocityX * 2, velocityY * 2,
                        bulletSpriteSheet, bulletFrame,
                        2, 100, // ขนาด Laser
                        screenWidth, screenHeight));
                break;
            case FIRE:

                bullets.add(new Bullet(fireX, fireY, velocityX, velocityY,
                        bulletSpriteSheet, bulletFrame,
                        10, 10,
                        screenWidth, screenHeight));
                break;
        }

        logger.debug("Player fired bullet: " + bullets.size());
        return bullets;
    }
    private List<Bullet> shootFallback(double screenWidth, double screenHeight, double fireX, double fireY, double velocityX, double velocityY, double bulletSpeed) {
        List<Bullet> bullets = new ArrayList<>();
        switch (weaponType) {
            case NORMAL:
            case MACHINE_GUN:
                bullets.add(new Bullet(fireX, fireY, velocityX, velocityY, Color.YELLOW, screenWidth, screenHeight));
                break;
            case SPREAD_GUN:
                bullets.add(new Bullet(fireX, fireY, Math.cos(Math.toRadians(aimAngle - 15)) * bulletSpeed, -Math.sin(Math.toRadians(aimAngle - 15)) * bulletSpeed, Color.YELLOW, screenWidth, screenHeight));
                bullets.add(new Bullet(fireX, fireY, velocityX, velocityY, Color.YELLOW, screenWidth, screenHeight));
                bullets.add(new Bullet(fireX, fireY, Math.cos(Math.toRadians(aimAngle + 15)) * bulletSpeed, -Math.sin(Math.toRadians(aimAngle + 15)) * bulletSpeed, Color.YELLOW, screenWidth, screenHeight));
                break;
            case LASER:
                bullets.add(new Bullet(fireX, fireY, velocityX * 2, velocityY * 2, Color.RED, 2, 100, screenWidth, screenHeight));
                break;
            case FIRE:
                bullets.add(new Bullet(fireX, fireY, velocityX, velocityY, Color.ORANGE, 10, 10, screenWidth, screenHeight));
                break;
        }
        return bullets;
    }

    public void setAimAngle(double aimAngle) {
        this.aimAngle = aimAngle;
    }

    public void setWeaponType(WeaponType weaponType) {
        logger.info("Weapon changed from {} to {}", this.weaponType, weaponType);
        this.weaponType = weaponType;
        if (weaponType == WeaponType.MACHINE_GUN) {
            fireRate = 10;
        } else {
            fireRate = 30;
        }
    }
    public void activateBarrier() {
        logger.info("Barrier activated - Player is now invincible for {} seconds", invincibilityTimer / 60.0);
        isInvincible = true;
        invincibilityTimer = 300; // 5 seconds of invincibility (60 frames per second)
    }

    public Rectangle2D getBounds() {
        double w = (isPressingDown && dx == 0) ? PRONE_WIDTH : width;
        double h = (isPressingDown && dx == 0) ? PRONE_HEIGHT : height;

        double bottomY = y + (isPressingDown ? PRONE_HEIGHT : height); // ปัจจุบัน
        double adjustedY = bottomY - h;

        double centerX = x + width / 2;
        double leftX = centerX - w / 2;

        return new Rectangle2D(leftX, adjustedY, w, h);
    }

    public void hit() {
        if (!isInvincible) {
            int damage = 10;
            health -= damage;
            logger.warn("Player hit! Health: {}/{}, Damage taken: {}", health, maxHealth, damage);
            isInvincible = true;
            invincibilityTimer = 120; // 2 seconds of invincibility
            if (health <= 0) {
                logger.warn("Player defeated! Respawning.");
                respawn();
            }
        } else {
            logger.info("Player hit but is invincible");
        }
    }

    public void respawn() {
        x = respawnX;
        y = respawnY;
        health = maxHealth;
        lives--;
        isInvincible = true;
        invincibilityTimer = 180; // 3 seconds of invincibility after respawn
        logger.warn("Player respawned at (x: {}, y: {}). Lives remaining: {}", x, y, lives);
        logger.info("Player respawned");
    }

    public boolean isDefeated() {
        return lives <= 0;
    }

    public int getLives() {
        return lives;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return isPressingDown ? PRONE_WIDTH : width;
    }

    public double getHeight() {
        return isPressingDown ? PRONE_HEIGHT : height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    public boolean isFacingRight() {return facingRight;}

    public boolean isOnSolidPlatform() {
        return onSolidPlatform;
    }



    public double getAimAngle() {
        return aimAngle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public void setLives(int lives) {this.lives = lives;}
}

