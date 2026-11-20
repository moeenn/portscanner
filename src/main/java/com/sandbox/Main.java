package com.sandbox;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sandbox.argparse.Argparse;
import com.sandbox.scanner.Portscanner;

public class Main {

    private final static Logger logger = Logger.getLogger(Main.class.getName());

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$-7s %2$s - %5$s%6$s%n");
    }

    private static void run(String[] args) throws Exception {
        var parsedArgs = new Argparse(args);
        var host = parsedArgs.getHost();

        var scanner = new Portscanner(host);
        var ports = Portscanner.commonPorts();

        logger.log(Level.INFO, "starting scan on host {0}", host);
        var results = scanner.scan(ports);
        logger.info("scan complete");

        System.out.println("");
        for (var entry : results) {
            System.out.printf("%-15s: %d\n", entry.name(), entry.port());
        }
    }

    public static void main(String[] args) {
        try {
            Main.run(args);
        } catch (Exception e) {
            System.err.printf("error: %s.\n", e.getMessage());
        }
    }
}
