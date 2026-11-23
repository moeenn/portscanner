package com.portscanner.validator;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP {

    public static boolean isValidIP(String s) {
        return isValidIPv4(s) || isValidIPv6(s);
    }

    private static boolean isValidIPv4(String s) {
        if (s == null) {
            return false;
        }
        String[] parts = s.split("\\.", -1);
        if (parts.length != 4) {
            return false;
        }
        for (String p : parts) {
            if (p.isEmpty() || p.length() > 3) {
                return false;
            }
            if (p.length() > 1 && p.charAt(0) == '0') {
                return false; // reject leading zeros

            }
            for (int i = 0; i < p.length(); i++) {
                if (!Character.isDigit(p.charAt(i))) {
                    return false;
                }
            }
            if (Integer.parseInt(p) > 255) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidIPv6(String s) {
        if (s == null || !s.contains(":")) {
            return false;
        }
        try {
            InetAddress.getByName(s);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
