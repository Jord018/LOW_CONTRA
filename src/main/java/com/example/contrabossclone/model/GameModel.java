package com.example.contrabossclone.model;

import com.example.contrabossclone.model.Boss.Boss;
import com.example.contrabossclone.model.Boss.SecondBoss;
import com.example.contrabossclone.model.Boss.ThirdBoss;
import com.example.contrabossclone.model.Enemy.Enemy;
import com.example.contrabossclone.model.Items.PowerUp;
import com.example.contrabossclone.model.MachanicShoot.*;
import com.example.contrabossclone.model.Stage.Level;
import com.example.contrabossclone.model.Stage.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameModel {

    private Player player;
    private Enemy enemy;
    private List<Level> levels = new ArrayList<>();
    private int currentLevelIndex = 0;
    private int currentStage = 1; // Track current stage (1-3)
    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Bullet> bossBullets = new ArrayList<>();
    public List<Bullet> getEnemyBullets() { return enemyBullets; }
    private List<Bullet> enemyBullets = new ArrayList<>();
    private transient Image bossBulletSheet;
    private transient Image bossJavaBulletSheet;
    private Rectangle2D bossBulletFrame;
    private Rectangle2D bossJavaBulletFrame;
    private Rectangle2D[] bossJavaBulletFrames;

    private transient Image powerUpSpriteSheet;
    private Map<PowerUp.PowerUpType, Rectangle2D> powerUpFrames;

    private boolean gameOver = false;
    private String gameOverMessage = "";

    private double width;
    private double height;


    public GameModel(double width, double height) {
        this.width = width;
        this.height = height;
        player = new Player(width / 2 - 25, height - 50);

        try {
            this.bossBulletSheet = new Image(getClass().getResourceAsStream("/GameAssets/BossWallBullet.png"));
            this.bossBulletFrame = new Rectangle2D(0, 0, 124, 126); // (sX, sY, sW, sH)
        } catch (Exception e) {
            System.err.println("!!! Error loading boss bullet sprite!");
            this.bossBulletSheet = null;
        }

        try {
            this.bossJavaBulletSheet = new Image(getClass().getResourceAsStream("/GameAssets/BossJavaBullet.png"));
            this.bossJavaBulletFrames = new Rectangle2D[] {
                    new Rectangle2D(0, 0, 93.75, 117),
                    new Rectangle2D(93.75, 0, 93.75, 117),
                    new Rectangle2D(187.5, 0, 93.75, 117),
                    new Rectangle2D(281.25, 0, 93.75, 117)
            };
        } catch (Exception e) {
            System.err.println("!!! Error loading boss bullet sprite!");
            this.bossJavaBulletSheet = null;
        }

        this.powerUpFrames = new HashMap<>();

        try {
            this.powerUpSpriteSheet = new Image(getClass().getResourceAsStream("/GameAssets/SpecialGun.png"));
            powerUpFrames.put(PowerUp.PowerUpType.MACHINE_GUN, new Rectangle2D(24.8, 0, 24.8, 16));
            powerUpFrames.put(PowerUp.PowerUpType.BARRIER, new Rectangle2D(49.6, 16, 24.8, 16));
            powerUpFrames.put(PowerUp.PowerUpType.SPREAD_GUN, new Rectangle2D(49.6, 0, 24.8, 16));
            powerUpFrames.put(PowerUp.PowerUpType.LASER, new Rectangle2D(99.2, 16, 24.8, 16));
            powerUpFrames.put(PowerUp.PowerUpType.FIRE, new Rectangle2D(0, 0, 24.8, 16));

        } catch (Exception e) {
            System.err.println("!!! Error loading PowerUp sprite sheet!");
            this.powerUpSpriteSheet = null;
        }

        try {
            Image explosionSheet = new Image(getClass().getResourceAsStream("/GameAssets/BulletExplode.png"));

            Rectangle2D[] frames = new Rectangle2D[] {
                    new Rectangle2D(0, 0, 279, 283)
            };

            Bullet.loadExplosionSprite(explosionSheet, frames);

        } catch (Exception e) {
            System.err.println("!!! Error loading Explosion sprite sheet!");
        }

        // Initialize all stages
        initializeStage1();
        initializeStage2();
        initializeStage3();

        if (!levels.isEmpty()) {
            Level startLevel = levels.get(0);
            player.setX(startLevel.getStartX());
            player.setY(startLevel.getStartY());
        }

    }


    private void initializeStage1() {
        List<Platform> platforms = new ArrayList<>();
        platforms.add(new Platform(0, 280, 260, 20));
        platforms.add(new Platform(260, 370, 70, 20));
        platforms.add(new Platform(50, 420, 210, 20));
        platforms.add(new Platform(330, 460, 70, 20));

        List<PowerUp> powerUps = new ArrayList<>();
        double itemWidth = 74.4, itemHeight = 48;

        powerUps.add(new PowerUp(50, 100, PowerUp.PowerUpType.MACHINE_GUN, powerUpSpriteSheet, powerUpFrames.get(PowerUp.PowerUpType.MACHINE_GUN), itemWidth, itemHeight));
        powerUps.add(new PowerUp(150, 300, PowerUp.PowerUpType.BARRIER, powerUpSpriteSheet, powerUpFrames.get(PowerUp.PowerUpType.BARRIER), itemWidth, itemHeight));
        powerUps.add(new PowerUp(350, 100, PowerUp.PowerUpType.SPREAD_GUN, powerUpSpriteSheet, powerUpFrames.get(PowerUp.PowerUpType.SPREAD_GUN), itemWidth, itemHeight));
//        powerUps.add(new PowerUp(400, 300, PowerUp.PowerUpType.LASER, powerUpSpriteSheet, powerUpFrames.get(PowerUp.PowerUpType.LASER), itemWidth, itemHeight));
        powerUps.add(new PowerUp(300, 500, PowerUp.PowerUpType.FIRE, powerUpSpriteSheet, powerUpFrames.get(PowerUp.PowerUpType.FIRE), itemWidth, itemHeight));

        List<Boss> bosses = new ArrayList<>();
        bosses.add(new Boss(440, 300,40,40, player, new ProjectileShoot(bossBulletSheet, bossBulletFrame),true,1));
        bosses.add(new Boss(520, 300, 40,40,player, new ProjectileShoot(bossBulletSheet, bossBulletFrame),true,1));
        bosses.add(new Boss(460, 400, 100,100,player, new DirectShoot(bossBulletSheet, bossBulletFrame),false,1));

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(300,300,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame,1));
        enemies.add(new Enemy(500,300,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame,1));


        levels.add(new Level(bosses, enemies, platforms, powerUps, "/GameAssets/MapBossWall.png", 3150, 10, 350, 210, height - 50,10, 10));
    }


    private void initializeStage2() {
        List<Platform> platforms = new ArrayList<>();

        List<PowerUp> powerUps = new ArrayList<>();

        List<Boss> bosses = new ArrayList<>();
        bosses.add(new SecondBoss(330, 0, 270, 270, player, new JAVA(bossJavaBulletSheet, bossJavaBulletFrames), "/GameAssets/BossJava.png",2));

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(150,300,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame,1));

        platforms.add(new Platform(0, 400, 90, 500, true));
        platforms.add(new Platform(290, 200, 90, 800, true));

        levels.add(new Level(bosses, enemies, platforms, powerUps, "/GameAssets/MapBossJava.png", 500, 10, 350, 210, height - 100,10, 10));
    }


    private void initializeStage3() {
        List<PowerUp> powerUps = new ArrayList<>();
        List<Platform> platforms = new ArrayList<>();

        List<Boss> bosses = new ArrayList<>();

        bosses.add(new ThirdBoss(350, 15, 100, 100 ,player, new SpiralShoot(bossBulletSheet, bossBulletFrame),2));
        bosses.add(new ThirdBoss(650, 15, 100, 100 ,player, new SpiralShoot(bossBulletSheet, bossBulletFrame),2));
        bosses.add(new ThirdBoss(50, 15, 100, 100 ,player, new SpiralShoot(bossBulletSheet, bossBulletFrame),2));

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(300,300,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame,1));
        enemies.add(new Enemy(400,300,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame,1));
        enemies.add(new Enemy(500,300,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame,1));
        enemies.add(new Enemy(600,300,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame,1));
        enemies.add(new Enemy(700,300,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame,1));
        enemies.add(new Enemy(800,300,player,"/GameAssets/Enemy2.png", bossBulletSheet, bossBulletFrame,1));


        levels.add(new Level(bosses, enemies, platforms, powerUps, "/GameAssets/MapBossJava.png", 2400, 10, 350, 210, height - 100,300, 300));
    }


    public void update() {
        Level currentLevel = levels.get(currentLevelIndex);
        player.update(currentLevel.getPlatforms(), currentLevel.getGroundLevel());

        if (player.getX() < 0) { player.setX(0); }
        if (player.getBounds().getMaxX() > width) { player.setX(width - player.getWidth()); }

        for (Boss boss : currentLevel.getBosses()) {
            boss.update();
        }

        for (Enemy enemy : currentLevel.getEnemies()) {
            enemy.update(currentLevel.getPlatforms(), currentLevel.getGroundLevel());
            if (enemy.isAlive()) {
                List<Bullet> newEnemyBullets = enemy.shoot(width, height);
                if (newEnemyBullets != null && !newEnemyBullets.isEmpty()) {
                    enemyBullets.addAll(newEnemyBullets);
                }
            }
        }

        for (Enemy enemy : currentLevel.getEnemies()) {
            if (enemy.isAlive() && enemy.getBounds().intersects(player.getBounds())) {
                player.hit();
            }
        }

        List<Bullet> playerBulletsToRemove = new ArrayList<>();
        for (Bullet bullet : playerBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(width, height) || bullet.isFinished()) {
                playerBulletsToRemove.add(bullet);
            }
        }
        playerBullets.removeAll(playerBulletsToRemove);

        List<Bullet> enemyBulletsToRemove = new ArrayList<>();
        for (Bullet bullet : enemyBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(width, height) || bullet.isFinished()) {
                enemyBulletsToRemove.add(bullet);
            }
        }
        enemyBullets.removeAll(enemyBulletsToRemove);

        List<Bullet> bossBulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bossBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(width, height) || bullet.isFinished()) {
                bossBulletsToRemove.add(bullet);
            }
        }
        bossBullets.removeAll(bossBulletsToRemove);

        boolean enemiesCleared = currentLevel.getEnemies().isEmpty();

        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Bullet bullet : playerBullets) {
            for (Enemy enemy : currentLevel.getEnemies()) {
                if (enemy.isAlive() && !bullet.isExploding() && bullet.getBounds().intersects(enemy.getBounds())) {
                    enemy.die();
                    bullet.explode();
                    player.setScore(player.getScore() + enemy.getScore());
                    enemiesToRemove.add(enemy);
                    break;
                }
            }
        }
        currentLevel.getEnemies().removeAll(enemiesToRemove);

        for (Bullet bullet : enemyBullets) {
            if (!bullet.isExploding() && bullet.getBounds().intersects(player.getBounds())) {
                player.hit();
                bullet.explode();
                System.out.println("Player hit by enemy bullet!");
            }
        }

        if (enemiesCleared) {
            for (Boss boss : currentLevel.getBosses()) {
                List<Bullet> newBossBullets = boss.shoot(width, height);
                if (newBossBullets != null && !newBossBullets.isEmpty()) {
                    bossBullets.addAll(newBossBullets);
                }
            }

            for (Bullet bullet : playerBullets) {
                for (Boss boss : currentLevel.getBosses()) {
                    if (!bullet.isExploding() && bullet.getBounds().intersects(boss.getBounds())) {
                        boolean wasAlive = !boss.isDefeated(); // ตรวจสถานะก่อนโดนยิง
                        boss.hit();
                        bullet.explode();

                        // ให้คะแนนทันทีตอน HP หมด (ก่อนโดนลบ)
                        if (wasAlive && boss.isDefeated()) {
                            player.setScore(player.getScore() + boss.getScore());
                        }
                    }

                }
            }
        }

        playerBullets.removeAll(playerBulletsToRemove);

        for (Bullet bullet : bossBullets) {
            if (!bullet.isExploding() && bullet.getBounds().intersects(player.getBounds())) {
                player.hit();
                bullet.explode();
            }
        }

        if (enemiesCleared) {
            for (Boss boss : currentLevel.getBosses()) {
                if (boss.getBounds().intersects(player.getBounds())) {
                    player.hit();
                }
            }
        }

        List<PowerUp> powerUpsToRemove = new ArrayList<>();
        for (PowerUp powerUp : currentLevel.getPowerUps()) {
            if (player.getBounds().intersects(powerUp.getBounds())) {
                switch (powerUp.getType()) {
                    case BARRIER -> player.activateBarrier();
                    case MACHINE_GUN -> player.setWeaponType(Player.WeaponType.MACHINE_GUN);
                    case SPREAD_GUN -> player.setWeaponType(Player.WeaponType.SPREAD_GUN);
                    case LASER -> player.setWeaponType(Player.WeaponType.LASER);
                    case FIRE -> player.setWeaponType(Player.WeaponType.FIRE);
                }
                powerUpsToRemove.add(powerUp);
            }
        }
        currentLevel.getPowerUps().removeAll(powerUpsToRemove);



        if (currentLevel.getBosses().isEmpty() && currentLevel.getEnemies().isEmpty()) {
            if (enemiesCleared) {
                currentLevel.getBosses().removeIf(Boss::isDefeated);
            }

            if (enemiesCleared && currentLevel.getBosses().isEmpty()) {
                if (currentLevelIndex < levels.size() - 1) {
                    currentLevelIndex++;
                    currentStage = currentLevelIndex + 1;

                    Level newLevel = levels.get(currentLevelIndex);
                    player.setX(newLevel.getStartX());
                    player.setY(newLevel.getStartY());
                } else {
                    gameOver = true;
                    gameOverMessage = "You Win! All Stages Completed! Score: " + player.getScore();
                }
            }

            if (player.getLives() <= 0) {
                gameOver = true;
                gameOverMessage = "Game Over";
            }
        }
        currentLevel.getBosses().removeIf(Boss::isDefeated);
    }



    public void resize(double newWidth, double newHeight) {
        double scaleX = newWidth / this.width;
        double scaleY = newHeight / this.height;

        player.setX(player.getX() * scaleX);
        player.setY(player.getY() * scaleY);
        player.setRespawnPosition(newWidth / 2 - 25, newHeight - 50);

        for (Bullet bullet : playerBullets) {
            bullet.setX(bullet.getX() * scaleX);
            bullet.setY(bullet.getY() * scaleY);
        }

        for (Bullet bullet : bossBullets) {
            bullet.setX(bullet.getX() * scaleX);
            bullet.setY(bullet.getY() * scaleY);
        }

        for (Bullet bullet : enemyBullets) {
            bullet.setX(bullet.getX() * scaleX);
            bullet.setY(bullet.getY() * scaleY);
        }

        Level currentLevel = levels.get(currentLevelIndex);

        for (Platform platform : currentLevel.getPlatforms()) {
            platform.setX(platform.getX() * scaleX);
            platform.setY(platform.getY() * scaleY);
        }

        for (Boss boss : currentLevel.getBosses()) {
            boss.setX(boss.getX() * scaleX);
            boss.setY(boss.getY() * scaleY);
        }

        for (Enemy enemy : currentLevel.getEnemies()) {
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


    public Player getPlayer() { return player; }

    public List<Level> getLevels() { return levels; }

    public int getCurrentLevelIndex() { return currentLevelIndex; }

    public List<Bullet> getPlayerBullets() { return playerBullets; }

    public List<Bullet> getBossBullets() { return bossBullets; }

    public boolean isGameOver() { return gameOver; }

    public String getGameOverMessage() { return gameOverMessage; }

    public double getWidth() { return width; }

    public double getHeight() { return height; }

    public int getCurrentStage() { return currentStage; }
}
