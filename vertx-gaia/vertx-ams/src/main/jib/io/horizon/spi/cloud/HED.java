package io.horizon.spi.cloud;

import io.horizon.annotations.Development;
import io.horizon.atom.common.KPair;
import io.horizon.eon.VString;
import io.horizon.util.HUt;

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
    @Development("生成代码专用，负责生成HED代码")
    static void sourceGen(final String packageName, final String className, final int length) {
        System.out.println(HEDKit.generate(packageName, className, length));
    }

    /*
     * RSA / DSA / ECC / DH
     */
    KPair loadRSA();
}

class HEDKit {
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

    static String generate(final String packageName, final String className, final int length) {
        final KPair pair = HUt.randomRsa(length);
        // PRIVATE_KEY
        final List<String> source = new ArrayList<>();
        source.add("package " + packageName + ";");
        source.add(VString.EMPTY);
        source.add("import " + HED.class.getName() + ";");
        source.add("import " + KPair.class.getName() + ";");
        source.add(VString.EMPTY);
        source.add(MessageFormat.format("public class {0} implements HED", className) + " {");
        {
            // PUBLIC_KEY
            source.add(VString.EMPTY);
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
            source.add(VString.EMPTY);
            final StringBuilder strPri = new StringBuilder();
            strPri.append("    private static final String PRIVATE_KEY = \"");
            final String region = pair.getPrivateKey();
            // 30 offset
            strPri.append(region, 0, 30).append("\" +");
            source.add(strPri.toString());
            // start = end, 30 -> adjust
            source.addAll(sourceNorm(region, 30));
        }
        source.add(VString.EMPTY);
        source.add("    @Override");
        source.add("    public KPair loadRSA() {");
        source.add("        return new KPair(PUBLIC_KEY, PRIVATE_KEY);");
        source.add("    }");
        source.add("}");
        return HUt.fromJoin(source, "\n");
    }
}
