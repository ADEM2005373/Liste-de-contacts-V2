# Liste de contacts (Version 2) — TP Android

Application Android développée en **Kotlin** et interfaces **XML**, axée sur l'interaction multi-activités, les Intents explicites/implicites et la gestion multimédia.

## 📱 Fonctionnalités
1. **MainActivity** :
   - Présentation de la liste de contacts avec **ListView personnalisée** (`ContactAdapter`).
   - Affichage de l'avatar, du nom complet et du numéro de téléphone sur chaque ligne.
   - Ajout d'un nouveau contact via le bouton d'en-tête.
   - Ouverture immédiate de la fiche contact lors du clic sur un élément.
   - Mise à jour instantanée de la liste lors du retour de résultat.

2. **DetailActivity** :
   - Consultation et modification : Nom, Prénom, Téléphone, E-mail, Photo de profil.
   - **Bouton Appeler** : Déclenchement d'un Intent implicite `ACTION_DIAL` préremplissant le composeur téléphonique.
   - **Bouton Envoyer un e-mail** : Déclenchement d'un Intent implicite `ACTION_SENDTO` préremplissant l'application de messagerie.
   - **Bouton Choisir photo** : Sélection d'une image locale dans la galerie via `ActivityResultContracts.GetContent`.
   - **Bouton Prendre photo** : Capture de cliché via l'appareil photo avec `ActivityResultContracts.TakePicturePreview`.
   - **Enregistrement & Validation** : Contrôle des champs obligatoires et transmission du résultat `RESULT_OK` à `MainActivity`.
   - **Annulation** : Retour à la liste sans modifier les valeurs existantes via `RESULT_CANCELED`.

---

## 📂 Structure du Répertoire
```
Liste-de-contacts-V2/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/contactslistv2/
│   │   │   ├── Contact.kt
│   │   │   ├── ContactAdapter.kt
│   │   │   ├── MainActivity.kt
│   │   │   └── DetailActivity.kt
│   │   └── res/
│   │       ├── drawable/ (ic_call.xml, ic_email.xml, ic_camera.xml, ...)
│   │       ├── layout/ (activity_main.xml, activity_detail.xml, item_contact.xml)
│   │       └── values/ (strings.xml, colors.xml, themes.xml)
│   └── build.gradle.kts
├── captures/
│   ├── 1_main_activity.png
│   ├── 2_detail_activity.png
│   ├── 3_modification_contact.png
│   ├── 4_galerie_photo.png
│   ├── 5_camera_photo.png
│   ├── 6_action_appel.png
│   └── 7_action_email.png
├── compte-rendu/
│   ├── compte-rendu.md
│   └── compte-rendu.pdf
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🚀 Compilation et Exécution

1. Ouvrir le dossier `Liste-de-contacts-V2` dans **Android Studio**.
2. Synchroniser le projet avec les fichiers Gradle.
3. Lancer sur émulateur ou appareil physique (`Shift + F10`) ou en ligne de commande :
   ```bash
   ./gradlew assembleDebug
   ```

---

## 👨‍💻 Auteur
- **Adem Ouerhani**
- Dépôt : [https://github.com/ADEM2005373/Liste-de-contacts-V2](https://github.com/ADEM2005373/Liste-de-contacts-V2)
