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

### Récupération des données d'un utilisateur suivant son nom et mot de passe
```sql
SELECT * FROM usr WHERE name = ? AND pwd = ?
```

### Récupération des utilisateurs abonnés à un thread
```sql
SELECT u.id, u.name 
FROM usr u JOIN subscriber s ON u.id = s.usr_id AND s.thread_id = ?
```

### Récupération des utilisateurs non-abonnés à un thread
```sql
SELECT u.id, u.name 
FROM usr u LEFT JOIN subscriber s ON u.id = s.usr_id AND s.thread_id = ? 
WHERE s.usr_id IS NULL
```

### Récupération du nombre de likes par post
```sql
SELECT COUNT(*)
FROM likes WHERE post_id = ?
```

### Récupération des posts d'un thread
```sql
SELECT post.id, post.contenu, usr.name, post.usr_id 
FROM post JOIN usr ON post.usr_id = usr.id 
WHERE thread_id = ? 
ORDER BY post.id ASC
```

### Récupération des threads auxquels un utilisateur est abonné
```sql
SELECT t.id, t.title 
FROM thread t JOIN subscriber s ON t.id = s.thread_id
WHERE s.usr_id = ?
```

### Vérifie si un utilisateur est bien abonné à un thread
```sql
SELECT 1 
FROM subscriber 
WHERE usr_id = ? AND thread_id = ?
```

### Vérifie si un utilisateur a déjà liké un post
```sql
SELECT 1 
FROM likes 
WHERE usr_id = ? AND post_id = ?
```

### Verifie si un utilisateur est bien l'admin d'un thread
```sql
SELECT admin_id FROM thread WHERE id = ? AND admin_id = ?
```