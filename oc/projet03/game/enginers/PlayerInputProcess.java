package oc.projet03.game.enginers;

import oc.projet03.game.GameStat;
import oc.projet03.game.entitys.HumanPlayer;
import oc.projet03.game.entitys.OrdiPlayer;
import oc.projet03.game.entitys.Player;
import oc.projet03.utils.Text;

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
            if(p.game().inGameCommand) {
                try{
                    switch(args[0]) {
                        case "$devmode" :
                            if(args[1].equalsIgnoreCase("true")) p.game().setDevMode(true);
                            else if(args[1].equalsIgnoreCase("false")) p.game().setDevMode(false);
                            break;
                        case "$keysize" :
                            p.game().setKeySize(Integer.parseInt(args[1]));
                            break;
                    }
                } catch(NumberFormatException e){
                    Text.ARGUMENT_ERROR.log(new String[] {"'arg'", args[1]});
                }
            }
        }
        else if(s.equalsIgnoreCase("exit") || ((p.game().getActualStat() == GameStat.WAITING || p.game().getActualStat() == GameStat.END) && s.equalsIgnoreCase("0"))) p.game().stop();
        else if(p.game().getActualStat() == GameStat.WAITING) {
            try {
                p.game().initMode(Integer.parseInt(s));
            }  catch(NumberFormatException e) {
                Text.ARGUMENT_ERROR.log(new String[] {"'arg'", s});
            }
        } else if(p.game().getActualStat() == GameStat.PLAYING) {
            if(p.game().getActualPlayer()!=p) return;
            if(p.game().getMode()==1 || p.game().getMode() == 3) {
                String compared = p.game().key.compareTo(s.toCharArray());
                p.game().lastComparedTry=s;
                p.game().lastComparedTryResult=compared;
                p.decrementTry();
                if(Objects.equals(compared, p.game().key.perfectString())) {
                    if(p.game().getActualPlayer() instanceof HumanPlayer) Text.PLAYER_WIN.log();
                    else Text.ORDI_WIN.log();
                    p.game().setGameStat(GameStat.END);
                } else if(p.remainTry()<1) {
                    p.game().setGameStat(GameStat.END);
                }
            } else {
                if(p instanceof HumanPlayer){
                    for(char c : s.toCharArray()) {
                        if(!(c=='+' || c=='-' || c=='=') || s.length()<p.game().key.size()){
                           Text.KEY_FORMAT_INVALID.log();
                            return;
                        }
                    }
                    p.game().lastComparedTryResult = s;
                    if(s.equalsIgnoreCase(p.game().key.perfectString())) {
                       Text.ORDI_WIN.log();
                        p.game().setGameStat(GameStat.END);
                    }
                }
                else {
                    if(p.remainTry()<1) {
                        Text.PLAYER_WIN.log();
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
