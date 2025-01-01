package fr.arinonia.launcher.loader;

import fr.arinonia.launcher.api.LauncherAPI;
import fr.arinonia.launcher.api.models.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
//! maybe ping the servers to check if they are online and get the data from the ping here
public class ServerLoader extends LoadableComponent {
    private final LauncherAPI launcherAPI;
    private final List<Server> servers = new ArrayList<>();

    public ServerLoader(final LauncherAPI launcherAPI) {
        this.launcherAPI = launcherAPI;
    }

    @Override
    protected CompletableFuture<Void> initialize() {
        return this.launcherAPI.fetchServers().thenAccept(serverResponse -> {
            this.servers.clear();
            this.servers.addAll(serverResponse);
        });
    }

    @Override
    protected String getComponentName() {
        return "Server Loader";
    }

    public List<Server> getServers() {
        return this.servers;
    }
}
