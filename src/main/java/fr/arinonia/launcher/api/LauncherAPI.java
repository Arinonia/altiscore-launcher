package fr.arinonia.launcher.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.arinonia.launcher.api.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LauncherAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(LauncherAPI.class);
    //! Maybe move this to Constants.java
    private static final String BASE_URL = "https://launcher.altiscore.fr/launcher";
    //! Same as above
    private static final Gson GSON = new GsonBuilder().create();

    public CompletableFuture<LauncherConfig> fetchLauncherConfig() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.info("Fetching launcher configuration...");
                return get("/config.json", LauncherConfig.class);
            } catch (final Exception e) {
                LOGGER.error("An error occurred while fetching launcher config", e);
                return null;
            }
        });
    }

    public CompletableFuture<List<News>> fetchNews() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.info("Fetching news...");
                final NewsResponse response = get("/news.json", NewsResponse.class);
                return response != null ? response.getNews() : new ArrayList<>();
            } catch (final IOException e) {
                LOGGER.error("Failed to fetch news", e);
                throw new RuntimeException("Failed to fetch news", e);
            }
        });
    }

    public CompletableFuture<List<Server>> fetchServers() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.info("Fetching servers...");
                final ServerResponse response = get("/servers.json", ServerResponse.class);
                return response != null ? response.getServers() : new ArrayList<>();
            } catch (final IOException e) {
                LOGGER.error("Failed to fetch servers", e);
                throw new RuntimeException("Failed to fetch servers", e);
            }
        });
    }

    private <T> T get(final String endpoint, final Class<T> responseType) throws IOException {
        final URL url = URI.create(BASE_URL + endpoint).toURL();
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        final int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (final InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                return GSON.fromJson(reader, responseType);
            }
        } else {
            throw new IOException("HTTP error code: " + responseCode);
        }
    }
}
