package agis.guillaume.argent.utils;

import java.math.BigDecimal;
import java.math.MathContext;

public class WeiUtils {

    private static final BigDecimal wei_to_ether_divider = new BigDecimal(1000000000000000000.d);

    /**
     * Convert a balance, in WEI, into ETH
     * @param wei wei amount
     * @return [double] ETH associated to the WEI balance
     */
    public static BigDecimal convertWeiToETH(BigDecimal wei) {
        return wei.divide(wei_to_ether_divider, MathContext.UNLIMITED);
    }
}
