package oc.projet03.game.entitys;

import oc.projet03.enginers.PlayerInputProcess;
import oc.projet03.game.Game;
import oc.projet03.game.GameStat;

import java.util.Scanner;

public class HumanPlayer extends CraftPlayer {
    public final String username;
    public HumanPlayer(Game g, String us) {
        super(g);
        username = us;
    }
    @Override
    public Thread speakListener() {
        return new Thread(() -> {
            while(game().getActualStat()!= GameStat.SHUTDOWN && game().getActualPlayer()==this){
                game().log(3, "En attente du joueur.");
                new PlayerInputProcess(new Scanner(System.in).nextLine(), this).run();
            }
        });
    }
}
