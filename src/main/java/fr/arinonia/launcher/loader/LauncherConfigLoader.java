package fr.arinonia.launcher.loader;

import fr.arinonia.launcher.api.LauncherAPI;
import fr.arinonia.launcher.api.models.LauncherConfig;

import java.util.concurrent.CompletableFuture;

public class LauncherConfigLoader extends LoadableComponent {
    private final LauncherAPI launcherAPI;
    private LauncherConfig launcherConfig;

    public LauncherConfigLoader(final LauncherAPI launcherAPI) {
        this.launcherAPI = launcherAPI;
    }

    @Override
    protected CompletableFuture<Void> initialize() {
        return this.launcherAPI.fetchLauncherConfig().thenAccept(launcherConfig -> this.launcherConfig = launcherConfig);
    }

    @Override
    protected String getComponentName() {
        return "Launcher Config Loader";
    }

    public LauncherConfig getLauncherConfig() {
        return this.launcherConfig;
    }
}
