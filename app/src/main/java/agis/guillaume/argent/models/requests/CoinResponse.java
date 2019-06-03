package agis.guillaume.argent.models.requests;

import androidx.annotation.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

public class CoinResponse {

    private static final String STATUS_UNAVAILABLE = "unavailable";
    private static final String STATUS_AVAILABLE = "available";

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("name")
    private String name;


    @SerializedName("imageSmall")
    private String imageSmall;

    @SerializedName("status")
    private String status;

    public String getSymbol() {
        return symbol;
    }

    @VisibleForTesting
    public void statusAvailable() {
        this.status = STATUS_AVAILABLE;
    }

    public boolean isAvailable()
    {
        return !STATUS_UNAVAILABLE.equals(status);
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }
}
