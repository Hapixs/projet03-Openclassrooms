package oc.projet03.main;

import org.apache.logging.log4j.Logger;

import java.util.logging.LogManager;

/**
 * @author Alexandre Sarouille
 * @version 1.0
 *
 * -----------------------------------------------------------------------------------------
 *
 * Ce programme à pour but de proposer trois mode de jeux
 * different à un utilisateur (Attaquant, Deffenseur et Duel).
 * Dans ces trois mode de jeux le joueur sera contre l'ordi.
 * En fonction du mode/respectivement les joueurs (joueur et ordi) devront
 * essayer de trouver une clée à x chiffres (x définis dans la config / dans les param)
 * Un nombre d'essais maximum est aussi configurer dans le fichier / les param de lancement
 * sinon cela serais trop facile ;)
 * Pour Chaque essais la console afficher pour chaque chiffre de la clée si la bonne valeur
 * est suppérieur (+), inférieur (-) ou juste (=).
 *
 * -----------------------------------------------------------------------------------------
 */

public class Main {
    private static Logger logger = org.apache.logging.log4j.LogManager.getLogger(Main.class);
    public static void main(String[] param) {
        System.out.println("Hello World !");
        /*
         TODO : Analyser les parametres donnés
                paramètres : devmode, maxtry:[value], keysize:[value]
                devmode : active le mode developpeur et les messages de debug
                maxtry:[value] : defini le nombre d'essais [value] pour les joueurs
                keysize:[value] : defini nombre de chiffre [value] que contient la clée
        */
        boolean devmode = false;
        int maxtry;
        int keysize;
        try{
            for(String p: param) {
                // PARAM devmode
                if(p.equalsIgnoreCase("devmode")) devmode=true;
                // PARAM maxtry
                else if(p.split(":").length>2&&p.split(":")[0].equalsIgnoreCase("maxtry")) maxtry = Integer.parseInt(p.split(":")[1]);
                // PARAM keysize
                else if(p.split(":").length>2&&p.split(":")[0].equalsIgnoreCase("keysize"))keysize = Integer.parseInt(p.split(":")[1]);
            }
        } catch(NumberFormatException e) {
            System.out.println("Un problème est survenu lors de l'analyse des paramètres de lancement, une valeur n'est pas bonne !");
        }
    }
}
