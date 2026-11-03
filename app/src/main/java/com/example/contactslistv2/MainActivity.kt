package com.example.contactslistv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Liste des contacts affichés
    private val contacts = mutableListOf(
        Contact("Dupont", "Claire", "06 12 34 56 78", "claire.dupont@example.com"),
        Contact("Martin", "Thomas", "07 23 45 67 89", "thomas.martin@example.com"),
        Contact("Bernard", "Sophie", "06 98 76 54 32", "sophie.bernard@example.com"),
        Contact("Petit", "Lucas", "07 11 22 33 44", "lucas.petit@example.com"),
        Contact("Moreau", "Julien", "06 55 44 33 22", "julien.moreau@example.com")
    )

    private lateinit var lvContacts: ListView
    private lateinit var adapter: ContactAdapter
    private lateinit var btnAjouterContact: ImageButton

    // Launcher pour récupérer les modifications renvoyées par DetailActivity
    private val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val position = data?.getIntExtra(EXTRA_POSITION, -1) ?: -1
            val contactModifie = data?.getSerializableExtra(EXTRA_CONTACT) as? Contact

            if (contactModifie != null) {
                if (position >= 0 && position < contacts.size) {
                    // Mise à jour d'un contact existant
                    contacts[position] = contactModifie
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, getString(R.string.msg_contact_mis_a_jour), Toast.LENGTH_SHORT).show()
                } else if (position == -1) {
                    // Ajout d'un nouveau contact
                    contacts.add(contactModifie)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Contact ajouté avec succès", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, getString(R.string.msg_operation_annulee), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lvContacts = findViewById(R.id.lvContacts)
        btnAjouterContact = findViewById(R.id.btnAjouterContact)

        adapter = ContactAdapter(this, contacts)
        lvContacts.adapter = adapter

        // Clic sur un contact de la liste pour ouvrir le détail
        lvContacts.setOnItemClickListener { _, _, position, _ ->
            val contactSelectionne = contacts[position]
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(EXTRA_CONTACT, contactSelectionne)
                putExtra(EXTRA_POSITION, position)
            }
            detailLauncher.launch(intent)
        }

        // Clic sur le bouton d'ajout de contact
        btnAjouterContact.setOnClickListener {
            val nouveauContact = Contact("", "", "", "")
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(EXTRA_CONTACT, nouveauContact)
                putExtra(EXTRA_POSITION, -1)
            }
            detailLauncher.launch(intent)
        }
    }

    companion object {
        const val EXTRA_CONTACT = "extra_contact"
        const val EXTRA_POSITION = "extra_position"
    }
}
