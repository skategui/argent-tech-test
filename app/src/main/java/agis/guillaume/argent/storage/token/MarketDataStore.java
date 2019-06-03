package agis.guillaume.argent.storage.token;

import androidx.annotation.NonNull;

import java.util.List;

public interface MarketDataStore {

    /**
     * Check if a pair is valid
     * @param pair pair to validate
     * @return true if the pair is valid, false otherwise
     */
    boolean isValidPair(@NonNull String pair);


    /**
     * Store a list of pairs locally
     * @param pairs pair to store locally
     */
    void storePairs(List<String> pairs);


    /**
     * Return the list of pairs
     * @return the list of pairs
     */
    List<String> getValidPairs();
}
