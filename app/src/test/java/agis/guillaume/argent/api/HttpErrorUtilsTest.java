package agis.guillaume.argent.api;

import com.google.common.truth.Truth;
import org.junit.Test;

import java.net.UnknownHostException;

public class HttpErrorUtilsTest {

    private HttpErrorUtils httpErrorUtils = new HttpErrorUtils();

    @Test
    public void Check_if_the_user_has_lost_the_connexion_given_a_UnknownHostException() {
        Truth.assertThat(httpErrorUtils.hasLostInternet(new UnknownHostException())).isTrue();
    }

    @Test
    public void Check_if_the_user_has_lost_the_connexion_given_an_exception_that_is_not_UnknownHostException() {
        Truth.assertThat(httpErrorUtils.hasLostInternet(new RuntimeException())).isFalse();
    }

}