package agis.guillaume.argent.models.requests;

import com.google.gson.annotations.SerializedName;

public class MarketInfo {

    @SerializedName("pair")
    private String pair;
    @SerializedName("rate")
    private Double rate;

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
