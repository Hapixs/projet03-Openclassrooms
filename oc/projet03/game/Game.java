package oc.projet03.game;

import oc.projet03.game.entitys.HumanPlayer;
import oc.projet03.game.entitys.OrdiPlayer;
import oc.projet03.game.entitys.Player;
import oc.projet03.utils.ConfigFile;
import oc.projet03.utils.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * Class Game:
 * Initialise une nouvelle partie avec les paramètre de la config par défaut puis (si il y en à) les paramètres de lancement définis dans le constructeur (args)
 * Cette class sert à gérer l'avencement/état de la partie actuelle et les joueurs qui y sont ajouter
 */
public class Game {

    private GameStat stat; // Etat actuelle de la partie
    private ArrayList<Player> players = new ArrayList<>();  // Liste des joueurs actifs (maximum deux joueurs (Un Human + un Bot))
    private boolean devmode = false; //Définit l'activation du mode développeur
    private int maxtry = 0;  // Définit le nombre d'essais que le joueur possède
    private int keysize = 0; // Définit la taille de la clé à trouver
    public Key key = null; // Objet représentant la clé actuelle.
    public String lastComparedTryResult; // Dernier resultat de la comparaison obtunu par un joueur
    public String lastComparedTry;  // Dernier essais effectuer d'un joueur.
    public String cfg_username; // nom d'utilisateur (uniquement si définit dans les paramètres de lancement)

    /**
     * Constructeur de la class Game
     * Il permet d'initialiser les variables ci-dessus avec les diférentes configurations possible.
     * @param args
     */
    public Game(String[] args) {
        // INITIALISATION
        Text.INITIALISATION.log();                  // SOME TEXT
        setGameStat(GameStat.INIT);                 // Definir l'état actuelle sur INIT

        // Récuperation des donnée dans config.xml
        try {
           Text.DEBUG_DATA_CONFIGFILE.log(devmode); // SOME DEBUG TEXT
           ConfigFile cf = new ConfigFile(); // Fichier de configuration de l'app.
            // INIT DES VARIABLE EN FONC DU FICHIER config.xml
           setDevMode(cf.devMode()); // DEV MODE
           setMaxTry(cf.maxTry()); // ESSAIS MAX
           setKeySize(cf.keySize()); // TAILLE CLE

           Text.DEBUG_DATA_CONFIGFILE_COMPLET.log(devmode); // SOME DEBUG TEXT
        } catch (ParserConfigurationException | IOException | SAXException e) { // En cas d'erreur de lecture du fichier config.xml.
           Text.WARN_DATA_CONFIGFILE_ERROR.log(); // WARN
        } catch (NumberFormatException e) { // Si un arguement n'est
            Text.WARN_DATA_ARG_INVALID.log(); // WARN
        }
        // Analyse des paramètres donner dans le tableau args
        try{
            Text.DEBUG_DATA_ARGS.log(devmode); // SOME DEBUG TEXT

            // Parcours chaque string dans le tableau
            for(String p: args) {
                if(p.equalsIgnoreCase("devmode")) devmode=true; // DEV MODE
                else if(p.split(":").length>=2&&p.split(":")[0].equalsIgnoreCase("maxtry")) setMaxTry(Integer.parseInt(p.split(":")[1])); // ESSAIS MAX
                else if(p.split(":").length>=2&&p.split(":")[0].equalsIgnoreCase("keysize"))setKeySize(Integer.parseInt(p.split(":")[1])); // TAILLE CLE
                else if(p.split(":").length>=2&&p.split(":")[0].equalsIgnoreCase("username")) cfg_username =  p.split(":")[1]; // USER NAME
                else Text.WARN_DATA_ARG_INVALID.log(new String[] {"'arg'", p}); // WARN
            }
            Text.DEBUG_DATA_ARGS_COMPLET.log(devmode); // SOME DEBUG TEXT
        } catch(NumberFormatException e) { // Si une des valeur d'un paramètre n'est pas correcte
            Text.WARN_DATA_ARGS_ERROR.log(); // WARN
        }
    }
    /**
      Activation du mode developpeur
     */
    public void setDevMode(boolean b) {
        devmode = b; // CHANGE DEV MODE VALUE
        if(b) Text.DEBUG_DEV_MODE_ACTIVATED.log(true); // SOME DEBUG TEXT
    }
    public boolean devMode() {
        return devmode;
    }
    /**
       Change le nombres d'essais maximum
     */
    public void setMaxTry(int mt){
        maxtry=mt; // CHANGE MAXTRY VALUE
        Text.DEBUG_MAXTRY_CHANGE.log(devmode); // SOME DEBUG TEXT
    }
    /**
       Change la taille de la clée à trouver
     */
    public void setKeySize(int ks){
        keysize=ks; // CHANGE KEYSIZE VALUE
        Text.DEBUG_KEYSIZE_CHANGE.log(devmode); // SOME DEBUG TEXT
    }
    /**
       Return la GameStat actuelle de la game
     */
    public GameStat getActualStat() {
        return stat;
    }
    /**
       Change le gameStat actuelle de la game
     */
    public void setGameStat(GameStat st) {
        stat = st; // CHANGE STAT VALUE
        String[] s = new String[] {"'gamestat'", st.name()};
        Text.DEBUG_GAMESTAT_CHANGE.log(s, devmode);
        if(stat==GameStat.WAITING) printMainMenu(); // Afficher le menu princiaple
        else if(stat==GameStat.END){
            printEndMenu(); // Affichier le menu de fin de partie
            ((ArrayList<Player>) players.clone()).stream().filter(p -> p instanceof OrdiPlayer).forEach(this::unregisterPlayer); // Suppression des joueur de la partie
        }
    }
    /**
     * Démarre la partie instancié
     */
    public void start() {
        Text.DEBUG_STARTING_GAME.log(devmode); // SOME DEBUG TEXT
        if(stat == GameStat.INIT) new Thread(() -> { // Initialisation finale de la partie (joueur) dans un autres Thread.
            if(cfg_username == null) {
                System.out.print("Nom d'utilisateur: ");
                registerNewPlayer(new HumanPlayer(this, new Scanner(System.in).next(), maxtry)); // Enregistrement d'un nouveau joueur human avec sont username.
            } else registerNewPlayer(new HumanPlayer(this, cfg_username, maxtry)); // Enregistrement d'un nouveau joueur human avec l'username des paramètres données.
            Text.WELCOM_MESSAGE.log(new String[] {"'player'", ((HumanPlayer) getActualPlayer()).username}); // SOME TEXT
            // La partie est maintenant totalement configurer il manque à ajouter le bot en fonction du mode choisis.
            setGameStat(GameStat.WAITING); // Changement de l'état en WAINTING, pour afficher le menu princiaple
        }).start();
        else Text.DEBUG_GAME_ALREADY_PLAYING.log(devmode); // SOME DEBUG TEXT
    }
    /**
     * Stop la partie en cours.
     */
    public void stop() {
        Text.STOP.log(); // SOME TEXT
        setGameStat(GameStat.SHUTDOWN); // Changement de l'état en SHUTDOWN pour arreter la partie et les Thread qui y sont accrocher
    }
    /**
     * Return le joueur qui doit jouer (si les joueur sont plusieur)
     * Le joueur qui doit joueur sera le premier de la liste puis passera dernier une foie jouer
     * @return
     */
    public Player getActualPlayer() {
        return players.get(0);
    }
    /**
     * Ajouter à la liste des joueurs un joueur définit
     * @param p
     */
    private void registerNewPlayer(Player p) {
        if(players()>2) players.stream().filter(pl -> pl instanceof OrdiPlayer).forEach(this::unregisterPlayer); // Si il y déja plus de deux joueur alors on supprime les bots.
        if(!players.contains(p)) players.add(p); // Ajout du joueur dans la liste
    }
    /**
     * Retire à la liste des joueurs un joueur définit
     * @param p
     */
    private void unregisterPlayer(Player p) {
        if(players.contains(p)) players.remove(p); // suppression du joueur dans la liste
        if(p instanceof HumanPlayer && ((HumanPlayer) p).speakListener().isAlive())  ((HumanPlayer) p).speakListener().stop(); // Si le joueur est un human alors on stop sont listener
    }

    /**
     * Change le joueur actuelle
     */
    public void switchPlayer() {
        if(players()>1){
            Text.DEBUG_SWITCHING_PLAYER.log(devmode); // SOME DEBUG TEXT
            Collections.reverse(players); // Retourner la lister le joueur 0 devient le joueur 1 et vis vers sa
            if(getActualPlayer()instanceof OrdiPlayer) ((OrdiPlayer) getActualPlayer()).trySomething(); // Si le joueur actuelle est maintenant un bot alors on lui demande de faire un essais
        }
    }
    /**
     * Affiche le menu principale et ces option
     */
    private void printMainMenu() {
        Text.MAIN_MENU.log(new String[] {"'maxtry'", maxtry+"", "'keysize'", keysize+""});
    }
    /**
     * Affiche le menu de fin de partie et ces option
     */
    private void printEndMenu(){
       Text.END_MENU.log();
    }
    /**
     * Return le nombre de joueur actif dans la partie
     * @return
     */
    public int players() {
        return players.size();
    }
    int mode = 0; // Definit le mode de jeux actuelle
    /**
     * Renvoie la valeur du mode de jeux actuelle
     * @return
     */
    public int getMode() {
        return mode;
    }
    /**
     * Initialise un mode de jeux définit
     * @param mode
     */
    public void initMode(int mode) {
        if(mode > 3) return; // Si le mode est incorrect
        for(Player p : players) p.resetData();
        lastComparedTryResult = null;
        lastComparedTry = null;
        key = new Key(keysize, this);
        this.mode = mode;
        if(mode!=1) {
            registerNewPlayer(new OrdiPlayer(this, maxtry, keysize));
            if(mode==3){
                key.generate();
                setGameStat(GameStat.PLAYING);
                if(new Random().nextInt(3) > 1)switchPlayer();
                return;
            }
        } else {
            key.generate();
            setGameStat(GameStat.PLAYING);
            return;
        }
        if(mode==2){
            setGameStat(GameStat.INIT);
            while(getActualStat()==GameStat.INIT){
                Text.DEFINE_KEY.log(new String[] {"'keysize'", keysize+""});
                String s = new Scanner(System.in).next();
                try {
                    if(s.toCharArray().length==keysize){
                        ArrayList<Integer> array = new ArrayList<>();
                        for(int i = 0; i < keysize; i++) {
                            array.add(Integer.parseInt(s.toCharArray()[i]+""));
                        }
                        key.defineValue(array);
                        setGameStat(GameStat.PLAYING);
                        switchPlayer();
                    } else Text.KEY_SIZE_INCORRECT.log();
                } catch(NumberFormatException e) {
                    Text.KEY_FORMAT_INVALID.log();
                }
            }
        }
    }
}