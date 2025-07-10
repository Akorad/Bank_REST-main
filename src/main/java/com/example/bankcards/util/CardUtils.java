package com.example.bankcards.util;

public class CardUtils {
    public static String maskCardNumber(String number){
        if (number.length() < 4 ) return "****";
        return "**** **** **** "+ number.substring(number.length()-4);
    }
}
