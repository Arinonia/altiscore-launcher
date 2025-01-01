package fr.arinonia.launcher.api.models;

public class LauncherConfig {
    private final String version;
    private final String discord;

    public LauncherConfig(final String version, final String discord) {
        this.version = version;
        this.discord = discord;
    }

    public String getVersion() {
        return this.version;
    }

    public String getDiscord() {
        return this.discord;
    }
}
