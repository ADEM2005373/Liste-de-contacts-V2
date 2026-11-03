# Compte Rendu — TP Android : Liste de Contacts (Version 2)

**Étudiant :** Adem Ouerhani  
**Module :** Développement Mobile Android (Kotlin & XML)  
**Projet :** Gestionnaire de Contacts avec Deux Activités et Intents Implicites / Explicites  
**Dépôt GitHub :** https://github.com/ADEM2005373/Liste-de-contacts-V2  

---

## 1. Objectifs du Travail Pratique

Ce travail pratique a pour objectif de concevoir une application Android avancée en Kotlin pour la gestion complète d'une liste de contacts. L'application met en œuvre :
* L'utilisation de **deux activités interconnectées** : `MainActivity` (liste récapitulative) et `DetailActivity` (consultation et édition approfondie).
* Une **ListView personnalisée** s'appuyant sur un adaptateur dédié (`ContactAdapter`) et un layout de ligne (`item_contact.xml`).
* Le passage de données structurées entre activités via des **Intents explicites** et le contrat moderne `ActivityResultContracts.StartActivityForResult()`.
* L'intégration d'**Intents implicites** pour déclencher le composeur téléphonique (`ACTION_DIAL`) et l'application d'e-mail (`ACTION_SENDTO`).
* La prise en charge multimédia avec sélection d'image dans la **galerie** (`GetContent`) et prise de photo via la **caméra** (`TakePicturePreview`).
* La gestion des cas limites et la validation rigoureuse des formulaires.

---

## 2. Architecture et Structure de l'Application

### 2.1 Modèle de données `Contact`
Le modèle `Contact` encapsule les informations essentielles et implémente `Serializable` pour être véhiculé au sein des Intents :

```kotlin
data class Contact(
    var nom: String,
    var prenom: String,
    var telephone: String,
    var email: String,
    var photoUri: String = ""
) : Serializable
```

### 2.2 Composants et Activités

1. **`MainActivity`** :
   * Affiche la liste des contacts dans une `ListView`.
   * Chaque ligne présente l'avatar (photo ou icône par défaut), le nom complet et le numéro de téléphone.
   * Gère le clic sur un élément pour lancer `DetailActivity` avec l'index et l'objet `Contact`.
   * Intègre un bouton d'ajout de nouveau contact dans l'en-tête.
   * Réceptionne les données modifiées grâce au callback `detailLauncher` et actualise instantanément la liste via `notifyDataSetChanged()`.

2. **`DetailActivity`** :
   * Permet de modifier le nom, le prénom, le téléphone et l'e-mail.
   * Fournit deux boutons d'action rapide : **Appeler** (ouverture de l'application Téléphone pré-remplie) et **Envoyer un e-mail** (messagerie pré-remplie).
   * Propose deux mécanismes d'ajout d'image : sélection dans la galerie ou capture photo.
   * Valide les champs obligatoires (nom, prénom, téléphone) avant confirmation.
   * Propose les boutons **Enregistrer** (`RESULT_OK`) et **Annuler** (`RESULT_CANCELED`).

---

## 3. Gestion des Événements et des Intents

### 3.1 Intents Implicites
* **Appel téléphonique** :
  ```kotlin
  val callIntent = Intent(Intent.ACTION_DIAL).apply {
      data = Uri.parse("tel:$telephone")
  }
  startActivity(callIntent)
  ```
* **Envoi d'e-mail** :
  ```kotlin
  val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
      data = Uri.parse("mailto:$email")
  }
  startActivity(emailIntent)
  ```
Chaque appel est sécurisé par un bloc `try / catch (e: ActivityNotFoundException)` pour éviter tout crash si l'appareil ne dispose pas d'application dédiée.

### 3.2 Gestion multimédia (Galerie et Caméra)
* **Galerie** : enregistrement d'un launcher `ActivityResultContracts.GetContent()` ciblant `"image/*"`.
* **Caméra** : enregistrement d'un launcher `ActivityResultContracts.TakePicturePreview()`, avec enregistrement du bitmap dans le cache interne de l'application pour persistance.

---

## 4. Tests Réalisés et Résultats

| Scénario de test | Comportement attendu | Résultat obtenu | Statut |
| :--- | :--- | :--- | :---: |
| **Ouverture d'un contact** | Données complètes transmises à DetailActivity | Affichage instantané et conforme | Conforme |
| **Modification et sauvegarde** | Mise à jour immédiate dans MainActivity | Données et avatar actualisés | Conforme |
| **Bouton Appeler** | Ouverture du composeur avec numéro prérempli | Composeur ouvert avec le bon numéro | Conforme |
| **Bouton E-mail** | Client de messagerie prérempli | Client mail ouvert avec destinataire | Conforme |
| **Sélection galerie** | Choix d'une photo et affichage dans la vue | Image affichée et URI stocké | Conforme |
| **Capture caméra** | Prise de photo et rafraîchissement de l'avatar | Photo capturée et assignée | Conforme |
| **Bouton Annuler** | Retour à la liste sans modification | Données d'origine inchangées | Conforme |
| **Champs obligatoires vides** | Blocage avec message d'erreur sur le champ | Indicateur d'erreur actif | Conforme |

---

## 5. Difficultés Rencontrées et Solutions Apportées

1. **Compatibilité Android 13+ pour les Intents Implicites** :
   * *Difficulté* : Le filtrage de visibilité des packages impose de déclarer les applications cibles.
   * *Solution* : Ajout de la balise `<queries>` dans `AndroidManifest.xml` pour les schémas `tel:` et `mailto:`.

2. **Transmission et persistance des photos prises par la caméra** :
   * *Difficulté* : Le passage d'un Bitmap volumineux par Intent peut provoquer une exception `TransactionTooLargeException`.
   * *Solution* : Enregistrement temporaire du cliché dans le répertoire de cache (`cacheDir`) et passage de son `Uri`.

---

## 6. Captures d'écran

Toutes les captures d'écran requises sont documentées dans le dossier `captures/` :
* `1_main_activity.png` : Vue principale avec la ListView de contacts personnalisée.
* `2_detail_activity.png` : Fiche détaillée avec formulaire, boutons d'appels et photos.
* `3_modification_contact.png` : Édition des coordonnées du contact.
* `4_galerie_photo.png` : Sélection d'une image dans la galerie.
* `5_camera_photo.png` : Prise de vue par l'appareil photo.
* `6_action_appel.png` : Composeur téléphonique prérempli.
* `7_action_email.png` : Interface d'envoi d'e-mail préremplie.
