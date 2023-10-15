package com.example.calculator;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ContactDatabase {
    private List<Contact> contacts = new ArrayList<>();

    public ContactDatabase(Context context) {
        // Initialize the database if needed
    }

    public void saveContact(Contact contact) {
        // Save the contact to the database
        contacts.add(contact);
    }

    public List<Contact> getAllContacts() {
        return contacts;
    }
}
