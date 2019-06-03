package agis.guillaume.argent.models.requests;

import com.google.gson.annotations.SerializedName;

public class BalanceResponse {

    @SerializedName("result")
    private String result;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
