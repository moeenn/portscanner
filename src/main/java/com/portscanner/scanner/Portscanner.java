package com.portscanner.scanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Portscanner {

    private final static Logger logger = Logger.getLogger(Portscanner.class.getName());
    private final String host;
    private final int TIMEOUT_MS = 2_000;
    private final int MAX_PARALLEL = 4;

    public Portscanner(String host) {
        this.host = host;
    }

    Optional<PortWithName> checkPort(String name, int port) {
        logger.log(Level.INFO, "checking port {0}", Integer.toString(port));
        try (var socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), TIMEOUT_MS);
            return Optional.of(new PortWithName(name, port));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public List<PortWithName> scan(List<PortGroup> targetPorts) throws InterruptedException, ExecutionException {
        List<Future<Optional<PortWithName>>> futures = new ArrayList<>();
        var sem = new Semaphore(MAX_PARALLEL);

        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var group : targetPorts) {
                for (var port : group.ports) {
                    var future = exec.submit(() -> {
                        sem.acquire();
                        try {
                            return checkPort(group.name, port);
                        } finally {
                            sem.release();
                        }
                    });

                    futures.add(future);
                }
            }
        }

        var result = new ArrayList<PortWithName>();
        for (var f : futures) {
            var value = f.get();
            if (value.isPresent()) {
                result.add(value.get());
            }
        }

        return result;
    }

    public static List<PortGroup> commonPorts() {
        var result = new ArrayList<PortGroup>();
        result.add(new PortGroup("http/s", 80, 443));
        result.add(new PortGroup("ssh", 22, 2222, 22222));
        result.add(new PortGroup("postgresql", 5432));
        result.add(new PortGroup("mysql", 3306, 33060, 33062));
        result.add(new PortGroup("mongodb", 27017, 27018, 27019, 27020));
        result.add(new PortGroup("redis", 6379));
        result.add(new PortGroup("influx-db", 8086));
        result.add(new PortGroup("elastic-search", 9200));
        result.add(new PortGroup("rabbit-mq", 5672, 15672));
        result.add(new PortGroup("web-app", 3000, 5000, 8000, 8080, 9000));
        result.add(new PortGroup("prometheus", 9090));
        result.add(new PortGroup("loki", 3100, 9095));
        result.add(new PortGroup("tempo", 3200, 9096));
        result.add(new PortGroup("mimir", 9009));
        result.add(new PortGroup("grpc", 50051));
        result.add(new PortGroup("jupyter", 8888));
        return result;
    }
}
