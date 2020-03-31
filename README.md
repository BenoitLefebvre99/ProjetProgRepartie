# Projet Prog-Répartie Lefebvre Benoit

## Présentation fonctionnement (Projet Non Terminé)

### Préparatifs

La partie client permet de crypter de deux manières différentes :
1. Questions/Réponses : le client pose les questions à l'utilisateur et stocke les messages cryptés au fur et à mesure.
2. Fichiers `/config/non_traite_X.txt` préparés et traités au lancement du client.
        
Schéma du fichier `/config/non_traite_X.txt` :

| ligne  | valeur                              |
| ------ | ----------------------------------- |
| 1      | protocole (TCP ou UDP)              |
| 2      | crypteur (0 César, 1 Vigénère, ...) | 
| 3      | clé ou pas de cryptage              |
| 4      | phrase à crypter (max 100c.)        |

Dans le cas de l'utilisation de fichiers, les résultats sont stockés dans le fichier `/config/traite_datetime.txt`.

### Démarrage

Pour démarrer les serveurs, il faut éxécuter la méthode *main* de `/MainServer.java`.
Pour démarrer le client, il suffit d'éxécuter la méthode *main* de `/MainClient.java`.

### Déroulement Utilisateur

1. Le client commence par la gestion des fichiers (voir plus bas).
2. Ensuite vient le Questions / Réponses.
3. Le client demande à l'utilisateur quel protocole utiliser ? (UDP/TCP)
4. Le client envoie alors la réponse au serveur d'accueil 'localhost:12345'.
5. Si aucune erreur n'est trouvée dans le message du client, le serveur contacte le serveur désiré.
6. Le serveur génère un IDC spécifique au client et l'ajoute dans la liste d'autorisation du serveur désiré.
7. Ensuite le client reçoit du serveur la réponse suivante `IDC:PROTOCOL:PORTSERVEURCRYPTAGE`.
8. Le serveur envoie un second message contenant une liste des différents crypteurs à disposition.
9. Le client se déconnecte alors du serveur d'accueil pour se connecter à l'adresse reçue.
10. Le client questionne à nouveau l'utilisateur et envoie au serveur de cryptage un message de la forme `IDC:IDCRYPTEUR:SHIFT:MESSAGE`.
11. Le serveur traite la demande du client après avoir vérifié que ce dernier présente un IDC valable.
12. Le client affiche le retour et le stocke dans le fichier `/sessions/session_DATE.txt`.
13. Le client envoie un message vide pour se déconnecter.
14. Le serveur supprime l'IDC du client et déconnecte le client.

### Explication UML Client

Toute la partie client se trouve dans le package `client`.
*  `client.MainClient` : c'est la classe qui lance et traite correctement les demandes du serveur et de l'utilisateur en déléguant certaines tâches.
*  `client.util.UncryptedMessage` : classe qui demande au client le pas ou la clé, ainsi que le message non codé à l'utilisateur.
*  `client.util.ServerInit` : classe dont le constructeur reçoit le message brut initial du serveur et en découpe les différentes parties pour les mettre à disposition de MainClient.
*  `client.util.CrypteursAvailable` : classe dont le constructeur reçoit le message contenant les crypteurs de la part du serveur. Elle découpe se tableau et mets à disposition la liste des cryptages disponibles.
*  `client.sessions` : package recevant les résultats des sessions Questions/Réponses.
*  `client.config` : package contenant les fichiers à traiter et leur résultat après traitement.

### Explication UML Server

Toute la partie serveur se trouve dans le package `server`.
*  `server.MainServer` : c'est la classe qui lance les trois serveurs (Accueil, UDP, TCP) et gère le multithread grâce la java.nio.
*  `server.tcp.ServerTcp` : classe qui gère le serveur de cryptage par le serveur de protocole TCP (port par défaut : 12346).
*  `server.udp.ServerUdp` : classe qui gère le serveur de cryptage par le serveur de protocole UDP (port par défaut : 12347).
*  `server.crypter.ICrypter` : interface définissant un crypteur.
*  `server.crypter.Cesar` : classe permettant de définir un crypteur spéficique basé sur le cryptage de César.
*  `server.crypter.Vigenere` : classe permettant de définir un crypteur spéficique basé sur le cryptage de Vigénère.
*  `server.crypter.Crypteurs` : énumération des différents Crypteurs disponibles.
*  `server.util.AllowList` : classe une liste d'autorisation d'IDC. Utilisée 2fois (ServerTcp et ServerUdp).
*  `server.util.ClientReponse` : classe qui reçoit une demande brute de la part du client et la découpe pour utilisation. C'est cette classe qui crypte le message et mets à disposition la réponse au client.
*  `server.util.Idc` : classe qui crée un IDC et génère la toute première réponse à envoyer au client pour établir la redirection.

### Explication test

Différents tests sont disponibles dans le package `test`. Ceux-ci fonctionnent avec Junit4.13 dont le jar est fourni dans /lib.
