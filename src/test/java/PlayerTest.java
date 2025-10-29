import com.example.contrabossclone.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(100, 100);
    }

    @Test
    void PlayerShouldDeath_WhenHealthIsZero() {
        player.setLives(0);
        assertTrue(player.isDefeated());
    }
}
