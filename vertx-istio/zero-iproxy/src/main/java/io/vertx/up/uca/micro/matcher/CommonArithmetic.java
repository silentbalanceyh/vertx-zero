package io.vertx.up.uca.micro.matcher;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.Record;
import io.vertx.up.eon.Strings;
import io.vertx.up.uca.matcher.RegexPath;
import io.vertx.up.uca.micro.discovery.Origin;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Simple load balancer arithmetic
 */
public class CommonArithmetic implements Arithmetic {
    @Override
    public Record search(final List<Record> records,
                         final RoutingContext context) {
        final HttpServerRequest request = context.request();
        // Input source
        final String uri = request.path();
        final Optional<Record> hitted =
            records.stream()
                .filter(record -> this.isMatch(uri, record))
                .findAny();
        // Find valid;
        return hitted.orElse(null);
    }

    /**
     * Match calculation.
     *
     * @param uri    real input from client include path variable
     * @param record discovery record in backend ( From etcd3 )
     *
     * @return whether the uri match and could be found in etcd3
     */
    private boolean isMatch(final String uri, final Record record) {
        final JsonObject data = record.getMetadata();
        boolean match = false;
        if (data.containsKey(Origin.PATH)) {
            final String path = data.getString(Origin.PATH);
            if (!Ut.isNil(path) && path.contains(Strings.COLON)) {
                final Pattern pattern = RegexPath.createRegex(path);
                match = pattern.matcher(uri).matches();
            } else {
                match = path.equalsIgnoreCase(uri);
            }
        }
        return match;
    }
}
