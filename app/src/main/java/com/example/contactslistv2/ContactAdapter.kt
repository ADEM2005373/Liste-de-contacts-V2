package com.example.contactslistv2

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

/**
 * Adaptateur personnalisé pour afficher les contacts dans la ListView.
 */
class ContactAdapter(
    context: Context,
    private val contacts: List<Contact>
) : ArrayAdapter<Contact>(context, 0, contacts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_contact, parent, false
        )

        val contact = contacts[position]

        val imgContact = view.findViewById<ImageView>(R.id.imgContact)
        val txtNomPrenom = view.findViewById<TextView>(R.id.txtNomPrenom)
        val txtTelephone = view.findViewById<TextView>(R.id.txtTelephone)

        txtNomPrenom.text = "${contact.nom} ${contact.prenom}"
        txtTelephone.text = contact.telephone

        if (contact.photoUri.isNotEmpty()) {
            try {
                imgContact.setImageURI(Uri.parse(contact.photoUri))
            } catch (e: Exception) {
                imgContact.setImageResource(R.drawable.ic_avatar_default)
            }
        } else {
            imgContact.setImageResource(R.drawable.ic_avatar_default)
        }

        return view
    }
}
