package oc.projet03.enginers;

import oc.projet03.game.GameStat;
import oc.projet03.game.entitys.HumanPlayer;
import oc.projet03.game.entitys.OrdiPlayer;
import oc.projet03.game.entitys.Player;
import sun.rmi.runtime.Log;

import java.lang.reflect.Array;
import java.util.Objects;

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
        if(p.game().getActualStat() == GameStat.INIT || p.game().getActualStat() == GameStat.SHUTDOWN || s.length()<1) return;
        if(p instanceof OrdiPlayer) System.out.println("Ordi > "+s);
        if(s.length()>1 && s.charAt(0)=='$') {
            //Split input string
            String[] args = s.split(" ");
            if(args.length<2)return;
            try{
                switch(args[0]) {
                    case "$devmode" :
                        if(args[1].equalsIgnoreCase("true")) p.game().setDevMode(true);
                        else if(args[1].equalsIgnoreCase("false")) p.game().setDevMode(false);
                        break;
                    case "$maxtry" :
                        p.game().setMaxTry(Integer.parseInt(args[1]));
                        break;
                    case "$keysize" :
                        p.game().setKeySize(Integer.parseInt(args[1]));
                        break;
                }
            } catch(NumberFormatException e){
                p.game().sendArgumentErrorMessage(args[1]);
            }
        }
        else if(s.equalsIgnoreCase("exit") || ((p.game().getActualStat() == GameStat.WAITING || p.game().getActualStat() == GameStat.END) && s.equalsIgnoreCase("0"))) p.game().stop();
        else if(p.game().getActualStat() == GameStat.WAITING) {
            try {
                p.game().initMode(Integer.parseInt(s));
            }  catch(NumberFormatException e) {
                p.game().sendArgumentErrorMessage(s);
            }
        } else if(p.game().getActualStat() == GameStat.PLAYING) {
            if(p.game().getActualPlayer()!=p) return;
            if(p.game().getMode()==1 || p.game().getMode() == 3) {
                String compared = p.game().key.compareTo(s.toCharArray());
                p.game().lastComparedTry=s;
                p.game().lastComparedTryResult=compared;
                p.decrementTry();
                if(Objects.equals(compared, p.game().key.perfectString())) {
                    if(p.game().getActualPlayer() instanceof HumanPlayer) p.game().log(0, "Tu à gagner ! Bravo !");
                    else p.game().log(0, "Tu à perdu l'ordi à trouver la clée avant toi !");
                    p.game().setGameStat(GameStat.END);
                } else if(p.remainTry()<1) {
                    p.game().log(0, "Tu as perdu ! il ne te reste plus d'essais !");
                    p.game().setGameStat(GameStat.END);
                }
            } else {
                if(p instanceof HumanPlayer){
                    for(char c : s.toCharArray()) {
                        if(!(c=='+' || c=='-' || c=='=') || s.length()<p.game().key.size()){
                            p.game().log(0, "Le format de la réponse donné n'est pas bonne !");
                            return;
                        }
                    }
                    p.game().lastComparedTryResult = s;
                    if(s.equalsIgnoreCase(p.game().key.perfectString())) {
                        p.game().log(0, "L'ordi à trouver la clée !");
                        p.game().setGameStat(GameStat.END);
                    }
                }
                else {
                    if(p.remainTry()<1) {
                        p.game().log(0, "Tu à gagner, l'ordi n'as pas trouver la clée");
                        p.game().setGameStat(GameStat.END);
                        return;
                    }
                    p.game().lastComparedTry = s;
                    p.decrementTry();
                }
            }
            if(p.game().players()>1) p.game().switchPlayer();
        } else if(p.game().getActualStat()==GameStat.END) {
            if(s.equals("1")) p.game().initMode(p.game().getMode());
            else if(s.equals("2")) p.game().setGameStat(GameStat.WAITING);
        }
    }
}
