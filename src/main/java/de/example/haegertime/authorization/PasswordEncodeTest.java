package de.example.haegertime.authorization;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncodeTest implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
    }
}
