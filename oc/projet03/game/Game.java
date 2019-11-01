package oc.projet03.game;

import oc.projet03.game.entitys.HumanPlayer;
import oc.projet03.game.entitys.Player;
import oc.projet03.utils.ConfigFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Game {
    //Logger
    private static Logger gamelogger = LogManager.getLogger(Game.class);
    //Stat de la game, si le jeux est en cours ou si le joueurs doit choisir une option dans un menu
    private GameStat stat;
    //Liste des joueurs actifs (maximum deux joueurs)
    private ArrayList<Player> players = new ArrayList<>();

    //Définit l'activation du mode développeur
    boolean devmode = false;
    // Définit le nombre d'essais que le joueur possède
    int maxtry = 0;
    // Définit la taille de la clé à trouver
    int keysize = 0;
    public void setDevMode(boolean b) {
        devmode = b;
        if(b) log(3, "Mode développeur activé.");
        else log(3, "Mode développeur desactivé.");
    }
    public void setMaxTry(int mt){
        maxtry=mt;
    }
    public void setKeySize(int ks){
        keysize=ks;
    }
    public Game(String[] args) {
        log(0, "Initalisation des paramètres du jeux...");
        try {
            ConfigFile cf = new ConfigFile();
            devmode = cf.devMode();
            maxtry = cf.maxTry();
            keysize = cf.keySize();
        } catch (Exception e) {
            log(1,"Une erreur est survenue lors de la lecture du fichier config.xml.");
        }
        try{
            for(String p: args) {
                if(p.equalsIgnoreCase("devmode")) devmode=true;
                else if(p.split(":").length>=2&&p.split(":")[0].equalsIgnoreCase("maxtry")) maxtry = Integer.parseInt(p.split(":")[1]);
                else if(p.split(":").length>=2&&p.split(":")[0].equalsIgnoreCase("keysize"))keysize = Integer.parseInt(p.split(":")[1]);
                else System.out.println(p+" n'est pas reconnus en tant qu'argument.");
            }
        } catch(NumberFormatException e) {
            log(1,"Un problème est survenu lors de l'analyse des paramètres de lancement, une valeur n'est pas bonne !");
        }
        log(3, "les paramètres de l'application on été initialisés." +
                "\n|\tDevmode = "+devmode
            +   "\n|\tMaxtry = "+maxtry
            +   "\n|\tKeysize = "+keysize);
    }
    public GameStat getActualStat() {
        return stat;
    }
    public void setGameStat(GameStat st) {
        stat = st;
        log(3, "Changement du Gamestat en "+st.name()+".");
        if(stat==GameStat.WAITING) printMainMenu();
        else if(stat==GameStat.END) printEndMenu();
    }
    public void start() {
        if(!threadInstance.isAlive()){
            threadInstance.start();
            log(3, "Lancement de la game avec les paramètres définis.");
        } else log(3, "Il semblerait que la game soit déjà en cours.");

    }
    public void stop() {
        log(3, "Arrêt de la game ...");
        setGameStat(GameStat.SHUTDOWN);
        for(int i = 0; i < players(); i++) unregisterPlayer(players.get(i));
    }
    private Thread threadInstance = new Thread(() -> {
        setGameStat(GameStat.INIT);
        log(0, "Nom d'utilisateur: ");
        registerNewPlayer(new HumanPlayer(this, new Scanner(System.in).next()));
        log(0, "Bonjour "+((HumanPlayer) players.get(0)).username+" !");
        log(0, "Bienvenue dans ce jeux.");
        log(0, "---------------------------------------------------------");
        setGameStat(GameStat.WAITING);
    });
    public Player getActualPlayer() {
        return players.get(0);
    }
    public void registerNewPlayer(Player p) {
        if(!players.contains(p) && players()<2) players.add(p);
        if(!p.speakListener().isAlive()) p.speakListener().start();
        log(3, "Un joueur "+p.getClass().getSimpleName()+" vient d'être ajouter dans la game.");
    }
    public void unregisterPlayer(Player p) {
        if(players.contains(p))players.remove(p);
        if(p.speakListener().isAlive())  p.speakListener().stop();
        log(3, "Un joueur "+p.getClass().getSimpleName()+" vient d'être retirer de la game.");
    }
    public void switchPlayer() {
        if(players.size()>1){
            log(3, "Changement du joueurs actuelle");
            Collections.reverse(players);
        }
    }
    public void log(int level, String log) {
        if(level==0) gamelogger.info(log);
        else if(level==1) gamelogger.warn(log);
        else if(level==2) gamelogger.error(log);
        else if(level==3 && devmode) gamelogger.debug(log);
        else if(level>3) System.out.println("une erreur est survenue, impossible de trouvée le niveau de log "+level);
    }
    public void printMainMenu() {
        log(0, "Choisis parmis les 3 modes de jeux ci-dessous celui au quel tu veux jouer");
        log(0, "\t (0) Quitter le jeux.");
        log(0, "\t (1) Attaquant : Essais de trouver la clée génere par l'ordi en "+maxtry+" essais");
        log(0, "\t (2) Deffenseur : Choisis une clé et l'ordi essaiera de la trouvée");
        log(0, "\t (3) Duel : Soit plus rapide que l'ordi à trouver la clée");
    }
    public void printEndMenu(){
        log(0, "Choisis une option ci-dessous.");
        log(0, "\t(0) Quitter le jeux.");
        log(0, "\t(1) Rejouer.");
        log(0, "\t(2) Revenir au menu principale.");
    }
    public int players() {
        return players.size();
    }
}