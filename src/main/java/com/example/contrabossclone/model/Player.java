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

    public double getAimAngle() {
        return aimAngle;
    }

    private double aimAngle = 0.0; // 0.0 means facing right

    // ⭐️ --- (3) เพิ่มตัวแปรสำหรับ Animation ---
    private transient Image spriteSheet; // ใช้ 'transient' เพื่อกันปัญหา Serialize
    private Map<String, Rectangle2D[]> animations;
    private int animationFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 15; // ความเร็ว Animation (5 game ticks ต่อ 1 frame)
    private boolean facingRight = true;
    private String currentState = "STAND";
    // ⭐️ --- สิ้นสุดตัวแปร Animation ---

    // ⭐️ --- (1) เพิ่มตัวแปรสำหรับ Sprite กระสุน ---
    private transient Image bulletSpriteSheet;
    private Rectangle2D bulletFrame; // Frame สำหรับกระสุนปกติ
    // (คุณสามารถเพิ่ม Frame สำหรับ Laser, Fire ได้ในอนาคต)
    // ⭐️ --- สิ้นสุดตัวแปร Sprite กระสุน ---

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

    public WeaponType getWeaponType() {
        return weaponType;
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

        // ⭐️ --- (2) โหลด Sprite Sheet (Bullet) ---
        try {
            // ⭐️⭐️ (สำคัญ) แก้ Path และชื่อไฟล์ให้ถูกต้อง
            this.bulletSpriteSheet = new Image(getClass().getResourceAsStream("/GameAssets/PlayerBullet.png"));
            // ⭐️⭐️ (สำคัญ) แก้พิกัด Sprite Frame ของกระสุนให้ถูกต้อง
            this.bulletFrame = new Rectangle2D(0, 0, 25, 25); // (sX, sY, sW, sH)

        } catch (Exception e) {
            System.err.println("!!! Error loading bullet sprite sheet: /GameAssets/PlayerBullet.png");
            this.bulletSpriteSheet = null;
        }
        // ⭐️ --- สิ้นสุดการโหลด Sprite กระสุน ---

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
        // --- เดินแล้วยิงเฉียงลง ---
        animations.put("RUN_AIM_DOWN_R", new Rectangle2D[] {
                //แก้ตรงนี้
                new Rectangle2D(384, 384, 128, 128),
                new Rectangle2D(512, 384, 128, 128),
                new Rectangle2D(640, 384, 128, 128),
                new Rectangle2D(0, 512, 128, 128),
                new Rectangle2D(128, 512, 128, 128),
                new Rectangle2D(256, 512, 128, 128)
        });
        animations.put("RUN_AIM_DOWN_L", new Rectangle2D[] {
                //แก้ตรงนี้
                new Rectangle2D(384, 384, 128, 128),
                new Rectangle2D(512, 384, 128, 128),
                new Rectangle2D(640, 384, 128, 128),
                new Rectangle2D(0, 512, 128, 128),
                new Rectangle2D(128, 512, 128, 128),
                new Rectangle2D(256, 512, 128, 128)
        });
        //หมอบ
        animations.put("CROUCH_L", new Rectangle2D[] { new Rectangle2D(256, 256, 128, 128) });
    }

    // ⭐️ --- (6) อัปเดตการเคลื่อนที่ให้เก็บทิศทาง ---
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
            if (isPressingDown) {
                // Fall through platform
                y += 1;
                logger.debug("Player fell through platform at (x: {}, y: {})", x, y);
            } else {
                velocityY = -7; // Jump strength
                logger.debug("Player jumped from position (x: {}, y: {})", x, y);
            }
            onGround = false;
        } else {
            logger.debug("Jump attempted but player is not on ground");
        }
    }

    public void setPressingDown(boolean pressingDown) {
        // ป้องกันไม่ให้ขยับ y ทุกเฟรม (เฉพาะตอนเปลี่ยนสถานะเท่านั้น)
        if (this.isPressingDown != pressingDown) {
            // เก็บตำแหน่งเท้าปัจจุบันไว้ก่อนเปลี่ยน
            double bottomY = y + (isPressingDown ? PRONE_HEIGHT : height);

            this.isPressingDown = pressingDown;
            y = bottomY - (pressingDown ? PRONE_HEIGHT : height);
            // หลังเปลี่ยนท่า ให้เท้ายังอยู่ที่เดิม
            logger.info("Action: Set Pressing Down | New State: {} | Position adjusted to y={}", pressingDown, y);
        }
    }


    public void setRespawnPosition(double x, double y) {
        this.respawnX = x;
        this.respawnY = y;
        logger.info("Action: Set Respawn Position | RespawnX: {} | RespawnY: {}", x, y);
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
            if (dx > 0) {
                newState = "RUN_AIM_DOWN";  // 🔹 เดินขวา + หมอบ → ยิงเฉียงลงขวา
                aimAngle = 315;              // ทิศเฉียงลงขวา
            } else if (dx < 0) {
                newState = "RUN_AIM_DOWN";  // 🔹 เดินซ้าย + หมอบ → ยิงเฉียงลงซ้าย
                aimAngle = 225;              // ทิศเฉียงลงซ้าย
            } else {
                newState = "CROUCH";
                aimAngle = 270;              // หมอบยิงตรงลง (ถ้าต้องการ)
            }
        }

        else if (!onGround) {
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
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeRect(hitbox.getMinX(), hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight());
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

    // ⭐️ --- (3) แก้ไขเมธอด shoot() ---
    public List<Bullet> shoot(double screenWidth, double screenHeight) {
        fireCooldown = fireRate;
        logger.info("Player fired {} shot at angle {}° - Position: (x: {}, y: {})",
            weaponType, aimAngle, x, y);
        List<Bullet> bullets = new ArrayList<>();
        double bulletSpeed = 10;

        Rectangle2D hitbox = getBounds();
        // ปล่อยกระสุนจากกลางตัวละคร
        double fireX = hitbox.getMinX() + hitbox.getWidth() / 2;
        double fireY = isPressingDown
                ? hitbox.getMinY() + hitbox.getHeight() * 0.6
                : hitbox.getMinY() + hitbox.getHeight() * 0.4;

        double velocityX = Math.cos(Math.toRadians(aimAngle)) * bulletSpeed;
        double velocityY = -Math.sin(Math.toRadians(aimAngle)) * bulletSpeed;

        // ⭐️ (A) กำหนดขนาดกระสุน
        double bulletWidth = 10;
        double bulletHeight = 10;

        // ⭐️ (B) เช็คว่า Sprite โหลดสำเร็จหรือไม่
        if (bulletSpriteSheet == null || bulletFrame == null) {
            logger.warn("Bullet sprite not loaded! Using fallback color.");
            // ถ้าโหลดไม่สำเร็จ, กลับไปใช้โค้ดเดิมที่ใช้ Color (Fallback)
            return shootFallback(screenWidth, screenHeight, fireX, fireY, velocityX, velocityY, bulletSpeed);
        }

        switch (weaponType) {
            case NORMAL:
            case MACHINE_GUN:
                // ⭐️ (C) เรียก Constructor ตัวใหม่ (แบบ Sprite)
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
                // ⭐️ (D) เราสามารถใช้ Sprite + ขนาดที่กำหนดเองได้
                // (คุณอาจจะต้องสร้าง laserFrame แยกต่างหากใน Constructor)
                bullets.add(new Bullet(fireX, fireY, velocityX * 2, velocityY * 2,
                        bulletSpriteSheet, bulletFrame, // ⭐️ (ควรใช้ laserFrame)
                        2, 100, // ขนาด Laser
                        screenWidth, screenHeight));
                break;
            case FIRE:
                // ⭐️ (D)
                bullets.add(new Bullet(fireX, fireY, velocityX, velocityY,
                        bulletSpriteSheet, bulletFrame, // ⭐️ (ควรใช้ fireFrame)
                        10, 10, // ขนาด Fire
                        screenWidth, screenHeight));
                break;
        }

        logger.debug("Player fired bullet: " + bullets.size());
        return bullets;
    }

    // ⭐️ เมธอดสำรอง: ถ้า Sprite โหลดไม่ขึ้น ให้กลับไปยิงกระสุนสี
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
    // ⭐️ --- สิ้นสุดการแก้ไข shoot() ---

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
        invincibilityTimer = 999999999; // 5 seconds of invincibility (60 frames per second)
    }

    // ⭐️ --- (9) อัปเดต getBounds ให้ใช้ขนาดของท่าหมอบที่ถูกต้อง ---
    public Rectangle2D getBounds() {
        double w = (isPressingDown && dx == 0) ? PRONE_WIDTH : width;
        double h = (isPressingDown && dx == 0) ? PRONE_HEIGHT : height;

        // ปรับ Y ให้ด้านล่าง hitbox อยู่ตำแหน่งเดิม
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
    public boolean isFacingRight() {return facingRight;}


}
