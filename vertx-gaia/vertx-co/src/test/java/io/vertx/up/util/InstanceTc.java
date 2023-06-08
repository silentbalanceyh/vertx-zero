package io.vertx.up.util;

import io.horizon.uca.log.Annal;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

public class InstanceTc extends ZeroBase {

    @Test
    public void testString() {
        final User user = new User();
        Ut.invoke(user, "invoke", "String");
    }

    @Test
    public void testNoArg() {
        final User user = new User();
        Ut.invoke(user, "invoke");
    }

    @Test
    public void testWrapperInteger() {
        final Email email = new Email();
        Ut.invoke(email, "invoke", 22);
    }

    @Test
    public void testUnboxInteger() {
        final Email email = new Email();
        Ut.invoke(email, "invoke", 22);
    }
}

class Email {

    private static final Annal LOGGER = Annal.get(Email.class);

    public void invoke(final Integer integer) {
        LOGGER.info("Email: invoke(Integer) " + integer);
    }

    public void invoke(final int integer) {

        LOGGER.info("Email: invoke(int)" + integer);
    }
}

class User {
    private static final Annal LOGGER = Annal.get(User.class);

    public void invoke() {
        LOGGER.info("User: invoke. ");
    }

    public void invoke(final String name) {
        LOGGER.info("User: invoke(String)" + name);
    }

    public void invoke(final int integer) {
        LOGGER.info("User: invoke(Integer)" + integer);
    }
}
