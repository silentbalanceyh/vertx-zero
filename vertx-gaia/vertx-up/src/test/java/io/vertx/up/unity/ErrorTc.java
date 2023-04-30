package io.vertx.up.unity;

import io.horizon.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import org.junit.Assert;
import org.junit.Test;

public class ErrorTc {

    @Test
    public void buildError() {
        final WebException error =
            new _500InternalServerException(getClass(), "Error Internal");
        System.out.println(error);
        Assert.assertNotNull(error);
    }
}
