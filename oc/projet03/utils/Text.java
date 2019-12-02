package oc.projet03.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public enum Text {
    INITIALISATION(0),
    WELCOM_MESSAGE(0),
    MAIN_MENU(0),
    END_MENU(0),
    ARGUMENT_ERROR(0),
    KEY_SIZE_INCORRECT(0),
    KEY_FORMAT_INVALID(0),
    STOP(0),
    DEFINE_KEY(0),
    KEY_RESULT_COMPARED(0),
    ORDI_WIN(0),
    PLAYER_WIN(0),
    WARN_DATA_ARG_INVALID(1),
    WARN_DATA_ARGS_ERROR(1),
    WARN_DATA_CONFIGFILE_ERROR(1),
    DEBUG_DATA_CONFIGFILE(3),
    DEBUG_DATA_CONFIGFILE_COMPLET(3),
    DEBUG_DATA_ARGS(3),
    DEBUG_DATA_ARGS_COMPLET(3),
    DEBUG_DEV_MODE_ACTIVATED(3),
    DEBUG_GAMESTAT_CHANGE(3),
    DEBUG_MAXTRY_CHANGE(3),
    DEBUG_KEYSIZE_CHANGE(3),
    DEBUG_STARTING_GAME(3),
    DEBUG_GAME_ALREADY_PLAYING(3),
    DEBUG_SWITCHING_PLAYER(3),
    DEBUG_PRINT_KEY(3),
    DEBUG_WAITING_FOR_PLAYER(3),
    ;
    private static Logger gamelogger = LogManager.getLogger("game_Logger");
    private static Logger deblogger = LogManager.getLogger("deb_Logger");
    private int l; // NIVEAU DE LOG DU MESSAGE
    private String text; // VALEUR DU MESSAGE
    private String final_text; // VALEUR FINAL DU MESSAGE A ENVOYER (au cas ou un replace est effectuer)
    private boolean dmode = false; // Dev mode
    Text(int level){
        l = level;
        try {
            TextFile cf = new TextFile();
            text = cf.getText(name());
            final_text=text;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Il semblerais qu'il y est un problème. Vérifier que le fichier text.xml soit bien présent dans le même fichier que l'app et qu'il soit bien configurer. ");
        }
    }

    /**
     * Utiliser pour log un message de debug et y remplacer des valeurs définie dans un tableau
     * La valeur [i] du tableau sera changer par la valeur [i+1] dans le String final_text
     * @param replace
     * @param devmode
     */
    public void log(String[] replace, boolean devmode) {
        dmode = devmode;
        log(replace);
    }
    /**
     * Utiliser pour log un message de debug
     * @param devmode
     */
    public void log(boolean devmode) {
        dmode = devmode;
        log();
    }
    /**
     * Utiliser pour log un message et y remplacer des valeurs définie dans un tableau
     * La valeur [i] du tableau sera changer par la valeur [i+1] dans le String final_text
     * @param replace
     */
    public void log(String[] replace) {
        final_text=text;
        for(int i = 0 ; i<replace.length;i+=2){
           final_text = final_text.replace(replace[i], replace[i+1]);
        }
        log();
    }

    /**
     * Utiliser pour log un message (final_text) avec log4j
     */
    public void log() {
        for (String s : final_text.split("\n")) {
            if (l == 0) gamelogger.info(s);
            else if (l == 1) gamelogger.warn(s);
            else if (l == 2) gamelogger.error(s);
            else if (l == 3 && dmode) deblogger.debug(s);
            else if (l > 3) System.out.println("une erreur est survenue, impossible de trouvée le niveau de log " + l);
        }
    }
}
