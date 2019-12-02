package oc.projet03.game.entitys;

import oc.projet03.game.enginers.PlayerInputProcess;
import oc.projet03.game.Game;
import oc.projet03.game.GameStat;
import oc.projet03.utils.Text;

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
                if(game().getActualPlayer()==this){
                    System.out.print(username+" > ");
                    Text.DEBUG_WAITING_FOR_PLAYER.log(game().devMode());
                    new PlayerInputProcess(new Scanner(System.in).nextLine(), this).run();
                }
            }
        });
    }
}
