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
