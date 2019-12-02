package oc.projet03.main;

import oc.projet03.game.Game;
/**
 * @author Alexandre Sarouille
 * @version 1.0
 *
 * -----------------------------------------------------------------------------------------
 *
 * Ce programme a pour but de proposer trois modes de jeux
 * à un utilisateur (Attaquant, Défenseur et Duel).
 * Dans ces trois modes de jeux le joueur sera contre l'ordi.
 * En fonction du mode/respectivement les joueurs (joueur et ordi) devront
 * essayer de trouver une clé à x chiffres (x définis dans la config / dans les param)
 * Un nombre d'essais maximum sont aussi configurés dans le fichier / les param de lancement
 * sinon cela serait trop facile ;)
 * Pour Chaque essai la console affichera pour chaque chiffre de la clé si la bonne valeur
 * est supérieure (+), inférieur (-) ou juste (=).
 *
 * -----------------------------------------------------------------------------------------
 */

public class Main {
    public static void main(String[] args) {
        //Initialisation d'une game avec les paramètres de lancement.
        Game g = new Game(args);
        //Lancement de la game
        g.start();
    }
}
