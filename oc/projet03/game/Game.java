package oc.projet03.game;

import oc.projet03.game.entitys.HumanPlayer;
import oc.projet03.game.entitys.OrdiPlayer;
import oc.projet03.game.entitys.Player;
import oc.projet03.utils.ConfigFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class Game {
    //Logger
    private static Logger gamelogger = LogManager.getLogger(Game.class);
    //Stat de la game, si le jeux est en cours ou si le joueurs doit choisir une option dans un menu
    private GameStat stat;
    //Liste des joueurs actifs (maximum deux joueurs)
    private ArrayList<Player> players = new ArrayList<>();
    private boolean devmode = false; //Définit l'activation du mode développeur
    private int maxtry = 0;  // Définit le nombre d'essais que le joueur possède
    private int keysize = 0; // Définit la taille de la clé à trouver
    public Key key = new Key(); // Objet représentant la clé actuelle.
    public String lastComparedTryResult; // Dérnier resultat de la comparaison
    public String lastComparedTry;  // Dérnier essais d'un joueur
    public String cfg_username; // nom d'utilisateur (uniquement si définit dans les paramètres de lancement)
    private HashMap<String, String> appText = new HashMap<>();

    public Game(String[] args) {
        log(0, "Initalisation des paramètres du jeux...");
        setGameStat(GameStat.INIT);
        // Récuperation des donnée dans config.xml
        try {
            log(3, "Récupération des donnée du fichier config.xml");
            ConfigFile cf = new ConfigFile(); // Config du programme
            setDevMode(cf.devMode());
            maxtry = cf.maxTry();
            keysize = cf.keySize();
            appText.put("WELCOM_MESSAGE", cf.getText("WELCOM_MESSAGE"));
            appText.put("MAIN_MENU", cf.getText("MAIN_MENU"));
            appText.put("END_MENU", cf.getText("END_MENU"));
            appText.put("ARGUMENT_ERROR", cf.getText("ARGUMENT_ERROR"));


            log(3, "Donnée du fichier config.xml chargées !");
        } catch (Exception e) {
            log(1,"Une erreur est survenue lors de la lecture du fichier config.xml.");
        }
        // Analyse des paramètres de lancement
        try{
            log(3, "Analyse des paramètres de lancement");
            for(String p: args) {
                if(p.equalsIgnoreCase("devmode")) devmode=true;
                else if(p.split(":").length>=2&&p.split(":")[0].equalsIgnoreCase("maxtry")) maxtry = Integer.parseInt(p.split(":")[1]);
                else if(p.split(":").length>=2&&p.split(":")[0].equalsIgnoreCase("keysize"))keysize = Integer.parseInt(p.split(":")[1]);
                else if(p.split(":").length>=2&&p.split(":")[0].equalsIgnoreCase("username")) cfg_username=  p.split(":")[1];
                else System.out.println(p+" n'est pas reconnus en tant qu'argument.");
            }
            log(3, "Paramètre instancié !");
        } catch(NumberFormatException e) {
            log(1,"Un problème est survenu lors de l'analyse des paramètres de lancement, une valeur n'est pas bonne !");
        }
        log(3, "les paramètres de l'application on été initialisés : " +
                "\n|\tDevmode = "+devmode
                +   "\n|\tMaxtry = "+maxtry
                +   "\n|\tKeysize = "+keysize);
    }
    // Activation du mode developpeur
    public void setDevMode(boolean b) {
        devmode = b;
        if(b) log(3, "Mode développeur activé.");
        else log(3, "Mode développeur desactivé.");
    }
    // Change le nombres d'essais maximum
    public void setMaxTry(int mt){
        maxtry=mt;
    }
    // Change la taille de la clée à trouver
    public void setKeySize(int ks){
        keysize=ks;
    }
    // Return la GameStat actuelle de la game
    public GameStat getActualStat() {
        return stat;
    }
    // Change le gameStat actuelle de la game
    public void setGameStat(GameStat st) {
        stat = st;
        log(3, "Changement du Gamestat en "+st.name()+".");
        if(stat==GameStat.WAITING) printMainMenu();
        else if(stat==GameStat.END){
            printEndMenu();
            for(Player p : (ArrayList<Player>) players.clone()) if(p instanceof OrdiPlayer) unregisterPlayer(p);
        }
    }
    public void start() {
        if(!threadInstance.isAlive()){
            threadInstance.start();
            log(3, "Lancement de la game avec les paramètres définis.");
        } else log(3, "Il semblerait que la game soit déjà en cours.");

    }
    private Thread threadInstance = new Thread(() -> {
        if(cfg_username == null) {
            System.out.print("Nom d'utilisateur: ");
            registerNewPlayer(new HumanPlayer(this, new Scanner(System.in).next(), maxtry));
        } else registerNewPlayer(new HumanPlayer(this, cfg_username, maxtry));
        for(String s : appText.get("WELCOM_MESSAGE").split("\n")) log(0, s.replace("'player'", ((HumanPlayer) players.get(0)).username));
        setGameStat(GameStat.WAITING);
    });
    public void stop() {
        log(3, "Arrêt de la game ...");
        setGameStat(GameStat.SHUTDOWN);
    }
    public Player getActualPlayer() {
        return players.get(0);
    }
    private void registerNewPlayer(Player p) {
        if(players()>2) for(Player pl : players) if(pl instanceof OrdiPlayer) unregisterPlayer(pl);
        if(!players.contains(p))players.add(p);
        log(3, "Un joueur "+p.getClass().getSimpleName()+" vient d'être ajouter dans la game.");
    }
    private void unregisterPlayer(Player p) {
        if(players.contains(p))players.remove(p);
        if(p instanceof HumanPlayer && ((HumanPlayer) p).speakListener().isAlive())  ((HumanPlayer) p).speakListener().stop();
        log(3, "Un joueur "+p.getClass().getSimpleName()+" vient d'être retirer de la game.");
    }
    public void switchPlayer() {
        if(players()>1){
            log(3, "Changement du joueurs actuelle");
            Collections.reverse(players);
            log(3, "Joueur actuelle: "+getActualPlayer().getClass().getSimpleName());
            if(getActualPlayer()instanceof OrdiPlayer) ((OrdiPlayer) getActualPlayer()).trySomthing();
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
        for(String s : appText.get("MAIN_MENU").split("\n")) log(0, s.replace("'maxtry'", maxtry+"")
        .replace("'keysize'", keysize+""));
    }
    public void sendArgumentErrorMessage(Object arg) {
        log(0, appText.get("ARGUMENT_ERROR").replace("'arg'", arg+""));
    }
    public void printEndMenu(){
        for(String s : appText.get("END_MENU").split("\n")) log(0, s);
    }
    public int players() {
        return players.size();
    }
    int mode = 0;
    public int getMode() {
        return mode;
    }
    public void initMode(int mode) {
        if(mode > 3) return;
        log(3, "Initialisation du mode "+mode+" ...");
        for(Player p : players) p.resetData();
        lastComparedTryResult = null;
        lastComparedTry = null;
        key = new Key();
        this.mode = mode;
        if(mode!=2){
            key.generate(keysize, this);
            setGameStat(GameStat.PLAYING);
        }
        else {
            setGameStat(GameStat.INIT);
            while(getActualStat()==GameStat.INIT){
                log(0, "Indiquer une clé à trouver de "+keysize+" chiffres");
                String s = new Scanner(System.in).next();
                try {
                    if(s.toCharArray().length==keysize){
                        ArrayList<Integer> array = new ArrayList<>();
                        for(int i = 0; i < keysize; i++) {
                            array.add(Integer.parseInt(s.toCharArray()[i]+""));
                        }
                        key.defineValue(array);
                        setGameStat(GameStat.PLAYING);
                    } else sendArgumentErrorMessage(s);
                } catch(NumberFormatException e) {
                    sendArgumentErrorMessage(s);
                }
            }
        }
        if(mode!=1) {
            registerNewPlayer(new OrdiPlayer(this, maxtry, keysize));
            if(mode==3 && new Random().nextInt(3) > 1) switchPlayer();
        }
    }
}