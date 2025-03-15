# Documentation de l'application **LilleConnect**

## 📖 1. Description générale de l’application
LilleConnect est une application web de réseau social dédiée aux étudiants. Elle permet la création de fils de discussion, la gestion des utilisateurs, et l'interaction via des messages et des likes.

### Fonctionnalités principales :
- 🔹 Création et gestion de threads (fils de discussion)
- 🔹 Publication de messages
- 🔹 Système de likes sur les messages
- 🔹 Gestion des abonnements aux threads
- 🔹 Authentification et sécurisation des données

---

##  2. Conception
### 2.1 Modèle Conceptuel des Données (MCD)

<img src="res/MCD_LilleConnect.png" alt="Description of the image">

### 2.2 Modèle Logique des Données (MLD)

- **usr** (<u>id</u>, name, pwd)
- **thread** (<u>id</u>, titre, _#admin_id_)
- **post** (<u>id</u>, contenu, date, _#thread_id, #usr_id_)
- **like** (<u>_#post_id, #usr_id_</u>)
- **subscriber** (<u>_#thread_id, #usr_id_</u>)

## 3. Requêtes SQL pertinentes (Partie 1)

### Récupération des messages d'un threa**d****
```sql
SELECT post.id, post.contenu, usr.name 
FROM post 
JOIN usr ON post.usr_id = usr.id 
WHERE post.thread_id = ? 
ORDER BY post.id ASC;
```

### Récupération des threads auxquels un utilisateur est abonné
```sql
SELECT thread.id, thread.titre, thread.description
FROM thread
JOIN abonnement ON thread.id = abonnement.thread_id
WHERE abonnement.usr_id = ?;
```