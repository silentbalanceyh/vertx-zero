package io.vertx.up.exception;

import io.horizon.exception.WebException;
import io.vertx.up.exception.web._403ForbiddenException;
import org.junit.Assert;
import org.junit.Test;

public class ErrorBuildTc {

    @Test
    public void testError() {
        final WebException failure = new _403ForbiddenException(getClass());
        Assert.assertNotNull(failure);
    }
}
