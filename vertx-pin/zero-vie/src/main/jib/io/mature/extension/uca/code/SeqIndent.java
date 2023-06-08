package io.mature.extension.uca.code;

import io.vertx.core.Future;

import java.util.Queue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SeqIndent extends AbstractSeq<String> {

    SeqIndent(final String sigma) {
        super(sigma);
    }

    @Override
    public Future<Queue<String>> generate(final String input, final Integer counter) {
        return this.stub().numberSigma(this.sigma(), input, counter)
            .compose(this::batch);
    }
}
