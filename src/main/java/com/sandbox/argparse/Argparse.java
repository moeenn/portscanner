package com.sandbox.argparse;

import java.net.InetAddress;

public class Argparse {

    private final String host;

    public Argparse(String[] args)
            throws Exception {

        if (args.length != 1) {
            throw new RuntimeException("please provide a host address to continue");
        }

        var hostRaw = args[0];
        if (!isValidIP(hostRaw)) {
            throw new Exception(hostRaw + " is not a valid IP address");
        }

        this.host = hostRaw;
    }

    public String getHost() {
        return this.host;
    }

    private static boolean isValidIP(String s) {
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
        } catch (Exception e) {
            return false;
        }
    }
}
