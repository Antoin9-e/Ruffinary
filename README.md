# Ruffinary

![Logo](/logo.png)

Ruffinary is a media library for my father-in-law who collects Blu-ray laser discs etc.

## Installation


### 1. 📥 Download Install.exe 
Download this installer `Ruffinary_Install.exe` : https://github.com/Antoin9-e/Ruffinary/blob/main/Ruffinary_Install.exe

### 2. 🛠️ Prérequis
- **JDK 23 (OU PLUS) : [https://www.java.com/en/download/manual.jsp](https://www.oracle.com/java/technologies/downloads/)
  -> telecharger le jdk
  -> ajouter le au variables d'environnement ( barre de recherche (variable environnement) >  ajouter une variable syst ( appeler la JAVA_HOME) > ajouter le path du jdk > CLiquer sur la Var PATH (nouvelle) > taper %JAVA_HOME%\bin)
- **MySQLserver** installé et configuré : [https://dev.mysql.com/downloads/installer/](https://dev.mysql.com/downloads/mysql/8.0.html)
- Compte MySQL valide (ex : `root / motdepasse`)
- **MysqlWorkbench** ou autres : pour executer le script.sql

### 3. ⚙️ Importer la base de données
Utilise `script.sql` (fourni dans le dossier d'installation) pour créer les tables :

#### Manuellement

.ouvrir ton outil sql ex workbench
.connecte toi a ton serveur (configur'2 grace a mysql server) et copie colle le  script.sql
. execute le 

#### Avec une commande

```bash
mysql -u root -p < script.sql
```

### 4. Connexion de l'application au server SQL

- Execute en tant qu'administrateur le fichier config.properties fourni dans l'installer avec un editeur comme notepad ++
- remplace par les identifiants de ton compte SQL ( ex : root / mdp)

### 5. Lance l'application avec le .exe fourni






## Usage

### Main Features

- **Feature 1**: Add a movie.
  -  To add a new movie, click on "Ajouter".
  -  You can add a movie manually or by barcode

- **Feature 2**: Modify a Movie.
  -  To modify a movie, click on "Modifier".
  -  You can replace any informations about the choosen movie

- **Feature 3**: Delete a movie.
  - To delete a movie, click on "Supprimer".
  






## License

Ce projet est sous licence **Antoin9-e**.  
Toute utilisation non autorisée est interdite.


## ✅ Tâches accomplies

- [x] Initialiser le dépôt Git
- [x] Créer le fichier README
- [x] Ajouter une licence
- [X] creer le projet maven
- [X] creer la base de donnees
- [X] fct ajouter manuellement
- [X] fct ajouter avec barcode
- [X] Faire la liste des fonctionnalites
- [X] Compteur Ditems
- [X] Api Laser Disc
- [X] modifier
- [X] Tri Base
- [X] mode sombre

---

## 🔧 Tâches à faire



- [ ] Debuggage
- [ ] Api UMD
- [ ] Stylisation
- [ ] a voir ...


