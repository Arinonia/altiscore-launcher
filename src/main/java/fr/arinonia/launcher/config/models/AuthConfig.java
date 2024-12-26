package fr.arinonia.launcher.config.models;

import java.util.ArrayList;
import java.util.List;

public class AuthConfig {

    private String selectedAccount;
    private List<Account> accounts;

    public AuthConfig() {
        this.accounts = new ArrayList<>();
    }

    public String getSelectedAccount() {
        return this.selectedAccount;
    }

    public void setSelectedAccount(final String selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(final List<Account> accounts) {
        this.accounts = accounts;
    }
}
