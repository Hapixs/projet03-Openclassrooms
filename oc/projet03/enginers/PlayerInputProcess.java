package oc.projet03.enginers;

import oc.projet03.game.GameStat;
import oc.projet03.game.entitys.HumanPlayer;
import oc.projet03.game.entitys.Player;

public class PlayerInputProcess {
    //Input String
    String s;
    //Current player
    Player p;
    public PlayerInputProcess(String inputString, Player player) {
        s = inputString;
        p = player;
    }
    public void run() {
        if(p.game().getActualStat() == GameStat.INIT
                || p.game().getActualStat() == GameStat.SHUTDOWN) return;
        if(p instanceof HumanPlayer) {
            if(s.charAt(0)=='$') {
                //Split input string
                String[] sts = s.split(" ");
                if(sts.length<2) return;
                if(sts[0].equalsIgnoreCase("$devmode")) {
                    if(sts[1].equalsIgnoreCase("true")) p.game().setDevMode(true);
                    else if(sts[1].equalsIgnoreCase("false")) p.game().setDevMode(false);
                    return;
                }
                try {
                    if(sts[0].equalsIgnoreCase("$maxtry")){
                        p.game().setMaxTry(Integer.parseInt(sts[1]));
                    } else if(sts[0].equalsIgnoreCase("$keysize")) {
                        p.game().setKeySize(Integer.parseInt(sts[1]));
                    }
                    return;
                } catch(NumberFormatException e){
                    p.game().log(1, "Erreur lors de la lecture de l'argument de la commande, "+sts[1]+" n'est pas un chiffre !");
                }
            }
            else if(s.equalsIgnoreCase("exit") || ((p.game().getActualStat() == GameStat.WAITING || p.game().getActualStat() == GameStat.END) && s.equalsIgnoreCase("0"))) p.game().stop();
        }
        if(p.game().getActualStat()!=GameStat.SHUTDOWN && p.game().players()>1)p.game().switchPlayer();
    }
}
