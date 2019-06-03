package agis.guillaume.argent.storage.profile;

import agis.guillaume.argent.models.Coin;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDataStore {


    /**
     * Store the ETh balance locally
     * @param ethBalance eth balance
     */
    void setBalanceEth(BigDecimal ethBalance);


    /**
     * Store the erc 20 tokens locally
     * @param tokens erc 20 tokens
     */
    void setERC20TokensBalance(List<Coin> tokens);


    /**
     * Get the stored ETH balance if any
     * @return the stored value, 0.f otherwise
     */
    BigDecimal getEthBalance();

    /**
     * Get the stored erc 20 tokens
     * @return get the stored ERC 20 tokens , null otherwise
     */
    @Nullable
    List<Coin> getERC20TokenBalance();
}
