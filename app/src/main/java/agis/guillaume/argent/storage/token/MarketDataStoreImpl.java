package agis.guillaume.argent.storage.token;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Store information related to the markets. Currently only store the pairs available in memory.
 * If I would have more time I would store those information in room
 */
public class MarketDataStoreImpl implements MarketDataStore {

    @Inject
    public MarketDataStoreImpl() { }

    // for the sake of this test I store the valid pair in the memory. In the real app I would instead store them in
    // Room
    private List<String> validPairs = new ArrayList<>();


    /**
     * Check if a pair is valid
     * @param pair pair to validate
     * @return true if the pair is valid, false otherwise
     */
    @Override
    public boolean isValidPair(@NonNull String pair) {
        return validPairs.contains(pair);
    }

    /**
     * Store a list of pairs locally
     * @param pairs pair to store locally
     */
    @Override
    public void storePairs(List<String> pairs) {
        this.validPairs.clear();
        this.validPairs.addAll(pairs);
    }

    /**
     * Return the list of pairs
     * @return the list of pairs
     */
    @Override
    public List<String> getValidPairs() {
        return validPairs;
    }
}
