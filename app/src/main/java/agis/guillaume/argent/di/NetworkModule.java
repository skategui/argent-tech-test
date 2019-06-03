package agis.guillaume.argent.di;

import agis.guillaume.argent.BuildConfig;
import agis.guillaume.argent.api.CacheInterceptor;
import agis.guillaume.argent.api.HttpErrorUtils;
import agis.guillaume.argent.api.EtherScanResource;
import agis.guillaume.argent.api.ShapeShiftResource;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;
import java.io.File;
import java.util.concurrent.TimeUnit;


@Module
class NetworkModule {

    private static Long TIMEOUT_SEC = 20L;
    private static Long cacheSize = 1024 * 1024 * 10L; // 10 MB
    private static String cacheName = "cache_name";

    @Provides
    Cache createCache(Context context) {
        File cacheDir = new File(context.getCacheDir(), cacheName);
        return new Cache(cacheDir, cacheSize);
    }

    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    // responsible to create the cache interceptor. Simple one at the moment but could be improved
    // to cache differently per resource
    @Provides
    Interceptor provideNetworkCacheInterceptor() {
        return new CacheInterceptor();
    }

    @Provides
    @Singleton
    OkHttpClient createOkHttpClient(Interceptor networkInterceptor,
                                    HttpLoggingInterceptor httpLoggingInterceptor) {


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(networkInterceptor)
                .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG)
            builder.addInterceptor(httpLoggingInterceptor);

        return builder.build();
    }




    @Provides
    HttpErrorUtils createHttpErrorUtils() {
        return new HttpErrorUtils();
    }

    @Provides
    @Singleton
    EtherScanResource createEtherScanResource(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.ETHERSCAN_BASE_URL)
                .client(client)
                .addCallAdapterFactory((RxJava2CallAdapterFactory.create()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(EtherScanResource.class);
    }

    @Provides
    @Singleton
    ShapeShiftResource createShapeShiftResource(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SHAPESHIFT_BASE_URL)
                .client(client)
                .addCallAdapterFactory((RxJava2CallAdapterFactory.create()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ShapeShiftResource.class);
    }

}