package br.com.ratacheski.bluefood.util;


import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

public class StringUtils {
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().length() == 0;
    }

    public static String encrypt(String rawString) {
        if (isEmpty(rawString)) {
            return null;
        }
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder.encode(rawString);
    }

    public static String concatenate(Collection<String> strings, String separator) {
        if (strings == null || strings.size() == 0) {
            return null;
        }
        separator += " ";
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String s : strings) {
            if (!first) {
                builder.append(separator);
            }
            builder.append(s);
            first = false;
        }
        return builder.toString();
    }
}
