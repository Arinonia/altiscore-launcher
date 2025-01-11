package fr.arinonia.launcher.loader;

import fr.arinonia.launcher.api.LauncherAPI;
import fr.arinonia.launcher.api.models.News;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NewsLoader extends LoadableComponent {

    private final LauncherAPI launcherAPI;
    private final List<News> news = new ArrayList<>();

    public NewsLoader(final LauncherAPI launcherAPI) {
        this.launcherAPI = launcherAPI;
    }

    @Override
    protected CompletableFuture<Void> initialize() {
        return this.launcherAPI.fetchNews().thenAccept(newsResponse -> {
            this.news.clear();
            this.news.addAll(newsResponse);
            LOGGER.info("Loaded {} news items", newsResponse.size());
        });
    }

    @Override
    protected String getComponentName() {
        return "News Manager";
    }

    public List<News> getNews() {
        return this.news;
    }
}
