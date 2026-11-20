package com.portscanner.scanner;

public class PortGroup {

    public final String name;
    public final int[] ports;

    public PortGroup(String name, int... ports) {
        this.name = name;
        this.ports = ports;
    }
}
