package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

/*
 * Zero Quiz needed when you used QzTc / QzMicroTc
 * It means that all the test case name must end with "Tc"
 * Because the name will be parsed.
 */
public class TestCaseNameException extends UpException {

    public TestCaseNameException(final Class<?> clazz,
                                 final String caseName) {
        super(clazz, caseName);
    }

    @Override
    public int getCode() {
        return -40062;
    }
}
