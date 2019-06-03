package agis.guillaume.argent.api;

import java.net.UnknownHostException;

/**
 * Utils to check the type of HTTP error thrown
 */
public class HttpErrorUtils {
    /**
     * Check if the user has list the connexion given the throwable thrown
     *
     * @param throwable throwable to check
     * @return true if he user has lost connexion, false otherwise
     */
    public Boolean hasLostInternet(Throwable throwable) {
        return throwable instanceof UnknownHostException;
    }
}
