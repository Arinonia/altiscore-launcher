package fr.arinonia.launcher;

import fr.arinonia.launcher.api.models.News;
import fr.arinonia.launcher.api.models.Server;
import fr.arinonia.launcher.auth.AuthenticationManager;
import fr.arinonia.launcher.config.AuthConfigManager;
import fr.arinonia.launcher.config.models.Account;
import fr.arinonia.launcher.file.FileManager;
import fr.arinonia.launcher.loader.LauncherLoader;
import fr.arinonia.launcher.loader.NewsLoader;
import fr.arinonia.launcher.loader.ServerLoader;
import fr.arinonia.launcher.ui.PanelManager;
import fr.arinonia.launcher.ui.panels.home.HomePanel;
import fr.arinonia.launcher.ui.panels.loading.LoadingPanel;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);
    private FileManager fileManager;
    private AuthConfigManager authConfigManager;
    private PanelManager panelManager;
    private AuthenticationManager authenticationManager;
    private LauncherLoader launcherLoader;
    private Account selectedAccount;


    public void init(final Stage stage) {
        LOGGER.info("Starting launcher...");
        this.fileManager = new FileManager();
        this.fileManager.createDirectories();

        this.authConfigManager = new AuthConfigManager(this.fileManager);
        this.authenticationManager = new AuthenticationManager(this.authConfigManager, this);
        this.launcherLoader = new LauncherLoader(this);
        this.panelManager = new PanelManager(this);
        this.panelManager.init(stage);

        this.panelManager.addPanel(new LoadingPanel());
        this.panelManager.addPanel(new HomePanel());
        this.panelManager.showPanel(LoadingPanel.class);
    }

    public AuthenticationManager getAuthenticationManager() {
        return this.authenticationManager;
    }

    public void setSelectedAccount(final Account selectedAccount) {
        this.authConfigManager.setSelectedAccount(selectedAccount != null ? selectedAccount.getUuid() : null);
        this.selectedAccount = selectedAccount;
    }

    public List<News> getNews() {
        return this.launcherLoader.getComponent(NewsLoader.class).getNews();
    }

    public List<Server> getServers() {
        return this.launcherLoader.getComponent(ServerLoader.class).getServers();
    }

    public Account getSelectedAccount() {
        return this.selectedAccount;
    }

    public AuthConfigManager getAuthConfigManager() {
        return this.authConfigManager;
    }

    public LauncherLoader getLauncherLoader() {
        return this.launcherLoader;
    }

}
