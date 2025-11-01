package com.example.contrabossclone.model.Boss;

import com.example.contrabossclone.model.MachanicShoot.ShootingStrategy;
import com.example.contrabossclone.model.Player;
import javafx.geometry.Rectangle2D; // ⭐️ เพิ่ม
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image; // ⭐️ เพิ่ม
import javafx.scene.paint.Color;
// ⭐️ (ลบ Map และ HashMap imports)

public class SecondBoss extends Boss {

    // ⭐️ --- (1) เพิ่มตัวแปรสำหรับ Animation (แบบง่าย) ---
    private transient Image spriteSheet;
    private Rectangle2D[] frames; // ⭐️ ใช้ Array ธรรมดา
    private int animationFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 200; // ความเร็ว Animation (ยิ่งน้อย ยิ่งเร็ว)
    // ⭐️ (ลบ currentState)

    // ⭐️ --- (2) แก้ไข Constructor ---
    // เพิ่ม String spriteSheetPath ตัวสุดท้าย
    public SecondBoss(double x, double y, double width, double height, Player player, ShootingStrategy shootingStrategy, String spriteSheetPath) {
        // ส่งต่อไปยัง Constructor ของ Boss.java (โค้ดเดิม)
        super(x, y, width, height, player, shootingStrategy);

        // โหลด Sprite Sheet
        try {
            this.spriteSheet = new Image(getClass().getResourceAsStream(spriteSheetPath));
        } catch (Exception e) {
            System.err.println("!!! Error loading boss sprite sheet: " + spriteSheetPath);
            this.spriteSheet = null;
        }
        // เรียกเมธอดเพื่อตั้งค่า Animation
        initializeAnimations();
    }

    // ⭐️ --- (3) เพิ่มเมธอดตั้งค่า Animation (แบบง่าย) ---
    protected void initializeAnimations() {
        // ⭐️⭐️⭐️ (สำคัญ) คุณต้องใส่พิกัด (sX, sY, sW, sH) ที่ถูกต้อง ⭐️⭐️⭐️
        // ⭐️⭐️⭐️ จาก Sprite Sheet ของบอสตัวที่ 2 ⭐️⭐️⭐️
        // นี่เป็นแค่ตัวอย่าง (สมมติว่ามีท่าเดียว 2 frame):
        this.frames = new Rectangle2D[]{
                new Rectangle2D(0, 0, 113, 113), // Frame 1
                new Rectangle2D(113, 0, 113, 113)  // Frame 2
        };
    }

    // ⭐️ --- (4) Override เมธอด update() ---
    @Override
    public void update() {
        // ⭐️⭐️⭐️ สำคัญมาก! ⭐️⭐️⭐️
        // เรียก update() ของ Boss.java (ตัวแม่)
        // เพื่อให้ Logic การยิงกระสุน (cooldown) ยังทำงานเหมือนเดิม
        super.update();

        // --- Logic การอัปเดต Frame Animation (แบบง่าย) ---
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;

            if (frames != null) {
                animationFrame = (animationFrame + 1) % frames.length;
            }
        }
    }


    // ⭐️ --- (5) แก้ไขเมธอด render() ---
    @Override
    public void render(GraphicsContext gc) {
        // --- วาด Sprite (แทนที่สี่เหลี่ยมสีม่วง) ---
        if (spriteSheet != null && frames != null) {

            if (animationFrame >= frames.length) animationFrame = 0;
            Rectangle2D frame = frames[animationFrame];

            double sX = frame.getMinX(), sY = frame.getMinY();
            double sW = frame.getWidth(), sH = frame.getHeight();

            // วาด Sprite ลงจอ (ที่ตำแหน่ง x, y และขนาด width, height ของบอส)
            // (เราจะ "ยืด" Sprite ให้พอดีกับ Hitbox ที่กำหนดใน GameModel)
            gc.drawImage(spriteSheet, sX, sY, sW, sH, getX(), getY(), getWidth(), getHeight());

        } else {
            renderFallback(gc); // วาดสี่เหลี่ยมถ้าโหลด Sprite ไม่ได้
        }


        // --- วาด Health bar (โค้ดเดิม) ---
        gc.setFill(Color.WHITE);
        gc.fillRect(getX(), getY() - 20, getWidth(), 10);
        gc.setFill(Color.RED);
        gc.fillRect(getX(), getY() - 20, getWidth() * (getHealth() / 100.0), 10);
    }

    // เมธอดสำรอง
    private void renderFallback(GraphicsContext gc) {
        gc.setFill(Color.PURPLE); // สีม่วง (สีเดิมของคุณ)
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}

