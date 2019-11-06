package oc.projet03.game.entitys;

import oc.projet03.enginers.PlayerInputProcess;
import oc.projet03.game.Game;

import java.util.ArrayList;
import java.util.Objects;


public class OrdiPlayer extends CraftPlayer {
    public OrdiPlayer(Game g, int maxTry, int keysize) {
        super(g, maxTry);
        min = new ArrayList<>();
        max = new ArrayList<>();
        for(int i = 0; i < keysize; i++) {
            max.add(9);
            min.add(0);
        }
    }
    //Les valeurs minimal possible pour chaque chiffres de la clée
    private ArrayList<Integer> min;
    //Les valeurs maximal possible pour chaque chiffres de la clée
    private ArrayList<Integer> max;
    public void trySomthing() {
        game().log(3, "Try something");
        updateRange(game().lastComparedTry, game().lastComparedTryResult);
        String s = "";
        for(int i = 0; i < game().key.size(); i++){
            int c =(max.get(i)-min.get(i))/2;
            if(min.get(i)==max.get(i))s+=min.get(i);
            else if(max.get(i)==1)s+=0;
            else if(c<2) s+=min.get(i)+1;
            else s+=min.get(i)+c;
        }
        new PlayerInputProcess(s, this).run();
        updateRange(game().lastComparedTry, game().lastComparedTryResult);
    }
   private void updateRange(String Try, String result) {
        if(Try==null || result==null) return;
        ArrayList<Integer> actual = new ArrayList<Integer>();
        for(char c : Try.toCharArray()) actual.add(Integer.parseInt(c+""));
        for(int i = 0; i < game().key.size(); i++) {
            if(!Objects.equals(min.get(i), max.get(i))){
                if(result.toCharArray()[i]=='+' && min.get(i)<actual.get(i)) min.set(i, actual.get(i));
                else if(result.toCharArray()[i]=='-' && max.get(i)>actual.get(i)) max.set(i, actual.get(i));
                else if(result.toCharArray()[i]=='=') {
                    min.set(i, actual.get(i));
                    max.set(i, actual.get(i));
                }
            }
        }
    }
}
