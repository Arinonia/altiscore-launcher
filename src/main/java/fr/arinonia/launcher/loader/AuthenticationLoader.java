package fr.arinonia.launcher.loader;

import fr.arinonia.launcher.Launcher;
import fr.arinonia.launcher.config.models.Account;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AuthenticationLoader extends LoadableComponent {
    private final Launcher launcher;
    private Account validatedAccount;

    public AuthenticationLoader(final Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    protected CompletableFuture<Void> initialize() {
            return CompletableFuture.supplyAsync(() -> {
                LOGGER.info("Verifying account...");
                final List<Account> accounts = this.launcher.getAuthConfigManager().getAccounts();

                if (accounts.isEmpty()) {
                    LOGGER.info("No accounts found.");
                    return null;
                }

                for (final Account account : accounts) {
                    if (account == null || account.getUsername() == null || account.getUsername().isEmpty()
                            || account.getRefreshToken() == null || account.getRefreshToken().isEmpty()) {
                        continue;
                    }

                    if (this.launcher.getAuthenticationManager().isAccountValid(account)) {
                        LOGGER.info("Account {} is valid.", account.getUsername());
                        this.validatedAccount = account;
                        this.launcher.setSelectedAccount(account);
                        return null;
                    } else {
                        LOGGER.info("Account {} is invalid.", account.getUsername());
                        this.launcher.getAuthConfigManager().removeAccount(account.getUuid());
                    }

                }
                LOGGER.info("No valid accounts found.");
                return null;
            });
    }

    @Override
    protected String getComponentName() {
        return "Authentication...";
    }

    public Account getValidatedAccount() {
        return this.validatedAccount;
    }
}
