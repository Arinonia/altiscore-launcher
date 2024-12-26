package fr.arinonia.launcher.utils;

import java.util.Locale;

public enum OsDetector {
    LINUX(new String[] { "linux", "unix" }),
    WINDOWS(new String[] { "win" }),
    MACOS(new String[] { "macos", "osx", "mac" }),
    UNKNOWN(new String[0]);

    private final String[] aliases;

    OsDetector(final String[] aliases) {
        this.aliases = (aliases == null) ? new String[0] : aliases;
    }

    public static OsDetector getCurrentPlatform() {
        final String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        for (final OsDetector os : values()) {
            for (final String alias : os.getAliases()) {
                if (osName.contains(alias)) {
                    return os;
                }
            }
        }
        return UNKNOWN;
    }

    public String[] getAliases() {
        return this.aliases;
    }
}
