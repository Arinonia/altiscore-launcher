package fr.arinonia.launcher.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.arinonia.launcher.config.models.Account;
import fr.arinonia.launcher.config.models.AuthConfig;
import fr.arinonia.launcher.file.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class AuthConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthConfigManager.class);
    private static final String CONFIG_FILE = "auth.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private AuthConfig authConfig;
    private final FileManager fileManager;

    public AuthConfigManager(final FileManager fileManager) {
        this.fileManager = fileManager;
        load();
    }

    private void load() {
        LOGGER.info("Loading auth config...");
        try {
            if (Files.exists(this.fileManager.getAuthConfigPath())) {
                final String json = Files.readString(this.fileManager.getAuthConfigPath());
                this.authConfig = GSON.fromJson(json, AuthConfig.class);
                LOGGER.info("Auth config loaded");
            } else {
                LOGGER.info("Auth config not found, creating a new one");
                this.authConfig = new AuthConfig();
                save();
            }
        } catch (final IOException e) {
            LOGGER.error("Error while loading auth config", e);
            this.authConfig = new AuthConfig();
        }
    }

    private void save() {
        try {
            final String json = GSON.toJson(this.authConfig);
            Files.writeString(this.fileManager.getAuthConfigPath(), json);
            LOGGER.info("Auth config saved");
        } catch (final IOException e) {
            LOGGER.error("Error while saving auth config", e);
        }
    }

    public void addAccount(final String uuid, final String username, final String accessToken, final String refreshToken) {
        final Account account = new Account();
        account.setUuid(uuid);
        account.setUsername(username);
        account.setAccessToken(accessToken);
        account.setRefreshToken(refreshToken);

        this.authConfig.getAccounts().add(account);
        if (this.authConfig.getSelectedAccount() == null) {
            this.authConfig.setSelectedAccount(uuid);
        }
        save();
    }

    public void addAccount(final Account account) {
        addAccount(account.getUuid(), account.getUsername(), account.getAccessToken(), account.getRefreshToken());
    }

    public void removeAccount(final String uuid) {
        this.authConfig.getAccounts().removeIf(account -> account.getUuid().equals(uuid));
        if (this.authConfig.getSelectedAccount() != null && this.authConfig.getSelectedAccount().equals(uuid)) {
            this.authConfig.setSelectedAccount(
                    this.authConfig.getAccounts().isEmpty() ? null : this.authConfig.getAccounts().get(0).getUuid()
            );
        }
        save();
    }

    public void setSelectedAccount(final String uuid) {
        this.authConfig.setSelectedAccount(uuid);
        save();
    }

    public Account getSelectedAccount() {
        if (this.authConfig.getSelectedAccount() == null) {
            return null;
        }
        return this.authConfig.getAccounts().stream()
                .filter(account -> account.getUuid().equals(this.authConfig.getSelectedAccount()))
                .findFirst()
                .orElse(null);
    }

    public List<Account> getAccounts() {
        return this.authConfig.getAccounts();
    }

    public void updateAccountTokens(final String uuid, final String accessToken, final String refreshToken) {
        this.authConfig.getAccounts().stream()
                .filter(account -> account.getUuid().equals(uuid))
                .findFirst()
                .ifPresent(account -> {
                    account.setAccessToken(accessToken);
                    account.setRefreshToken(refreshToken);
                    save();
                });
    }

    public AuthConfig getAuthConfig() {
        return this.authConfig;
    }
}
