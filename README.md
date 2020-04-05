# Projet Prog-Répartie Lefebvre Benoit

## Présentation fonctionnement

### Préparatifs

La partie client permet de crypter de deux manières différentes :
1. Questions/Réponses : le client pose les questions à l'utilisateur et stocke les messages cryptés au fur et à mesure.
2. Fichiers `config.txt` préparés et traités au lancement du client.
        
Schéma du fichier `config.txt` :

| ligne  | valeur                              |
| ------ | ----------------------------------- |
| 1      | protocole (TCP ou UDP)              |
| 2      | crypteur (0 César, 1 Vigénère, ...) | 
| 3      | clé ou pas de cryptage              |
| 4      | phrase à crypter (max 100c.)        |
| 5      | crypteur (0 César, 1 Vigénère, ...) | 
| 6      | clé ou pas de cryptage              |
| 7      | phrase à crypter (max 100c.)        |

Dans le cas de l'utilisation de fichiers, les résultats sont stockés dans le fichier `traite_yyyyMMDD-HHMMSS.txt`.

### Démarrage

Pour démarrer les serveurs, il faut éxécuter la méthode *main* de `/MainServer.java`.
Pour démarrer le client, il suffit d'éxécuter la méthode *main* de `/MainClient.java`.
Pour démarrer le client traîtant le fichier `config.txt`, il suffit d'éxécuter la méthode *main* de `/MainLectureFichier.java`.

### Déroulement Utilisateur

1. Cette partie ne concerne pas la gestion des fichiers (voir plus bas).
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
12. Le client envoie un message vide pour se déconnecter.
13. Le serveur supprime l'IDC du client et déconnecte le client.

### Gestion par fichier

1. Le client ouvre le fichier `config.txt` bien préparé (fichier exemple fourni).
2. Le client traîte l'ouverture du fichier, ainsi que l'extraction du protocole.
3. Le client crée une liste de tous les messages à traîter.
4. Le client se connecte au serveur et reçoit l'adresse et l'IDC pour se connecter à un serveur de cryptage adapté au protocole demandé.
5. Le client envoie les messages à crypter au serveur, qui va les crypter et les envoyer.
6. Le client complète à alors une liste de messages cryptés.
7. Pour finir, le client intègre le contenu de la liste au fichier `traite_yyyyMMDD-HHMMSS.txt`.

### Explication UML Client

Toute la partie client se trouve dans le package `client`.
*  `client.MainClient` : c'est la classe qui lance et traite correctement les demandes du serveur et de l'utilisateur en déléguant certaines tâches.
*  `client.util.UncryptedMessage` : classe qui demande au client le pas ou la clé, ainsi que le message non codé à l'utilisateur.
*  `client.util.ServerInit` : classe dont le constructeur reçoit le message brut initial du serveur et en découpe les différentes parties pour les mettre à disposition de MainClient.
*  `client.util.CrypteursAvailable` : classe dont le constructeur reçoit le message contenant les crypteurs de la part du serveur. Elle découpe se tableau et mets à disposition la liste des cryptages disponibles.
*  `client.sessions` : package recevant les résultats des sessions Questions/Réponses.
*  `client.file.config` : package contenant les fichiers à traiter et leur résultat après traitement.

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
