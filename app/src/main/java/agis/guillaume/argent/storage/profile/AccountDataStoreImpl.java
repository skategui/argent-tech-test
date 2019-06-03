package agis.guillaume.argent.storage.profile;

import agis.guillaume.argent.models.Coin;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Store information related to the account. Currently only store the ETH balance
 * and the ERc 20 tokens for this account.
 * If I would have more time I would store those information in room
 */
public class AccountDataStoreImpl implements AccountDataStore {

    private static final String BALANCE_ETH = "balance_eth";
    private static final String ERC20_TOKENS = "erc_tokens";

    private SharedPreferences preferences;
    private Gson gson;

    /**
     * Default constructor, dependencies provided by dagger
     *
     * @param preferences SharedPreferences instance
     * @param gson        Gson instance, not ser/de
     */
    @Inject
    public AccountDataStoreImpl(SharedPreferences preferences, Gson gson) {
        this.preferences = preferences;
        this.gson = gson;
    }

    /**
     * Store the ETh balance locally
     *
     * @param ethBalance eth balance
     */
    @Override
    public void setBalanceEth(BigDecimal ethBalance) {
        preferences.edit()
                .putString(BALANCE_ETH, ethBalance.toString())
                .apply();
    }

    /**
     * Store the erc 20 tokens locally
     *
     * @param tokens erc 20 tokens
     */
    @Override
    public void setERC20TokensBalance(@NonNull List<Coin> tokens) {
        preferences.edit()
                .putString(ERC20_TOKENS, gson.toJson(tokens))
                .apply();
    }

    /**
     * Get the stored ETH balance associated to this account if any
     *
     * @return the stored value, 0.f otherwise
     */
    @Override
    public BigDecimal getEthBalance() {
        String value = preferences.getString(BALANCE_ETH, null);
        if (value == null)
            return new BigDecimal(0);
        return new BigDecimal(value);
    }

    /**
     * Get the stored erc 20 tokens with the balance associated, of the user
     *
     * @return get the stored ERC 20 tokens balance , null otherwise
     */
    @Nullable
    @Override
    public List<Coin> getERC20TokenBalance() {
        String content = preferences.getString(ERC20_TOKENS, null);
        if (content == null)
            return null;
        return gson.fromJson(content, new TypeToken<ArrayList<Coin>>() {
        }.getType());
    }
}
