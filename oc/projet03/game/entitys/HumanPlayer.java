package oc.projet03.game.entitys;

import oc.projet03.enginers.PlayerInputProcess;
import oc.projet03.game.Game;
import oc.projet03.game.GameStat;

import java.util.Scanner;

public class HumanPlayer extends CraftPlayer {
    public final String username;
    public HumanPlayer(Game g, String us, int maxTry) {
        super(g, maxTry);
        username = us;
        speakListener().start();
    }
    public Thread speakListener() {
        return new Thread(() -> {
            while(game().getActualStat()!= GameStat.SHUTDOWN){
                game().log(3, "En attente du joueur.");
                System.out.print(username+" > ");
                new PlayerInputProcess(new Scanner(System.in).nextLine(), this).run();
            }
        });
    }
}
