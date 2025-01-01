package fr.arinonia.launcher.api.models;

public class Server {
    private final String name;
    private final String host;

    public Server(final String name, final String host) {
        this.name = name;
        this.host = host;
    }

    public String getName() {
        return this.name;
    }

    public String getHost() {
        return this.host;
    }
}
