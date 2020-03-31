# Projet Prog-Répartie Lefebvre Benoit

## Présentation fonctionnement

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