package io.vertx.up.backbone.announce;

import io.horizon.exception.WebException;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.up.atom.Rule;
import io.vertx.up.backbone.regular.Ruler;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Verify special workflow
 */
public class FileRigor implements Rigor {

    @Override
    public WebException verify(final Map<String, List<Rule>> rulers,
                               final Object body) {
        WebException error = null;
        if (!rulers.isEmpty()) {
            // Merge rulers here.
            final Set<Rule> rules = new HashSet<>();
            Observable.fromIterable(rulers.keySet())
                .map(rulers::get)
                .flatMap(Observable::fromIterable)
                .subscribe(rules::add)
                .dispose();
            // Rules here.
            error = Ruler.verify(rules, "BODY", body);
        }
        return error;
    }
}
