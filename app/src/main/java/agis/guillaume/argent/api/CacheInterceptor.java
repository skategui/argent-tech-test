package agis.guillaume.argent.api;

import androidx.annotation.NonNull;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Custom interceptor for the Ok HttpClient to provide a cache to the requests
 */
public class CacheInterceptor  implements Interceptor {

    /**
     * Cache expiry time in minute
     */
    private static int cacheMaxAgeMinute = 10;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain)
            throws IOException {

        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(cacheMaxAgeMinute, TimeUnit.MINUTES)
                .build();

        Request request = chain.request();
        request = request.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build();
        return chain.proceed(request);
    }
}