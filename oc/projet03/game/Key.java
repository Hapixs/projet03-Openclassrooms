package oc.projet03.game;

import oc.projet03.utils.Text;

import java.util.ArrayList;
import java.util.Random;

public class Key {
    //Array de chaque chiffre de la clé
    private ArrayList<Integer> keyarray = new ArrayList<>();
    //taille de la clé
    private int size = 0;
    private Game game;
    public Key(int s, Game g) {
        size = s;
        game = g;
    }
    void generate() {
        for(int i = 0; i < size; i++){
            int a = new Random().nextInt(10);
            keyarray.add(a);
        }
        Text.DEBUG_PRINT_KEY.log(new String[] {"'key'", keyarray.toString()}, game.devMode());
    }

    /**
     * Return ce que devrait être le string de comparaison de la clé correct
     */
    public String perfectString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < size; i++) s.append("=");
        return s.toString();
    }
    void defineValue(ArrayList<Integer> array){
        for(int i =  0; i < size; i++) {
            keyarray.add(array.get(i));
        }
    }
    public int size () {
        return size;
    }
    public String compareTo(char[] s) {
        StringBuilder st = new StringBuilder(); // String de comparaison à renvoyer
        for(int i = 0; i < size(); i++){
            try {
                int a = keyarray.get(i); // Valeur de la clé à l'emplacement i
                int b = Integer.parseInt(s[i]+""); // Valeur de l'essai à l'emplacement i
                if(a == b) st.append("="); // Si les valeurs sont égales
                else if(a > b) st.append("+"); // Si la clé à une valeur supérieur
                else if(a < b) st.append("-"); // Si la clé à une valeur inférieur
            } catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
                /*
                    Si le joueur fait une proposition avec autre chose qu'un nombre
                    Ou si la taille de l'essai est plus petit que la taille de la clé
                 */
                st.append("#");
            }
        }
        Text.KEY_RESULT_COMPARED.log(new String[] {"'result'", st+""});
        return st.toString();
    }
}
