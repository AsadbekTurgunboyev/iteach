package com.example.iteach;

import java.text.DecimalFormat;

public class Const {
    public static final String KEY = "key";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String ROLE = "role";
    public static final String PAYMENT_HISTORY = "PaymentHistory";
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_SUPERADMIN = "super_admin";


    public static String currencyFormatter(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
    }
}
