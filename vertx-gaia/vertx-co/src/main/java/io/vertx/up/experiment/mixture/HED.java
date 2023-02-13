package io.vertx.up.experiment.mixture;

import io.vertx.up.eon.Strings;
import io.vertx.up.experiment.specification.KPair;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * To avoid Open-Source and Product Conflict
 * 1) The default KPair must be loaded from `HED` interface, here:
 * - E: encrypt
 * - D: decrypt
 * 2) Zero Framework Provide Non-Implementation to be sure any customer/user could not see
 * the content of privateKey/publicKey
 * 3) This implementation class could be configured in
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HED {
    static void sourceGen(final String className, final int length) {
        System.out.println(T.generate(className, length));
    }

    /*
     * RSA / DSA / ECC / DH
     */
    KPair loadRSA();
}

class T {
    private static List<String> sourceNorm(final String region, final int adjust) {
        final List<String> source = new ArrayList<>();
        // start = end, 31 -> adjust
        final int total = region.length() - adjust;
        final int time = total / 68 + 1;
        for (int timeIdx = 0; timeIdx < time; timeIdx++) {
            final int lineStart = adjust + 68 * timeIdx;
            final int lineEnd = lineStart + 68;
            if (lineEnd < region.length() && lineStart < region.length()) {
                source.add("        \"" + region.substring(lineStart, lineEnd) + "\" + ");
            } else {
                source.add("        \"" + region.substring(lineStart, region.length() - 1) + "\";");
            }
        }
        return source;
    }

    static String generate(final String className, final int length) {
        final KPair pair = Ut.randomRsa(length);
        // PUBLIC_KEY
        final StringBuilder content = new StringBuilder();
        // PRIVATE_KEY
        final List<String> source = new ArrayList<>();
        source.add("package cn.vertxup.uca.extension;");
        source.add(Strings.EMPTY);
        source.add("import io.vertx.up.experiment.mixture.HED;");
        source.add("import io.vertx.up.experiment.specification.KPair;");
        source.add(Strings.EMPTY);
        source.add(MessageFormat.format("public class {0} implements HED", className) + " {");
        {
            // PUBLIC_KEY
            source.add(Strings.EMPTY);
            final StringBuilder strPub = new StringBuilder();
            strPub.append("    private static final String PUBLIC_KEY = \"");
            final String region = pair.getPublicKey();
            // 31 offset
            strPub.append(region, 0, 31).append("\" +");
            source.add(strPub.toString());
            // start = end, 31 -> adjust
            source.addAll(sourceNorm(region, 31));
        }
        {
            // PRIVATE_KEY
            source.add(Strings.EMPTY);
            final StringBuilder strPri = new StringBuilder();
            strPri.append("    private static final String PRIVATE_KEY = \"");
            final String region = pair.getPrivateKey();
            // 30 offset
            strPri.append(region, 0, 30).append("\" +");
            source.add(strPri.toString());
            // start = end, 30 -> adjust
            source.addAll(sourceNorm(region, 30));
        }
        source.add(Strings.EMPTY);
        source.add("    @Override");
        source.add("    public KPair loadRSA() {");
        source.add("        return new KPair(PUBLIC_KEY, PRIVATE_KEY);");
        source.add("    }");
        source.add("}");
        return Ut.fromJoin(source, "\n");
    }
}
