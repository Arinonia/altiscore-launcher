package fr.arinonia.launcher.api.models;

import com.google.gson.annotations.SerializedName;
import fr.flowarg.flowupdater.download.json.CurseFileInfo;
import fr.flowarg.flowupdater.download.json.Mod;

import java.util.List;

public class Server {
    private final String name;
    private final String host;
    private final String version;
    @SerializedName("modLoader")
    private final ModLoader modLoader;
    private List<Mod> mods;
    private final List<CurseFileInfo> curseFiles;
    private final String description;
    private final String icon;

    public Server(final String name, final String host, final String version,
                  final ModLoader modLoader, final List<Mod> mods,
                  final List<CurseFileInfo> curseFiles, final String description,
                  final String icon) {
        this.name = name;
        this.host = host;
        this.version = version;
        this.modLoader = modLoader;
        this.mods = mods;
        this.curseFiles = curseFiles;
        this.description = description;
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public String getHost() {
        return this.host;
    }

    public String getVersion() {
        return this.version;
    }

    public ModLoader getModLoader() {
        return this.modLoader;
    }

    public List<Mod> getMods() {
        return this.mods;
    }

    public List<CurseFileInfo> getCurseFiles() {
        return this.curseFiles;
    }

    public String getDescription() {
        return this.description;
    }

    public String getIcon() {
        return this.icon;
    }

    @Override
    public String toString() {
        return "Server{" +
                "name='" + this.name + '\'' +
                ", host='" + this.host + '\'' +
                ", version='" + this.version + '\'' +
                ", modLoader=" + this.modLoader +
                ", mods=" + this.mods +
                ", curseFiles=" + this.curseFiles +
                ", description='" + this.description + '\'' +
                ", icon='" + this.icon + '\'' +
                '}';
    }

    public enum ModLoader {
        @SerializedName("vanilla")
        VANILLA("vanilla"),
        @SerializedName("forge")
        FORGE("forge"),
        @SerializedName("fabric")
        FABRIC("fabric"),
        @SerializedName("neo_forge")
        NEO_FORGE("neo_forge");

        private final String value;

        ModLoader(final String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static ModLoader fromValue(final String value) {
            for (final ModLoader modLoader : values()) {
                if (modLoader.getValue().equalsIgnoreCase(value)) {
                    return modLoader;
                }
            }
            return null;
        }
    }
}
