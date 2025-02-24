package com.nu_pix.nu_pix.model;

import java.util.regex.Pattern;


public class Validator {

    public static boolean validarCpf(String cpf) {
        if (cpf == null) {
            return false;
        }

        String regex = "\\d{11}";
        return Pattern.matches(regex, cpf);
    }

    public static boolean validarCnpj(String cnpj) {
        if (cnpj == null) {
            return false;
        }

        String regex = "\\d{14}";
        return Pattern.matches(regex, cnpj);
    }

    public static boolean validarTelefone(String telefone) {
        if (telefone == null) {
            return false;
        }

        String regex = "\\+55\\d{11}";
        return Pattern.matches(regex, telefone);
    }

    public static boolean validarEmail(String email) {
        if (email == null) {
            return false;
        }

        String regex = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }


}

