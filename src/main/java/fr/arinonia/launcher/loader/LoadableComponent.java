package fr.arinonia.launcher.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public abstract class LoadableComponent {
    protected final Logger LOGGER;
    protected LoadingState state = LoadingState.NOT_STARTED;
    protected String error;

    //! maybe add LauncherAPI here
    public LoadableComponent() {
        this.LOGGER = LoggerFactory.getLogger(this.getClass());
    }

    public CompletableFuture<Void> load() {
        if (this.state == LoadingState.LOADING) {
            LOGGER.warn("{} is already loading", getComponentName());
            return CompletableFuture.completedFuture(null);
        }
        this.state = LoadingState.LOADING;
        LOGGER.info("Loading component: {}", getComponentName());

        return initialize().thenRun(() -> {
            this.state = LoadingState.LOADED;
            LOGGER.info("Component {} loaded", getComponentName());
        }).exceptionally(throwable -> {
            this.state = LoadingState.ERROR;
            this.error = throwable.getMessage();
            LOGGER.error("An error occurred while loading component {}", getComponentName(), throwable);
            return null;
        });
    }

    protected abstract CompletableFuture<Void> initialize();

    protected abstract String getComponentName();

    public LoadingState getState() {
        return this.state;
    }

    public String getLastError() {
        return this.error;
    }

    public boolean isLoaded() {
        return this.state == LoadingState.LOADED;
    }

    public boolean hasError() {
        return this.state == LoadingState.ERROR;
    }

    public enum LoadingState {
        NOT_STARTED,
        LOADING,
        LOADED,
        ERROR
    }
}
