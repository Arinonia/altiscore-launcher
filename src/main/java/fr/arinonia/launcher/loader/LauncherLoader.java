package fr.arinonia.launcher.loader;

import fr.arinonia.launcher.Launcher;
import fr.arinonia.launcher.api.LauncherAPI;
import fr.arinonia.launcher.utils.CallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LauncherLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(LauncherLoader.class);
    private final Map<Class<? extends LoadableComponent>, LoadableComponent> components;
    private final LauncherAPI launcherAPI; //! maybe gonna move this to Launcher class
    private final Launcher launcher;

    public LauncherLoader(final Launcher launcher) {
        this.launcher = launcher;
        this.components = new LinkedHashMap<>();
        this.launcherAPI = new LauncherAPI();

        this.components.put(AuthenticationLoader.class, new AuthenticationLoader(this.launcher));
        this.components.put(LauncherConfigLoader.class, new LauncherConfigLoader(this.launcherAPI));
        this.components.put(NewsLoader.class, new NewsLoader(this.launcherAPI));
        this.components.put(ServerLoader.class, new ServerLoader(this.launcherAPI));
    }

    public CompletableFuture<Void> loadAll(CallBack progressCallback) {
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        final double progressStep = 1.0 / this.components.size();
        double currentProgress = 0.0;

        for (Map.Entry<Class<? extends LoadableComponent>, LoadableComponent> entry : components.entrySet()) {
            double finalProgress = currentProgress;
            future = future.thenCompose(v -> {
                if (progressCallback != null) {
                    progressCallback.onProgress(finalProgress,
                            "Chargement de " + entry.getValue().getComponentName());
                }
                return entry.getValue().load();
            });
            currentProgress += progressStep;
        }

        return future.whenComplete((v, ex) -> {
            if (ex != null) {
                LOGGER.error("Error during loading components", ex);
            } else {
                LOGGER.info("All components loaded successfully");
                if (progressCallback != null) {
                    progressCallback.onProgress(1.0, "Chargement terminé");
                    progressCallback.onComplete();
                }
            }
        });
    }

    public <T extends LoadableComponent> T getComponent(final Class<T> componentClass) {
        return componentClass.cast(this.components.get(componentClass));
    }

    public boolean hasErrors() {
        return this.components.values().stream().anyMatch(LoadableComponent::hasError);
    }

    public Map<String, String> getErrors() {
        final Map<String, String> errors = new LinkedHashMap<>();
        for (final LoadableComponent component : this.components.values()) {
            if (component.hasError()) {
                errors.put(component.getComponentName(), component.getLastError());
            }
        }
        return errors;
    }

}
