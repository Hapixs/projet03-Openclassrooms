package oc.projet03.game.entitys;

import oc.projet03.enginers.PlayerInputProcess;
import oc.projet03.game.Game;
import oc.projet03.game.GameStat;

import java.util.Objects;

public class OrdiPlayer extends CraftPlayer {
    private String next = "";
    public OrdiPlayer(Game g) {
        super(g);
    }
    @Override
    public Thread speakListener() {
        return new Thread(() -> {
            while(game().getActualStat()!= GameStat.SHUTDOWN && game().getActualPlayer()==this) if(!Objects.equals(next, "")){
                game().log(3, "En attente de l'ordi");
                new PlayerInputProcess(next, this).run();
                next = "";
            }
        });
    }
}
