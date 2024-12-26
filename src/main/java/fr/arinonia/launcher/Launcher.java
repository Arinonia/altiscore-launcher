package fr.arinonia.launcher;

import fr.arinonia.launcher.auth.AuthenticationManager;
import fr.arinonia.launcher.config.AuthConfigManager;
import fr.arinonia.launcher.config.models.Account;
import fr.arinonia.launcher.config.models.AuthConfig;
import fr.arinonia.launcher.file.FileManager;
import fr.arinonia.launcher.ui.PanelManager;
import fr.arinonia.launcher.ui.panels.home.HomePanel;
import fr.arinonia.launcher.ui.panels.loading.LoadingPanel;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);
    private FileManager fileManager;
    private AuthConfigManager authConfigManager;
    private PanelManager panelManager;
    private AuthenticationManager authenticationManager;
    private Account selectedAccount;

    public void init(final Stage stage) {
        LOGGER.info("Starting launcher...");
        this.fileManager = new FileManager();
        this.fileManager.createDirectories();

        this.authConfigManager = new AuthConfigManager(this.fileManager);
        this.authenticationManager = new AuthenticationManager(this.authConfigManager, this);

        this.panelManager = new PanelManager(this);
        this.panelManager.init(stage);
        //TODO show home panel
        this.panelManager.addPanel(new LoadingPanel());
        this.panelManager.addPanel(new HomePanel());
        this.panelManager.showPanel(LoadingPanel.class);

    }

    public void checkAuth() {
        // consider to create an Account object for the current account in Launcher class
        final AuthConfig authConfig = this.authConfigManager.getAuthConfig();
        if (authConfig.getSelectedAccount() != null) {
            if (this.authenticationManager.isAccountValid(this.authConfigManager.getSelectedAccount())) {
                LOGGER.info("Account {} is valid", authConfig.getSelectedAccount());
                this.selectedAccount = this.authConfigManager.getSelectedAccount();
            } else {
                LOGGER.info("Account {} is not valid", authConfig.getSelectedAccount());
                this.authConfigManager.setSelectedAccount(null);
            }
        } else {
            LOGGER.info("No account selected");
        }
    }


    public AuthenticationManager getAuthenticationManager() {
        return this.authenticationManager;
    }

    public void setSelectedAccount(final Account selectedAccount) {
        this.authConfigManager.setSelectedAccount(selectedAccount != null ? selectedAccount.getUuid() : null);
        this.selectedAccount = selectedAccount;
    }

    public Account getSelectedAccount() {
        return this.selectedAccount;
    }
}
