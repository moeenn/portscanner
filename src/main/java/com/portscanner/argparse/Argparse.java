package com.portscanner.argparse;

import com.portscanner.validator.IP;

public class Argparse {

    private final String host;

    public Argparse(String[] args)
            throws Exception {

        if (args.length != 1) {
            throw new RuntimeException("please provide a host address to continue");
        }

        var hostRaw = args[0];
        if (!IP.isValidIP(hostRaw)) {
            throw new Exception(hostRaw + " is not a valid IP address");
        }

        this.host = hostRaw;
    }

    public String getHost() {
        return this.host;
    }
}
