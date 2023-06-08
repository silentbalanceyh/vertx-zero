package io.horizon.util;

import io.horizon.exception.WebException;
import io.horizon.exception.web._500InternalServerException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lang : 2023-05-11
 */
public class HIoTestCase {

    @Test
    public void testWeb500() {
        final WebException error = new _500InternalServerException(this.getClass(), "Error Message");
        System.out.println(error.getMessage());
        System.out.println(error.toJson().encodePrettily());
        Assert.assertNotNull(error);
    }
}
