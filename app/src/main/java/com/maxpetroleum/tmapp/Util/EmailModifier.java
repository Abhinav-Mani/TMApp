package com.maxpetroleum.tmapp.Util;

public class EmailModifier {
    String Email;

    public EmailModifier(String email) {
        Email = email;
    }

    public String getEmail() {
        Email=Email.replace('@','a').replace('.','d');
        return Email;
    }
}
