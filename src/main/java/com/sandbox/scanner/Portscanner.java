package com.sandbox.scanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Portscanner {

    private final static Logger logger = Logger.getLogger(Portscanner.class.getName());
    private final String host;
    private final int timeoutMs = 2_000;

    public Portscanner(String host) {
        this.host = host;
    }

    Optional<PortWithName> checkPort(String name, int port) {
        logger.log(Level.INFO, "checking port {0}", Integer.toString(port));
        try (var socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMs);
            return Optional.of(new PortWithName(name, port));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public List<PortWithName> scan(List<PortGroup> targetPorts) {
        var result = new ArrayList<PortWithName>();
        for (var group : targetPorts) {
            for (var port : group.ports()) {
                var portResult = checkPort(group.name(), port);
                if (portResult.isPresent()) {
                    result.add(portResult.get());
                }
            }
        }

        return result;
    }

    public static List<PortGroup> commonPorts() {
        var result = new ArrayList<PortGroup>();
        result.add(new PortGroup("http/s", new int[]{80, 443}));
        result.add(new PortGroup("ssh", new int[]{22, 2222, 22222}));
        result.add(new PortGroup("postgresql", new int[]{5432}));
        result.add(new PortGroup("mysql", new int[]{3306, 33060, 33062}));
        result.add(new PortGroup("mongodb", new int[]{27017, 27018, 27019, 27020}));
        // result.add(new PortGroup("redis", new int[]{6379}));
        // result.add(new PortGroup("influx-db", new int[]{8086}));
        // result.add(new PortGroup("elastic-search", new int[]{9200}));
        // result.add(new PortGroup("rabbit-mq", new int[]{5672, 15672}));
        // result.add(new PortGroup("web-app", new int[]{3000, 5000, 8000, 8080, 9000}));
        // result.add(new PortGroup("prometheus", new int[]{9090}));
        // result.add(new PortGroup("loki", new int[]{3100, 9095}));
        // result.add(new PortGroup("tempo", new int[]{3200, 9096}));
        // result.add(new PortGroup("mimir", new int[]{9009}));
        // result.add(new PortGroup("grpc", new int[]{50051}));
        // result.add(new PortGroup("jupyter", new int[]{8888}));
        return result;
    }
}
