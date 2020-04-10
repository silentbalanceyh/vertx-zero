package io.vertx.up.uca.rs.announce;

import io.reactivex.Observable;
import io.vertx.up.atom.Rule;
import io.vertx.up.exception.WebException;
import io.vertx.up.uca.rs.regular.Ruler;

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
