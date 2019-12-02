# projet03-Openclassrooms
## Lancement du programme :
 Dans un fichier .bat, écrire la ligne: 'java -jar <chemin vers le programme>/projet03.jar [arguments]'.
 Le programme utilise deux fichier de configuration obligatoire en dehors de sa structure, config.xml et texts.xml
 Config.xml -> Permet de changer les paramètre tel que devmode, taille de la clé ou nombre d'essais.
 Texts.xml  -> Répertorie les messages que le programme envoie en debug / info / erreur.
 Ces deux fichier sont essentiels et doivent être obligatoirement dans le même dossier que le programme .jar
 A l'interieur du programme ce trouver le fichier log4j2.xml qui permet la configuration des differents loggers.

### Args de lancement:
  Pour activer le mode developpeur :
  ```
  devmode
  ```
  Pour changer le taille de la clée:
  ```
  keysize:[taille]
  ```
  Pour changer le nombre d'essais:
  ```
  maxtry:[essais]
  ```
  Pour prédéfinir un nom d'utilisateur:
  ```
  username:[nom]
  ```
  La plus part de ces paramètres sont aussi configurable dirrectement depuis le fichier config.xml
