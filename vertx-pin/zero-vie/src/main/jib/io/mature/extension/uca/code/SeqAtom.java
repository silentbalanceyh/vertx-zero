package io.mature.extension.uca.code;

import io.vertx.core.Future;
import io.vertx.mod.atom.modeling.builtin.DataAtom;

import java.util.Objects;
import java.util.Queue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SeqAtom extends AbstractSeq<DataAtom> {

    SeqAtom(final String sigma) {
        super(sigma);
    }

    @Override
    public Future<Queue<String>> generate(final DataAtom atom, final Integer counter) {
        Objects.requireNonNull(atom);
        final String identifier = atom.identifier();
        /*
         * WHERE SIGMA = ? AND IDENTIFIER = ?
         */
        return this.stub().numberSigmaI(this.sigma(), identifier, counter)
            .compose(this::batch);
    }
}
