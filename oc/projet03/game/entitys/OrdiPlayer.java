package oc.projet03.game.entitys;

import oc.projet03.enginers.PlayerInputProcess;
import oc.projet03.game.Game;

import java.util.ArrayList;


public class OrdiPlayer extends CraftPlayer {
    private String next = "";
    public OrdiPlayer(Game g, int maxTry, int keysize) {
        super(g, maxTry);
        for(int i = 0; i < keysize; i++) {
            min.add(0);
            max.add(9);
        }
    }
    private ArrayList<Integer> min = new ArrayList<>();
    private ArrayList<Integer> max = new ArrayList<>();
    public void trySomthing() {
        game().log(3, "Try something");
        updateRange(game().lastComparedTry, game().lastComparedTryResult);
        String s = "";
        for(int i = 0; i < game().key.size(); i++){
            s+= min.get(i)+(max.get(i)-min.get(i))/2;
        }
        new PlayerInputProcess(s, this).run();
    }
   private void updateRange(String Try, String result) {
        if(Try==null || result==null)return;
        ArrayList<Integer> actual = new ArrayList<Integer>();
        for(char c : Try.toCharArray()) actual.add(Integer.parseInt(c+""));
        for(int i = 0; i < game().key.size(); i++) {
            if(result.toCharArray()[i]=='+' && min.get(i)<actual.get(i)) min.set(i, actual.get(i));
            else if(result.toCharArray()[i]=='-' && max.get(i)>actual.get(i)) max.set(i, actual.get(i));
            else if(result.toCharArray()[i]=='=') {
                min.set(i, actual.get(i));
                max.set(i, actual.get(i));
            }
        }
    }
}
