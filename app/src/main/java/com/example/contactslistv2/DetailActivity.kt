package com.example.contactslistv2

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class DetailActivity : AppCompatActivity() {

    private lateinit var imgDetailPhoto: ImageView
    private lateinit var etNom: EditText
    private lateinit var etPrenom: EditText
    private lateinit var etTelephone: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnAppeler: ImageButton
    private lateinit var btnEmail: ImageButton
    private lateinit var btnChoisirPhoto: Button
    private lateinit var btnPrendrePhoto: Button
    private lateinit var btnEnregistrer: Button
    private lateinit var btnAnnuler: Button
    private lateinit var btnRetour: ImageButton

    private var currentPosition: Int = -1
    private var currentPhotoUri: String = ""

    // Launcher pour la sélection d'une photo depuis la galerie
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentPhotoUri = uri.toString()
            imgDetailPhoto.setImageURI(uri)
            Toast.makeText(this, getString(R.string.msg_photo_selectionnee), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Sélection de photo annulée", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher pour la capture d'une photo via l'appareil photo
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            imgDetailPhoto.setImageBitmap(bitmap)
            // Enregistrement du bitmap dans le cache interne pour générer un URI persistant
            try {
                val file = File(cacheDir, "contact_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                currentPhotoUri = Uri.fromFile(file).toString()
                Toast.makeText(this, "Photo prise avec succès", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                currentPhotoUri = ""
            }
        } else {
            Toast.makeText(this, "Capture photo annulée", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imgDetailPhoto = findViewById(R.id.imgDetailPhoto)
        etNom = findViewById(R.id.etNom)
        etPrenom = findViewById(R.id.etPrenom)
        etTelephone = findViewById(R.id.etTelephone)
        etEmail = findViewById(R.id.etEmail)
        btnAppeler = findViewById(R.id.btnAppeler)
        btnEmail = findViewById(R.id.btnEmail)
        btnChoisirPhoto = findViewById(R.id.btnChoisirPhoto)
        btnPrendrePhoto = findViewById(R.id.btnPrendrePhoto)
        btnEnregistrer = findViewById(R.id.btnEnregistrer)
        btnAnnuler = findViewById(R.id.btnAnnuler)
        btnRetour = findViewById(R.id.btnRetour)

        // Récupération des données passées par MainActivity
        val contact = intent.getSerializableExtra(MainActivity.EXTRA_CONTACT) as? Contact
        currentPosition = intent.getIntExtra(MainActivity.EXTRA_POSITION, -1)

        contact?.let {
            etNom.setText(it.nom)
            etPrenom.setText(it.prenom)
            etTelephone.setText(it.telephone)
            etEmail.setText(it.email)
            currentPhotoUri = it.photoUri

            if (it.photoUri.isNotEmpty()) {
                try {
                    imgDetailPhoto.setImageURI(Uri.parse(it.photoUri))
                } catch (e: Exception) {
                    imgDetailPhoto.setImageResource(R.drawable.ic_avatar_default)
                }
            }
        }

        // Action Appeler (Intent implicite vers le composeur téléphonique)
        btnAppeler.setOnClickListener {
            val tel = etTelephone.text.toString().trim()
            if (tel.isNotEmpty()) {
                val callIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$tel")
                }
                try {
                    startActivity(callIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, getString(R.string.erreur_application_introuvable), Toast.LENGTH_SHORT).show()
                }
            } else {
                etTelephone.error = getString(R.string.erreur_telephone_obligatoire)
            }
        }

        // Action Envoyer un e-mail (Intent implicite vers l'application de messagerie)
        btnEmail.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                }
                try {
                    startActivity(emailIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, getString(R.string.erreur_application_introuvable), Toast.LENGTH_SHORT).show()
                }
            } else {
                etEmail.error = "Veuillez renseigner un e-mail"
            }
        }

        // Action Choisir photo (Galerie)
        btnChoisirPhoto.setOnClickListener {
            try {
                galleryLauncher.launch("image/*")
            } catch (e: Exception) {
                Toast.makeText(this, "Impossible d'accéder à la galerie", Toast.LENGTH_SHORT).show()
            }
        }

        // Action Prendre photo (Appareil photo)
        btnPrendrePhoto.setOnClickListener {
            try {
                cameraLauncher.launch(null)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Aucun appareil photo disponible", Toast.LENGTH_SHORT).show()
            }
        }

        // Action Enregistrer
        btnEnregistrer.setOnClickListener {
            val nom = etNom.text.toString().trim()
            val prenom = etPrenom.text.toString().trim()
            val telephone = etTelephone.text.toString().trim()
            val email = etEmail.text.toString().trim()

            // Validation des champs obligatoires
            var valide = true
            if (nom.isEmpty()) {
                etNom.error = getString(R.string.erreur_nom_obligatoire)
                valide = false
            }
            if (prenom.isEmpty()) {
                etPrenom.error = getString(R.string.erreur_prenom_obligatoire)
                valide = false
            }
            if (telephone.isEmpty()) {
                etTelephone.error = getString(R.string.erreur_telephone_obligatoire)
                valide = false
            }

            if (!valide) return@setOnClickListener

            val contactModifie = Contact(
                nom = nom,
                prenom = prenom,
                telephone = telephone,
                email = email,
                photoUri = currentPhotoUri
            )

            val resultIntent = Intent().apply {
                putExtra(MainActivity.EXTRA_CONTACT, contactModifie)
                putExtra(MainActivity.EXTRA_POSITION, currentPosition)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // Action Annuler
        btnAnnuler.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        // Bouton de retour en haut
        btnRetour.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
