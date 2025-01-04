package fr.arinonia.launcher.auth;

import fr.arinonia.launcher.Launcher;
import fr.arinonia.launcher.config.AuthConfigManager;
import fr.arinonia.launcher.config.models.Account;
import fr.arinonia.launcher.utils.CallBack;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class AuthenticationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationManager.class);

    private final AuthConfigManager authConfigManager;
    private final MicrosoftAuthenticator microsoftAuthenticator;
    private final Launcher launcher;

    public AuthenticationManager(final AuthConfigManager authConfigManager, final Launcher launcher) {
        this.authConfigManager = authConfigManager;
        this.microsoftAuthenticator = new MicrosoftAuthenticator();
        this.launcher = launcher;
    }

    public void authenticate(final CallBack callBack) {
        LOGGER.info("Authenticating...");
        final CompletableFuture<MicrosoftAuthResult> result = this.microsoftAuthenticator.loginWithAsyncWebview();
        result.thenAccept(res -> {
            handleAuthResult(res);
            callBack.onComplete();
        }).exceptionally(e -> {
            LOGGER.error("Failed to authenticate with Microsoft", e);
            //! we need the notification system for that
            //callBack.onError(e, "Failed to authenticate with Microsoft");
            return null;
        });
    }

    private void handleAuthResult(final MicrosoftAuthResult result) {
        LOGGER.info("Successfully authenticated with Microsoft");
        final Account account = new Account();

        account.setUuid(result.getProfile().getId());
        account.setUsername(result.getProfile().getName());
        account.setAccessToken(result.getAccessToken());
        account.setRefreshToken(result.getRefreshToken());

        this.authConfigManager.addAccount(account);
        this.launcher.setSelectedAccount(account);
    }

    public boolean refreshToken(final Account account) {
        LOGGER.info("Refreshing token...");
        try {
            final MicrosoftAuthResult result = this.microsoftAuthenticator.loginWithRefreshToken(account.getRefreshToken());

            this.authConfigManager.updateAccountTokens(
                   account.getUuid(),
                        result.getAccessToken(),
                        result.getRefreshToken()
            );
            LOGGER.info("Successfully refreshed tokens for {}", account.getUsername());
            return true;
        } catch (final MicrosoftAuthenticationException e) {
            LOGGER.error("Failed to refresh token for {}", account.getUsername(), e);
            return false;
        }
    }

    public boolean isAccountValid(final Account account) {
        return account != null && refreshToken(account);
    }
}
