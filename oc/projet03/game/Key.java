package oc.projet03.game;

import java.util.ArrayList;
import java.util.Random;

public class Key {
    //Array de chaque chiffre de la clé
    private ArrayList<Integer> keyarray = new ArrayList<>();
    //taille de la clé
    private int size = 0;
    private Game game;
    public void generate(int s, Game g) {
        size = s;
        game =g;
        for(int i = 0; i < size; i++){
            int a = new Random().nextInt(10);
            keyarray.add(a);
        }
        g.log(3, "Clé : "+keyarray.toString());
    }
    public String perfectString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < size; i++) s.append("=");
        return s.toString();
    }
    public void defineValue(ArrayList<Integer> array){
        keyarray = array;
        size = array.size();
    }
    public int size () {
        return size;
    }
    public String compareTo(char[] s) {
        StringBuilder st = new StringBuilder();
        for(int i = 0; i < keyarray.size(); i++){
            try {
                int a = keyarray.get(i);
                int b = Integer.parseInt(s[i]+"");
                if(a == b) st.append("=");
                else if(a > b) st.append("+");
                else if(a < b) st.append("-");
            } catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
                st.append("#");
            }
        }
        game.log(0, "Clé > "+st);
        return st.toString();
    }
}
