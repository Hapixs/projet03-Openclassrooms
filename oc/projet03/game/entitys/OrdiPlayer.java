package oc.projet03.game.entitys;

import oc.projet03.game.enginers.PlayerInputProcess;
import oc.projet03.game.Game;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public void trySomething() {
        updateRange(game().lastComparedTry, game().lastComparedTryResult);
        String s = ""; // String représentant l'essai du bot
        for(int i = 0; i < game().key.size(); i++){
            double c =(max.get(i)-min.get(i))/2; // Différence entre le min et le max div par 2
            if(min.get(i)==max.get(i))s+=min.get(i); // Si le min = max alors la valeur est bonne
            else if(max.get(i)==1)s+=0; // Si le max est de 1 alors la valeur est forcement 0
            //Sinon on calcul la valeur entre le min et le max
            else s+=min.get(i)+ new BigDecimal(c).setScale(0, RoundingMode.UP).intValue();
        }
        new PlayerInputProcess(s, this).run();
        updateRange(game().lastComparedTry, game().lastComparedTryResult);
    }
   private void updateRange(String Try, String result) {
       //Si une des deux valeur est null, impossible de mettre a jour
        if(Try==null || result==null) return;
        ArrayList<Integer> actual = new ArrayList<>(); // Tableau représentant une clé à x Integers
        for(char c : Try.toCharArray()) actual.add(Integer.parseInt(c+"")); // Ajout des valeur de l'essai 'try'
        for(int i = 0; i < game().key.size(); i++) {
            if(!Objects.equals(min.get(i), max.get(i))){ // Si une valeur à déjà été trouvée pas besoin de changer
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
