package agis.guillaume.argent;

import agis.guillaume.argent.models.ERC20TokenTx;
import agis.guillaume.argent.models.ERC20TokenUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Dummy data only used for this test purposes. Would use a Builder pattern otherwise, to avoid providing
 * static values
 */
public class DataBuilder {

    public static List<String> generatePairs() {
        List<String> pairs = new ArrayList<>();
        pairs.add("BTC-ETH");
        pairs.add("OMG-ETH");
        pairs.add("TRX-ETH");
        return pairs;
    }

    public static List<ERC20TokenTx> generateERC20Tokens() {
        List<ERC20TokenTx> tokens = new ArrayList<>();
        ERC20TokenTx btc = new ERC20TokenTx();
        btc.setTokenSymbol("BTC");
        btc.setTokenName("bitcoin");
        btc.setBalance("0.42");
        btc.setTransactionFrom(BuildConfig.ACCOUNT_ADDR);

        ERC20TokenTx eth = new ERC20TokenTx();
        eth.setTokenSymbol("ETH");
        eth.setTokenName("ethereum");
        eth.setBalance("1.11");
        eth.setTransactionFrom(BuildConfig.ACCOUNT_ADDR);

        ERC20TokenTx omg = new ERC20TokenTx();
        omg.setTokenSymbol("OMG");
        omg.setTokenName("ohmygoodd");
        omg.setBalance("42");
        omg.setTransactionFrom(BuildConfig.ACCOUNT_ADDR);

        tokens.add(btc);
        tokens.add(eth);
        tokens.add(omg);
        return tokens;
    }

    public static List<ERC20TokenUser> getErc20TokensWithBalance() {
        ERC20TokenUser btc = new ERC20TokenUser("Bitcoin", "BTC", new BigDecimal(2), new BigDecimal(10));
        ERC20TokenUser omg = new ERC20TokenUser("OmiseGo", "OMG", new BigDecimal(2), new BigDecimal(10));
        ERC20TokenUser xvg = new ERC20TokenUser("Verge", "XVG", new BigDecimal(20), new BigDecimal(1));
        List<ERC20TokenUser> list = new ArrayList<>();
        list.add(btc);
        list.add(omg);
        list.add(xvg);
        return list;
    }

}
