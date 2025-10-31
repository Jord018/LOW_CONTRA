package com.example.contrabossclone.model;

import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.Stage.Platform;
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

    private WeaponType weaponType = WeaponType.NORMAL;

    private double x, y;
    private double width = 40, height = 60;
    private final double PRONE_WIDTH = 40;
    private final double PRONE_HEIGHT = 60;

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
    private double velocityY = 0;
    private double gravity = 0.15;
    private boolean onGround = false;
    private boolean isPressingDown = false;
    private double aimAngle = 90.0;

    // ⭐️ --- (3) เพิ่มตัวแปรสำหรับ Animation ---
    private transient Image spriteSheet; // ใช้ 'transient' เพื่อกันปัญหา Serialize
    private Map<String, Rectangle2D[]> animations;
    private int animationFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 15; // ความเร็ว Animation (5 game ticks ต่อ 1 frame)
    private boolean facingRight = true;
    private String currentState = "STAND";
    // ⭐️ --- สิ้นสุดตัวแปร Animation ---

    private int maxHealth = 100;
    private static final Logger logger = LogManager.getLogger(Player.class);

    public int getScore() {
        logger.info("Player score: " + score);
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int score = 0;
    private int health = maxHealth;

    public void setLives(int lives) {
        this.lives = lives;
    }

    private int lives = 3;
    private int fireRate = 30; // Lower is faster
    private int fireCooldown = 0;
    private boolean isInvincible = false;
    private int invincibilityTimer = 0;
    private double respawnX, respawnY;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.respawnX = x;
        this.respawnY = 10;

        // ⭐️ --- (4) โหลด Sprite Sheet และกำหนดค่าเริ่มต้น Animation ---
        try {
            // **สำคัญ**: ต้องมั่นใจว่าไฟล์ Characters.png อยู่ในโฟลเดอร์ resources
            this.spriteSheet = new Image(getClass().getResourceAsStream("/GameAssets/Character2.png"));
        } catch (Exception e) {
            System.err.println("!!! Error loading sprite sheet: /Characters.png");
            System.err.println("โปรดตรวจสอบว่าไฟล์อยู่ในโฟลเดอร์ src/main/resources");
            this.spriteSheet = null;
        }
        initializeAnimations();
        // ⭐️ --- สิ้นสุดการโหลด ---
    }

    // ⭐️ --- (5) เมธอดใหม่: สำหรับเก็บพิกัด Sprite ทั้งหมด ---
    private void initializeAnimations() {
        animations = new HashMap<>();
        // พิกัด (x, y, width, height) ของตัวละครสีฟ้า (Bill)

        // --- หันขวา ---
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
        }); // ท่าหมุน

        animations.put("AIM_UP_R", new Rectangle2D[] { new Rectangle2D(0, 256, 128, 128) }); // ยืนเงยหน้า

        animations.put("AIM_DIAG_R", new Rectangle2D[] {
                new Rectangle2D(384, 256, 128, 128),
                new Rectangle2D(512, 256, 128, 128),
                new Rectangle2D(640, 256, 128, 128),
                new Rectangle2D(0, 384, 128, 128),
                new Rectangle2D(128, 384, 128, 128),
                new Rectangle2D(256, 384, 128, 128)
        }); // ยืนยิงเฉียง

        animations.put("RUN_AIM_DIAG_R", new Rectangle2D[] { // วิ่งยิงเฉียง
                new Rectangle2D(384, 256, 128, 128),
                new Rectangle2D(512, 256, 128, 128),
                new Rectangle2D(640, 256, 128, 128),
                new Rectangle2D(0, 384, 128, 128),
                new Rectangle2D(128, 384, 128, 128),
                new Rectangle2D(256, 384, 128, 128)
        });

        animations.put("CROUCH_R", new Rectangle2D[] { new Rectangle2D(256, 256, 128, 128) }); // หมอบ

        // --- หันซ้าย ---
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

        animations.put("CROUCH_L", new Rectangle2D[] { new Rectangle2D(256, 256, 128, 128) });
    }

    // ⭐️ --- (6) อัปเดตการเคลื่อนที่ให้เก็บทิศทาง ---
    public void moveLeft() {
        dx = -speed;
        facingRight = false; // ⭐️ เพิ่ม
        logger.debug("Player moved left");
    }

    public void moveRight() {
        dx = speed;
        facingRight = true; // ⭐️ เพิ่ม
        logger.debug("Player moved right");
    }

    public void stop() {
        dx = 0;
    }

    public void jump() {
        if (onGround) {
            if (isPressingDown) {
                // Fall through platform
                y += 1;
                logger.debug("Player fell through platform");
            } else {
                velocityY = -7; // Jump strength
                logger.debug("Player jumped");
            }
            onGround = false;
        }
    }

    public void setPressingDown(boolean pressingDown) {
        isPressingDown = pressingDown;
    }

    public void setRespawnPosition(double x, double y) {
        this.respawnX = x;
        this.respawnY = y;
    }

    public void update(List<Platform> platforms, double screenHeight) {
        x += dx;

        // Apply gravity
        velocityY += gravity;
        y += velocityY;

        onGround = false;

        // Check for ground collision
        if (getBounds().getMaxY() > screenHeight) { // ⭐️ ปรับเล็กน้อย
            y = screenHeight - getBounds().getHeight();
            // จัดการ y ใหม่ตาม getBounds()
            if (isPressingDown) {
                y = screenHeight - PRONE_HEIGHT;
            } else {
                y = screenHeight - height;
            }

            velocityY = 0;
            onGround = true;
            logger.info("Player landed on ground");
        }

        // Check for platform collisions
        for (Platform platform : platforms) {
            if (getBounds().intersects(platform.getBounds())) {
                // If falling and hit top of platform
                if (velocityY > 0 && getBounds().getMaxY() - velocityY <= platform.getY()) {

                    if (isPressingDown) {
                        y = platform.getY() - PRONE_HEIGHT;
                    } else {
                        y = platform.getY() - height;
                    }

                    velocityY = 0;
                    onGround = true;
                    logger.info("Player landed on platform");
                }
            }
        }

        // ⭐️ --- (7) เพิ่ม Logic อัปเดตสถานะและ Frame Animation ---
        // 1. ตรวจสอบสถานะปัจจุบัน
        String newState = "STAND"; // ท่าเริ่มต้น
        if (isPressingDown) {
            newState = "CROUCH";
        } else if (!onGround) {
            newState = "JUMP";
        } else if (dx != 0) { // กำลังวิ่ง
            if (aimAngle == 45 || aimAngle == 135) {
                newState = "RUN_AIM_DIAG";
            } else {
                newState = "RUN";
            }
        } else { // กำลังยืน
            if (aimAngle == 90) {
                newState = "AIM_UP";
            } else if (aimAngle == 45 || aimAngle == 135) {
                newState = "AIM_DIAG";
            } else {
                newState = "STAND";
            }
        }

        // 2. อัปเดต Frame
        if (!newState.equals(currentState)) {
            // ถ้าเปลี่ยนท่า ให้เริ่มนับ Frame 0 ใหม่
            currentState = newState;
            animationFrame = 0;
            animationTick = 0;
        } else {
            // ถ้าท่าเดิม ให้ขยับ Frame
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
        // ⭐️ --- สิ้นสุด Logic Animation ---

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

    // ⭐️ --- (8) แทนที่เมธอด render ทั้งหมด ---
    public void render(GraphicsContext gc) {

        if (spriteSheet == null || animations == null) {
            renderFallback(gc); // วาดสี่เหลี่ยมถ้าโหลด Sprite ไม่ได้
            return;
        }

        // 1. หา Key ของ Animation ที่ถูกต้อง
        String animKey = currentState + (facingRight ? "_R" : "_L");
        Rectangle2D[] frames = animations.get(animKey);

        if (frames == null) { // ถ้าไม่มีท่า (เช่น วิ่งยิงตรง) ให้กลับไปท่ายืน/วิ่ง
            if (currentState.equals("RUN")) {
                animKey = "RUN_" + (facingRight ? "_R" : "_L");
            } else {
                animKey = "STAND_" + (facingRight ? "_R" : "_L");
            }
            frames = animations.get(animKey);
        }

        if (frames == null) { renderFallback(gc); return; } // Fallback สุดท้าย

        // 2. หา Frame ปัจจุบัน
        if (animationFrame >= frames.length) animationFrame = 0;
        Rectangle2D frame = frames[animationFrame];

        double sX = frame.getMinX(), sY = frame.getMinY();
        double sW = frame.getWidth(), sH = frame.getHeight();

        // 3. หา Hitbox ปัจจุบัน (ว่ากำลังยืนหรือหมอบ)
        Rectangle2D hitbox = getBounds();

        // 4. คำนวณตำแหน่งวาด (dX, dY)
        // จัดให้ "กึ่งกลางล่าง" ของ Sprite ตรงกับ "กึ่งกลางล่าง" ของ Hitbox
        double dX = hitbox.getMinX() + (hitbox.getWidth() - sW) / 2;
        double dY = hitbox.getMinY() + (hitbox.getHeight() - sH);

        // 5. วาด Sprite ลงจอ
        // ⭐️ (FIX 2) เพิ่ม Logic การกลับด้านรูป
        if (facingRight) {
            // ⭐️ หันขวา: วาดตามปกติ
            gc.drawImage(spriteSheet, sX, sY, sW, sH, dX, dY, sW, sH);
        } else {
            // ⭐️ หันซ้าย: วาดแบบกลับด้าน
            // (วาดที่ dX + sW และใช้ความกว้างติดลบ -sW เพื่อพลิกรูป)
            gc.drawImage(spriteSheet, sX, sY, sW, sH, dX + sW, dY, -sW, sH);
        }

        // --- วาดหลอดเลือด (ปรับตำแหน่งเล็กน้อย) ---
        gc.setFill(Color.WHITE);
        gc.fillRect(hitbox.getMinX(), hitbox.getMinY() - 10, hitbox.getWidth(), 5);
        gc.setFill(Color.GREEN);
        gc.fillRect(hitbox.getMinX(), hitbox.getMinY() - 10, hitbox.getWidth() * (health / 100.0), 5);

        // --- วาดกรอบอมตะ (ถ้ามี) ---
        if (isInvincible) {
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(2);
            gc.strokeRect(hitbox.getMinX(), hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight());
        }
    }

    // เมธอดสำรอง เผื่อ Sprite โหลดไม่ขึ้น
    private void renderFallback(GraphicsContext gc) {
        gc.setFill(Color.CYAN);
        Rectangle2D bounds = getBounds();
        gc.fillRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
        logger.warn("Rendering fallback rectangle for player.");
    }

    public boolean canShoot() {
        // ... (โค้ดเดิม) ...
        return fireCooldown <= 0;
    }

    public List<Bullet> shoot(double screenWidth, double screenHeight) {
        fireCooldown = fireRate;
        List<Bullet> bullets = new ArrayList<>();
        double bulletSpeed = 10;
        double velocityX = Math.cos(Math.toRadians(aimAngle)) * bulletSpeed;
        double velocityY = -Math.sin(Math.toRadians(aimAngle)) * bulletSpeed;

        switch (weaponType) {
            case NORMAL:
                logger.info("Weapon type NORMAL");
                bullets.add(new Bullet(x + width / 2 - 2.5, y, velocityX, velocityY, Color.YELLOW, screenWidth, screenHeight));
                break;
            case MACHINE_GUN:
                logger.info("Weapon type MACHINE_GUN");
                bullets.add(new Bullet(x + width / 2 - 2.5, y, velocityX, velocityY, Color.YELLOW, screenWidth, screenHeight));
                break;
            case SPREAD_GUN:
                logger.info("Weapon type SPREAD_GUN");
                double angle1 = aimAngle - 15;
                double angle2 = aimAngle + 15;
                double velocityX1 = Math.cos(Math.toRadians(angle1)) * bulletSpeed;
                double velocityY1 = -Math.sin(Math.toRadians(angle1)) * bulletSpeed;
                double velocityX2 = Math.cos(Math.toRadians(angle2)) * bulletSpeed;
                double velocityY2 = -Math.sin(Math.toRadians(angle2)) * bulletSpeed;
                bullets.add(new Bullet(x + width / 2 - 2.5, y, velocityX1, velocityY1, Color.YELLOW, screenWidth, screenHeight));
                bullets.add(new Bullet(x + width / 2 - 2.5, y, velocityX, velocityY, Color.YELLOW, screenWidth, screenHeight));
                bullets.add(new Bullet(x + width / 2 - 2.5, y, velocityX2, velocityY2, Color.YELLOW, screenWidth, screenHeight));
                break;
            case LASER:
                logger.info("Weapon type LASER");
                // For now, laser will be a fast, long bullet
                bullets.add(new Bullet(x + width / 2 - 1, y, velocityX * 2, velocityY * 2, Color.RED, 2, 100, screenWidth, screenHeight));
                break;
            case FIRE:
                logger.info("Weapon type FIRE");
                bullets.add(new Bullet(x + width / 2 - 5, y, velocityX, velocityY, Color.ORANGE, 10, 10, screenWidth, screenHeight));
                break;
        }
        logger.debug("Player fired bullet: " + bullets.size());
        return bullets;

    }

    public void setAimAngle(double aimAngle) {
        this.aimAngle = aimAngle;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
        if (weaponType == WeaponType.MACHINE_GUN) {
            fireRate = 10;
        } else {
            fireRate = 30;
        }
    }



    public void activateBarrier() {
        isInvincible = true;
        invincibilityTimer = 999999999; // 5 seconds of invincibility (60 frames per second)
    }

    // ⭐️ --- (9) อัปเดต getBounds ให้ใช้ขนาดของท่าหมอบที่ถูกต้อง ---
    public Rectangle2D getBounds() {
        if (isPressingDown) {
            // คำนวณ y ของท่าหมอบใหม่ ให้เท้าอยู่ที่เดิม
            double standBottomY = y + height; // y ของเท้าท่ายืน
            double proneTopY = standBottomY - PRONE_HEIGHT; // y ของหัวท่าหมอบ

            // คำนวณ x ของท่าหมอบ ให้กึ่งกลางอยู่ที่เดิม
            double standCenterX = x + width / 2;
            double proneLeftX = standCenterX - (PRONE_WIDTH / 2);

            return new Rectangle2D(proneLeftX, proneTopY, PRONE_WIDTH, PRONE_HEIGHT);
        } else {
            // ท่าปกติ (ยืน)
            return new Rectangle2D(x, y, width, height);
        }
    }

    public void hit() {
        if (!isInvincible) {
            health -= 20;
            if (health <= 0) {
                lives--;
                if (lives > 0) {
                    respawn();
                } else {
                    health = 0;
                }
            }
        }
        logger.info("Player hit");
    }

    public void respawn() {
        health = maxHealth;
        x = respawnX;
        y = respawnY;
        isInvincible = true;
        //180
        invincibilityTimer = 999999999; // 3 seconds of invincibility after respawning
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
        // ⭐️ (10) ส่งค่าความกว้างปัจจุบัน (เผื่อหมอบ)
        return isPressingDown ? PRONE_WIDTH : width;
    }

    public double getHeight() {
        // ⭐️ (10) ส่งค่าความสูงปัจจุบัน (เผื่อหมอบ)
        return isPressingDown ? PRONE_HEIGHT : height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
