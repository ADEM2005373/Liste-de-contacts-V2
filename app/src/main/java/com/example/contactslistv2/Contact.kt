package com.example.contactslistv2

import java.io.Serializable

/**
 * Modèle de données représentant un contact.
 * Implémente Serializable pour permettre la transmission via les Intents.
 */
data class Contact(
    var nom: String,
    var prenom: String,
    var telephone: String,
    var email: String,
    var photoUri: String = ""
) : Serializable
