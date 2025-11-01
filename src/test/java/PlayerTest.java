import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.model.Player;
import com.example.contrabossclone.model.Stage.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        player = new Player(100, 100);
        gameModel = new GameModel(900,600);
    }

    @Test
    void PlayerShouldDeath_WhenHealthIsZero() {
        player.setLives(0);
        assertTrue(player.isDefeated());
    }
    @Test
    void PlayerShouldMoveRight_WhenMoveRightIsCalled() {
        Player player = gameModel.getPlayer();
        player.moveRight();
        System.out.println(player.getX());
        gameModel.update();
        System.out.println(player.getX());
        assertEquals(430,player.getX());
    }
    @Test
    void PlayerShouldMoveLeft_WhenMoveLeftIsCalled() {
        Player player = gameModel.getPlayer();
        player.moveLeft();
        gameModel.update();
        assertEquals(420,player.getX());
    }
    @Test
    void PlayerShouldJump_WhenJumpIsCalled() {
        Player player = gameModel.getPlayer();
        System.out.println(player.getY());
        player.jump();
        gameModel.update();
        System.out.println(player.getY());
        assertEquals(540,player.getY());
    }
    @Test
    void PlayerShouldShoot_WhenShootIsCalled() {
        Player player = gameModel.getPlayer();
        
        // Arrange: Player starts with ability to shoot
        assertTrue(player.canShoot(), "Player should be able to shoot initially");
        
        // Act: Player shoots
        var bullets = player.shoot(900, 600);
        
        // Assert: Bullets are created and cooldown is set
        assertNotNull(bullets, "Bullets list should not be null");
        assertEquals(1, bullets.size(), "Normal weapon should create 1 bullet");
        assertFalse(player.canShoot(), "Player should not be able to shoot immediately after (cooldown active)");
        
        // Test different weapon types
        player.setWeaponType(Player.WeaponType.SPREAD_GUN);
        // Wait for cooldown to reset
        for (int i = 0; i < 30; i++) {
            player.update(gameModel.getLevels().get(0).getPlatforms(), 600);
        }
        assertTrue(player.canShoot(), "Player should be able to shoot after cooldown");
        
        bullets = player.shoot(900, 600);
        assertEquals(3, bullets.size(), "Spread gun should create 3 bullets");
    }
    @Test
    void testScoreIncreasesAndLevelAdvancesWhenBossDefeated() {
        Player player = gameModel.getPlayer();
        int oldScore = player.getScore();
        int oldLevelIndex = gameModel.getCurrentLevelIndex();

        // ทำให้บอสในด่านปัจจุบันหมด (จำลองว่าตายหมดแล้ว)
        Level currentLevel = gameModel.getLevels().get(oldLevelIndex);
        currentLevel.getBosses().clear(); // ไม่มีบอสเหลือแล้ว

        // เรียกอัปเดตหนึ่งรอบ
        gameModel.update();

        // ตรวจว่าคะแนนเพิ่มขึ้น
        assertEquals(oldScore + 1, player.getScore(), "Player score should increase when bosses are all defeated");

        // ตรวจว่ามีการเปลี่ยนด่าน (ถ้าไม่ใช่ด่านสุดท้าย)
        if (oldLevelIndex < gameModel.getLevels().size() - 1) {
            assertEquals(oldLevelIndex + 1, gameModel.getCurrentLevelIndex(), "Game should advance to next level");
            assertFalse(gameModel.isGameOver(), "Game should not be over yet");
        } else {
            assertTrue(gameModel.isGameOver(), "Game should be over after last level");
            assertTrue(gameModel.getGameOverMessage().contains("You Win!"), "Should show win message");
        }
    }
    @Test
    void PlayerShouldRespawn_WhenHit(){}
}
