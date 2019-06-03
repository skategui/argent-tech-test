package agis.guillaume.argent.models.requests;

import agis.guillaume.argent.models.ERC20TokenTx;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ERC20TokenResponse {

    @SerializedName("result")
    private List<ERC20TokenTx> result;

    public List<ERC20TokenTx> getResult() {
        return result;
    }

    public void setResult(List<ERC20TokenTx> result) {
        this.result = result;
    }
}
