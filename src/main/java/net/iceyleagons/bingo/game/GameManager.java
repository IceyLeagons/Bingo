package net.iceyleagons.bingo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.icicle.annotations.Autowired;
import net.iceyleagons.icicle.annotations.Service;
import org.bukkit.World;

@Getter
@Service
@AllArgsConstructor(onConstructor__ = @Autowired)
public class GameManager {

    private final Main main;

    public World getAndGenerateWorld(Game game) {
        //TODO
        return null;
    }

}
