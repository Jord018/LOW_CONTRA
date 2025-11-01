package com.example.contrabossclone.model;

import com.example.contrabossclone.model.Boss.Boss;
import com.example.contrabossclone.model.Boss.SecondBoss;
import com.example.contrabossclone.model.Boss.ThirdBoss;
import com.example.contrabossclone.model.Enemy.Enemy;
import com.example.contrabossclone.model.Items.PowerUp;
import com.example.contrabossclone.model.MachanicShoot.AimShoot;
import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.MachanicShoot.DirectShoot;
import com.example.contrabossclone.model.MachanicShoot.ProjectileShoot;
import com.example.contrabossclone.model.Stage.Level;
import com.example.contrabossclone.model.Stage.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private Player player;
    private Enemy enemy;
    private List<Level> levels = new ArrayList<>();
    private int currentLevelIndex = 0;
    private int currentStage = 1; // Track current stage (1-3)

    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Bullet> bossBullets = new ArrayList<>();

    public List<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

    private List<Bullet> enemyBullets = new ArrayList<>();

    // ⭐️ (1) เราโหลด Sprite ของกระสุนบอสไว้ที่นี่
    private transient Image bossBulletSheet;
    private Rectangle2D bossBulletFrame;

    private boolean gameOver = false;
    private String gameOverMessage = "";

    private double width;
    private double height;

    public GameModel(double width, double height) {
        this.width = width;
        this.height = height;
        player = new Player(width / 2 - 25, height - 50);

        // ⭐️ (2) เราโหลด Sprite แค่ครั้งเดียว
        try {
            this.bossBulletSheet = new Image(getClass().getResourceAsStream("/GameAssets/BossWallBullet.png"));
            // ⭐️⭐️ (สำคัญ) แก้พิกัด (sX, sY, sW, sH) ให้ตรงกับไฟล์รูปของคุณ
            this.bossBulletFrame = new Rectangle2D(0, 0, 124, 126); // (sX, sY, sW, sH)
        } catch (Exception e) {
            System.err.println("!!! Error loading boss bullet sprite!");
            this.bossBulletSheet = null;
        }

        // Initialize all stages
        initializeStage1();
        initializeStage2();
        initializeStage3(); // ⭐️ (เปิดใช้งานด่าน 3)

    }

    /**
     * Stage 1: Tutorial stage with multiple weak bosses and all power-ups available
     */
    private void initializeStage1() {
        List<Platform> platforms = new ArrayList<>();
        platforms.add(new Platform(0, 280, 260, 20));
        platforms.add(new Platform(260, 370, 70, 20));
        platforms.add(new Platform(50, 420, 210, 20));
        platforms.add(new Platform(330, 460, 70, 20));
        
        List<PowerUp> powerUps = new ArrayList<>();
        powerUps.add(new PowerUp(100, 300, PowerUp.PowerUpType.MACHINE_GUN));
        powerUps.add(new PowerUp(200, 300, PowerUp.PowerUpType.BARRIER));
        powerUps.add(new PowerUp(300, 300, PowerUp.PowerUpType.SPREAD_GUN));
        powerUps.add(new PowerUp(400, 300, PowerUp.PowerUpType.LASER));
        powerUps.add(new PowerUp(500, 300, PowerUp.PowerUpType.FIRE));

        List<Boss> bosses = new ArrayList<>();
        // ⭐️ (3) ส่ง Sprite ที่โหลดไว้ เข้าไปใน Constructor
        bosses.add(new Boss(440, 300,40,40, player, new ProjectileShoot(bossBulletSheet, bossBulletFrame)));
        bosses.add(new Boss(520, 300, 40,40,player, new ProjectileShoot(bossBulletSheet, bossBulletFrame)));
        // ⭐️ (แก้ไข) ลบ 'false' ที่เกินมา และส่ง Sprite เข้าไป
        bosses.add(new Boss(460, 330, 100,200,player, new DirectShoot(bossBulletSheet, bossBulletFrame)));

        List<Enemy> enemies = new ArrayList<>();
        // ⭐️ (3) ส่ง Sprite ที่โหลดไว้ เข้าไปใน Constructor ของ Enemy
        enemies.add(new Enemy(300,300,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame));

        levels.add(new Level(bosses, enemies, platforms, powerUps, "/GameAssets/MapBossWall.png",
                3150, 10, 350, 210, height - 50)); // ⭐️ (เพิ่ม groundLevel)
    }

    /**
     * Stage 2: Intermediate stage with stronger boss and more platforms
     */
    private void initializeStage2() {
        List<Platform> platforms = new ArrayList<>();

        List<PowerUp> powerUps = new ArrayList<>();
        // No power-ups in stage 2 - player must rely on what they collected in stage 1

        List<Boss> bosses = new ArrayList<>();
        // ⭐️ (3) ส่ง Sprite ที่โหลดไว้ เข้าไปใน Constructor (ของ AimShoot)
        bosses.add(new SecondBoss(330, 0, 270, 270, player, new AimShoot(bossBulletSheet, bossBulletFrame), "/GameAssets/BossJava.png"));

        List<Enemy> enemies = new ArrayList<>();
        // ⭐️ (3) ส่ง Sprite ที่โหลดไว้ เข้าไปใน Constructor ของ Enemy
        enemies.add(new Enemy(0,0,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame));

        levels.add(new Level(bosses, enemies, platforms, powerUps, "/GameAssets/MapBossJava.png",
                500, 10, 350, 210, height - 100)); // ⭐️ (เพิ่ม groundLevel)
    }

    /**
     * Stage 3: Final stage with challenging boss configuration
     */
    private void initializeStage3() {
        List<PowerUp> powerUps = new ArrayList<>();
        List<Platform> platforms = new ArrayList<>();
        platforms.add(new Platform(100, height - 100, 100, 20));
        platforms.add(new Platform(width - 200, height - 100, 100, 20));
        platforms.add(new Platform(350, height - 200, 100, 20));

        List<Boss> bosses = new ArrayList<>();
        // ⭐️ (3) ส่ง Sprite ที่โหลดไว้ เข้าไปใน Constructor
        bosses.add(new ThirdBoss(width - 120, height - 120, 100, 100 ,player, new ProjectileShoot(bossBulletSheet, bossBulletFrame)));
        bosses.add(new ThirdBoss(width - 150, height - 120, 100, 100 ,player, new ProjectileShoot(bossBulletSheet, bossBulletFrame)));
        bosses.add(new ThirdBoss(width - 180, height - 120, 100, 100 ,player, new ProjectileShoot(bossBulletSheet, bossBulletFrame)));

        List<Enemy> enemies = new ArrayList<>();
        // ⭐️ (3) ส่ง Sprite ที่โหลดไว้ เข้าไปใน Constructor ของ Enemy
        enemies.add(new Enemy(0,0,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame));

        levels.add(new Level(bosses, enemies, platforms, powerUps, "/GameAssets/MapBossJava.png",
                2400, 10, 350, 210, height - 50)); // ⭐️ (เพิ่ม groundLevel)
    }


    public void update() {
        Level currentLevel = levels.get(currentLevelIndex);

        // ⭐️ (4) ส่ง groundLevel ของด่านปัจจุบันไปให้ Player
        player.update(currentLevel.getPlatforms(), currentLevel.getGroundLevel());

        for (Boss boss : currentLevel.getBosses()) {
            boss.update();
        }

        // Update enemies and handle their shooting
        for (Enemy enemy : currentLevel.getEnemies()) {
            // ⭐️ (4) ส่ง groundLevel ของด่านปัจจุบันไปให้ Enemy
            enemy.update(currentLevel.getPlatforms(), currentLevel.getGroundLevel());

            // Handle enemy shooting
            if (enemy.isAlive()) {
                // ⭐️ (5) ส่งขนาดจอ (width, height) เข้าไปใน shoot()
                // (เพื่อให้ Bullet รู้ขอบเขต)
                List<Bullet> newEnemyBullets = enemy.shoot(width, height);
                if (newEnemyBullets != null && !newEnemyBullets.isEmpty()) {
                    enemyBullets.addAll(newEnemyBullets);
                }
            }
        }
        
        // Boss shooting
        for (Boss boss : currentLevel.getBosses()) {
            List<Bullet> newBossBullets = boss.shoot(width, height);
            if (newBossBullets != null && !newBossBullets.isEmpty()) {
                bossBullets.addAll(newBossBullets);
            }
        }

        // Update player bullets
        List<Bullet> playerBulletsToRemove = new ArrayList<>();
        for (Bullet bullet : playerBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(width, height)) {
                playerBulletsToRemove.add(bullet);
            }
        }
        playerBullets.removeAll(playerBulletsToRemove);

        // Update enemy bullets
        List<Bullet> enemyBulletsToRemove = new ArrayList<>();
        for (Bullet bullet : enemyBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(width, height)) {
                enemyBulletsToRemove.add(bullet);
            }
        }
        enemyBullets.removeAll(enemyBulletsToRemove);


        // Collision detection: player Bullet vs enemy
        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Bullet bullet : playerBullets) {
            for (Enemy enemy : currentLevel.getEnemies()) {
                if (enemy.isAlive() && bullet.getBounds().intersects(enemy.getBounds())) {
                    System.out.println("Player hit enemy at ({}, {})" + enemy.getX() + enemy.getY());
                    enemy.die();
                    playerBulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                    break; // Exit inner loop once bullet hits an enemy
                }
            }
        }
        // Remove hit enemies
        currentLevel.getEnemies().removeAll(enemiesToRemove);
        playerBullets.removeAll(playerBulletsToRemove);

        // Collision detection: enemy bullets vs player
        for (Bullet bullet : enemyBullets) {
            if (bullet.getBounds().intersects(player.getBounds())) {
                player.hit();
                enemyBulletsToRemove.add(bullet);
                System.out.println("Player hit by enemy bullet!"); // Debug message
            }
        }
        enemyBullets.removeAll(enemyBulletsToRemove);


        // Update boss bullets
        List<Bullet> bossBulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bossBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(width, height)) {
                bossBulletsToRemove.add(bullet);
            }
        }
        bossBullets.removeAll(bossBulletsToRemove);

        // Collision detection: player bullets vs boss
        for (Bullet bullet : playerBullets) {
            for (Boss boss : currentLevel.getBosses()) {
                if (bullet.getBounds().intersects(boss.getBounds())) {
                    boss.hit();
                    playerBulletsToRemove.add(bullet);
                }
            }
        }
        playerBullets.removeAll(playerBulletsToRemove);

        // Collision detection: boss bullets vs player
        for (Bullet bullet : bossBullets) {
            if (bullet.getBounds().intersects(player.getBounds())) {
                player.hit();
                bossBulletsToRemove.add(bullet);
            }
        }
        bossBullets.removeAll(bossBulletsToRemove);

        // Collision detection: player vs boss bodies
        for (Boss boss : currentLevel.getBosses()) {
            if (boss.getBounds().intersects(player.getBounds())) {
                player.hit();
            }
        }
        // Collision detection: player vs enemy
        for (Enemy enemy : currentLevel.getEnemies()) {
            if(enemy.getBounds().intersects(player.getBounds())) {
                player.hit();
            };
        }
        // Collision detection: player vs power-ups
        List<PowerUp> powerUpsToRemove = new ArrayList<>();
        for (PowerUp powerUp : currentLevel.getPowerUps()) {
            if (player.getBounds().intersects(powerUp.getBounds())) {
                switch (powerUp.getType()) {
                    case BARRIER:
                        player.activateBarrier();
                        break;
                    case MACHINE_GUN:
                        player.setWeaponType(Player.WeaponType.MACHINE_GUN);
                        break;
                    case SPREAD_GUN:
                        player.setWeaponType(Player.WeaponType.SPREAD_GUN);
                        break;
                    case LASER:
                        player.setWeaponType(Player.WeaponType.LASER);
                        break;
                    case FIRE:
                        player.setWeaponType(Player.WeaponType.FIRE);
                        break;
                }
                powerUpsToRemove.add(powerUp);
            }
        }
        currentLevel.getPowerUps().removeAll(powerUpsToRemove);

        // Remove defeated bosses
        currentLevel.getBosses().removeIf(Boss::isDefeated);
//Score Update
        if (currentLevel.getBosses().isEmpty() && currentLevel.getEnemies().isEmpty()) { // ⭐️ (เพิ่มเช็ค Enemy)
            player.setScore(player.getScore() + 1);
            if (currentLevelIndex < levels.size() - 1) {
                currentLevelIndex++;
                currentStage = currentLevelIndex + 1; // Update stage number
                player.setX(width / 2 - 25);
                player.setY(height - 50);
            } else {
                gameOver = true;
                gameOverMessage = "You Win! All 3 Stages Completed!" + player.getScore();
            }
        }

        if (player.getLives() <= 0) {
            gameOver = true;
            gameOverMessage = "Game Over";
        }

    }

    public void resize(double newWidth, double newHeight) {
        double scaleX = newWidth / this.width;
        double scaleY = newHeight / this.height;

        player.setX(player.getX() * scaleX);
        player.setY(player.getY() * scaleY);
        player.setRespawnPosition(newWidth / 2 - 25, newHeight - 50);

        // Adjust positions of existing bullets (optional, can also clear and re-add)
        for (Bullet bullet : playerBullets) {
            bullet.setX(bullet.getX() * scaleX);
            bullet.setY(bullet.getY() * scaleY);
        }

        for (Bullet bullet : bossBullets) {
            bullet.setX(bullet.getX() * scaleX);
            bullet.setY(bullet.getY() * scaleY);
        }

        for (Bullet bullet : enemyBullets) { // ⭐️ (เพิ่ม Enemy bullets)
            bullet.setX(bullet.getX() * scaleX);
            bullet.setY(bullet.getY() * scaleY);
        }

        // Adjust positions of platforms, bosses, powerups in current level
        Level currentLevel = levels.get(currentLevelIndex);
        for (Platform platform : currentLevel.getPlatforms()) {
            platform.setX(platform.getX() * scaleX);
            platform.setY(platform.getY() * scaleY);
        }
        for (Boss boss : currentLevel.getBosses()) {
            boss.setX(boss.getX() * scaleX);
            boss.setY(boss.getY() * scaleY);
        }

        for (Enemy enemy : currentLevel.getEnemies()) { // ⭐️ (เพิ่ม Enemy)
            enemy.setX(enemy.getX() * scaleX);
            enemy.setY(enemy.getY() * scaleY);
        }

        for (PowerUp powerUp : currentLevel.getPowerUps()) {
            powerUp.setX(powerUp.getX() * scaleX);
            powerUp.setY(powerUp.getY() * scaleY);
        }

        this.width = newWidth;
        this.height = newHeight;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public List<Bullet> getPlayerBullets() {
        return playerBullets;
    }

    public List<Bullet> getBossBullets() {
        return bossBullets;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getGameOverMessage() {
        return gameOverMessage;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    /**
     * Returns the current stage number (1-3)
     */
    public int getCurrentStage() {
        return currentStage;
    }
}
