package io.vertx.up.util;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.eon.Values;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: chunmei deng
 * Modified: 2020/03/22
 */

public class CodecTc extends ZeroBase {


    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static final long[] Kt = {
            0x428a2f98d728ae22l, 0x7137449123ef65cdl, 0xb5c0fbcfec4d3b2fl, 0xe9b5dba58189dbbcl, 0x3956c25bf348b538l,
            0x59f111f1b605d019l, 0x923f82a4af194f9bl, 0xab1c5ed5da6d8118l, 0xd807aa98a3030242l, 0x12835b0145706fbel,
            0x243185be4ee4b28cl, 0x550c7dc3d5ffb4e2l, 0x72be5d74f27b896fl, 0x80deb1fe3b1696b1l, 0x9bdc06a725c71235l,
            0xc19bf174cf692694L, 0xe49b69c19ef14ad2l, 0xefbe4786384f25e3l, 0x0fc19dc68b8cd5b5l, 0x240ca1cc77ac9c65l,
            0x2de92c6f592b0275l, 0x4a7484aa6ea6e483l, 0x5cb0a9dcbd41fbd4l, 0x76f988da831153b5l, 0x983e5152ee66dfabl,
            0xa831c66d2db43210l, 0xb00327c898fb213fl, 0xbf597fc7beef0ee4l, 0xc6e00bf33da88fc2l, 0xd5a79147930aa725l,
            0x06ca6351e003826fl, 0x142929670a0e6e70l, 0x27b70a8546d22ffcl, 0x2e1b21385c26c926l, 0x4d2c6dfc5ac42aedl,
            0x53380d139d95b3dfl, 0x650a73548baf63del, 0x766a0abb3c77b2a8l, 0x81c2c92e47edaee6l, 0x92722c851482353bl,
            0xa2bfe8a14cf10364l, 0xa81a664bbc423001l, 0xc24b8b70d0f89791l, 0xc76c51a30654be30l, 0xd192e819d6ef5218l,
            0xd69906245565a910l, 0xf40e35855771202al, 0x106aa07032bbd1b8l, 0x19a4c116b8d2d0c8l, 0x1e376c085141ab53l,
            0x2748774cdf8eeb99l, 0x34b0bcb5e19b48a8l, 0x391c0cb3c5c95a63l, 0x4ed8aa4ae3418acbl, 0x5b9cca4f7763e373l,
            0x682e6ff3d6b2b8a3l, 0x748f82ee5defb2fcl, 0x78a5636f43172f60l, 0x84c87814a1f0ab72l, 0x8cc702081a6439ecl,
            0x90befffa23631e28l, 0xa4506cebde82bde9l, 0xbef9a3f7b2c67915l, 0xc67178f2e372532bl, 0xca273eceea26619cl,
            0xd186b8c721c0c207l, 0xeada7dd6cde0eb1el, 0xf57d4f7fee6ed178l, 0x06f067aa72176fbal, 0x0a637dc5a2c898a6l,
            0x113f9804bef90dael, 0x1b710b35131c471bl, 0x28db77f523047d84l, 0x32caab7b40c72493l, 0x3c9ebe0a15c9bebcl,
            0x431d67c49c100d4cl, 0x4cc5d4becb3e42b6l, 0x597f299cfc657e2al, 0x5fcb6fab3ad6faecl, 0x6c44198c4a475817l
    };
    private static final long[] state = {
            0x6a09e667f3bcc908l, 0xbb67ae8584caa73bl, 0x3c6ef372fe94f82bl, 0xa54ff53a5f1d36f1l,
            0x510e527fade682d1l, 0x9b05688c2b3e6c1fl, 0x1f83d9abfb41bd6bl, 0x5be0cd19137e2179l
    };
    //working arrays
    private static final long[] Wt = new long[80];
    private static final long[] Ht = new long[8];
    private static final long[] TEMPt = new long[128];
    private static final int[] K = {0x428a2f98, 0x71374491, 0xb5c0fbcf,
            0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74,
            0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 0xe49b69c1, 0xefbe4786,
            0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc,
            0x76f988da, 0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
            0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, 0x27b70a85,
            0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb,
            0x81c2c92e, 0x92722c85, 0xa2bfe8a1, 0xa81a664b, 0xc24b8b70,
            0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3,
            0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 0x748f82ee, 0x78a5636f,
            0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7,
            0xc67178f2
    };
    private static final int[] H0 = {0x6a09e667, 0xbb67ae85, 0x3c6ef372,
            0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19};
    //working arrays
    private static final int[] W = new int[64];
    private static final int[] H = new int[8];
    private static final int[] TEMP = new int[8];

    public static String getRandomString(final int length) {
        final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final Random random = new Random();
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            final int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    private static String byteArrayToHexString(final byte[] b) {
        final StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(final byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        final int d1 = n / 16;
        final int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static String MD5Encode(final String origin, final String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            final MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        } catch (final Exception exception) {
        }
        return resultString.toUpperCase();
    }

    public static String MD5EncodeUtf8(String origin) {
        final Properties props = new Properties();
        ;
        final String key = "password.salt";
        origin = origin + props.getProperty(key.trim(), "");
        return MD5Encode(origin, "utf-8");
    }

    public static String sha512code(final String input) throws UnsupportedEncodingException {
        String output = "";

        final byte[] inbyte = input.getBytes("UTF-8");
        byte[] outbyte;
        outbyte = hasht(inbyte);
        outbyte = sha256X16(outbyte, "UTF-8");
        output = new String(outbyte);


        return output;
    }

    public static byte[] hasht(final byte[] message) {
        //let H=H0
        System.arraycopy(state, 0, Ht, 0, state.length);

        final long[] words = toLongArrayt(padt(message));

        for (int i = 0, n = words.length / 16; i < n; ++i) {
            System.arraycopy(words, i * 16, Wt, 0, 16);
            for (int t = 16; t < Wt.length; ++t) {
                Wt[t] = smallSig1t(Wt[t - 2]) + Wt[t - 7] + smallSig0t(Wt[t - 15]) + Wt[t - 16];
            }
            System.arraycopy(Ht, 0, TEMPt, 0, Ht.length);
            for (int t = 0; t < Wt.length; ++t) {
                final long t1 = TEMPt[7] + bigSig1t(TEMPt[4]) + cht(TEMPt[4], TEMPt[5], TEMPt[6]) + Kt[t] + Wt[t];
                final long t2 = bigSig0t(TEMPt[0]) + majt(TEMPt[0], TEMPt[1], TEMPt[2]);
                System.arraycopy(TEMPt, 0, TEMPt, 1, TEMPt.length - 1);
                TEMPt[4] += t1;
                TEMPt[0] = t1 + t2;
            }
            for (int t = 0; t < Ht.length; ++t) {
                Ht[t] += TEMPt[t];
            }
        }
        return toByteArrayt(Ht);
    }

    public static byte[] padt(final byte[] message) {
        final int blockBits = 1024;
        final int blockBytes = blockBits / 8;

        // new message length: original + 1-bit and padding + 8-byte length
        int newMessageLength = message.length + 1 + 8;
        final int padBytes = blockBytes - (newMessageLength % blockBytes);
        newMessageLength += padBytes;

        // copy message to extended array
        final byte[] paddedMessage = new byte[newMessageLength];
        System.arraycopy(message, 0, paddedMessage, 0, message.length);

        // write 1-bit
        paddedMessage[message.length] = (byte) 0b10000000;

        // write 8-byte integer describing the original message length
        final int lenPos = message.length + 1 + padBytes;
        ByteBuffer.wrap(paddedMessage, lenPos, 8).putLong(message.length * 8);

        return paddedMessage;

    }

    public static long[] toLongArrayt(final byte[] bytes) {
        if (bytes.length % Long.BYTES != 0) {
            throw new IllegalArgumentException("byte array length");
        }

        final ByteBuffer buf = ByteBuffer.wrap(bytes);

        final long[] result = new long[bytes.length / Long.BYTES];
        for (int i = 0; i < result.length; ++i) {
            result[i] = buf.getLong();
        }

        return result;
    }

    public static byte[] toByteArrayt(final long[] ints) {
        final ByteBuffer buf = ByteBuffer.allocate(ints.length * Long.BYTES);
        for (int i = 0; i < ints.length; ++i) {
            buf.putLong(ints[i]);
        }

        return buf.array();
    }

    private static long cht(final long x, final long y, final long z) {
        return (z ^ (x & (y ^ z)));
    }

    private static long majt(final long x, final long y, final long z) {
        return (((x | y) & z) | (x & y));
    }

    private static long bigSig0t(final long x) {
        return Long.rotateRight(x, 28) ^ Long.rotateRight(x, 34) ^ Long.rotateRight(x, 39);
    }

    private static long bigSig1t(final long x) {
        return Long.rotateRight(x, 14) ^ Long.rotateRight(x, 18) ^ Long.rotateRight(x, 41);
    }

    private static long smallSig0t(final long x) {
        return Long.rotateRight(x, 1) ^ Long.rotateRight(x, 8) ^ (x >>> 7);
    }

    private static long smallSig1t(final long x) {
        return Long.rotateRight(x, 19) ^ Long.rotateRight(x, 61) ^ (x >>> 6);
    }

    private static String toHexString(final String str) throws Exception {
        String hexString = "";
        final String[] escapeArray = {"\b", "\t", "\n", "\f", "\r"};
        boolean flag = false;
        for (final String esacapeStr : escapeArray) {
            if (str.contains(esacapeStr)) {
                flag = true;
                break;
            }
        }
        if (flag) throw new Exception("");

        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final StringBuilder sb = new StringBuilder();
        final byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(hexArray[bit]);
            bit = bs[i] & 0x0f;
            sb.append(hexArray[bit]);
        }
        hexString = sb.toString();
        return hexString;
    }

    public static String base64code(final String input) {
        String output = "";
        Long chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        int i = 0;
        final String _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

        while (i < input.length()) {

            chr1 = Long.valueOf(input.charAt(i++));
            if (i < input.length()) {
                chr2 = Long.valueOf(input.charAt(i++));
                if (i < input.length()) {
                    chr3 = Long.valueOf(input.charAt(i++));
                    enc1 = chr1 >> 2;
                    enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                    enc4 = chr3 & 63;
                } else {
                    enc1 = chr1 >> 2;
                    enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                    enc3 = ((chr2 & 15) << 2);
                    enc4 = 64l;
                }
            } else {
                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4);
                enc3 = enc4 = 64l;
            }
            output = output + _keyStr.charAt(Math.toIntExact(enc1)) + _keyStr.charAt(Math.toIntExact(enc2)) + _keyStr.charAt(Math.toIntExact(enc3)) + _keyStr.charAt(Math.toIntExact(enc4));
        }
        return output;
    }

    public static String urlcode(final String input, final boolean encript) {
        String output = "";

        final String[] _StrKey = {"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2B", "2C", "2F", "3A", "3B", "3C", "3D", "3E", "3F", "40", "5B", "5C", "5D", "5E", "60", "7B", "7C", "7D", "7E"};
        final String[] _StrOld = {" ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "+", ",", "/", ":", ";", "<", "=", ">", "?", "@", "[", "\\", "]", "^", "`", "{", "|", "}", "~"};

        int i = 0;
        Boolean bool = false;
        int indexf = 0;
        String Newtemp = "";

        if (encript) {
            //decode
            while (i < input.length()) {

                if (input.charAt(i) == '%') {
                    i++;
                    Newtemp = Newtemp + input.charAt(i);
                    i++;
                    Newtemp = Newtemp + input.charAt(i);
                    int j;
                    for (j = 0; j < _StrKey.length; j++) {
                        if (Newtemp.equals(_StrKey[j])) {
                            bool = true;
                            indexf = j;
                            j = _StrKey.length;
                        }
                    }
                    if (bool) {
                        output = output + _StrOld[indexf];
                        bool = false;
                    } else {
                        output = output + Newtemp;
                    }
                    Newtemp = "";
                    i++;
                } else {
                    output = output + input.charAt(i);
                    i++;
                }
            }
        } else {
            //encode
            while (i < input.length()) {
                int j;
                for (j = 0; j < _StrOld.length; j++) {
                    if (input.charAt(i) == _StrOld[j].charAt(0)) {
                        bool = true;
                        indexf = j;
                        j = _StrOld.length;
                    }
                }
                if (bool) {
                    if (_StrKey[indexf] == "20")
                        output = output + "+";
                    else
                        output = output + "%" + _StrKey[indexf];
                    bool = false;
                } else if (isContainChinese(String.valueOf(input.charAt(i)))) {
                    output = output + convertStringToUTF8(String.valueOf(input.charAt(i)));
                } else if (input.charAt(i) == '¥' || input.charAt(i) == '…' || input.charAt(i) == '～' || input.charAt(i) == '【') {
                    if (input.charAt(i) == '¥')
                        output = output + "%C2%A5";
                    else if (input.charAt(i) == '～')
                        output = output + "%EF%BD%9E";
                    else if (input.charAt(i) == '【')
                        output = output + "%E3%80%90";
                    else
                        output = output + "%E2%80%A6";
                } else if (input.charAt(i) == '】' || input.charAt(i) == '「' || input.charAt(i) == '」' || input.charAt(i) == '《') {
                    if (input.charAt(i) == '】')
                        output = output + "%E3%80%91";
                    else if (input.charAt(i) == '「')
                        output = output + "%E3%80%8C";
                    else if (input.charAt(i) == '」')
                        output = output + "%E3%80%8D";
                    else
                        output = output + "%E3%80%8A";
                } else if (input.charAt(i) == '》' || input.charAt(i) == '，' || input.charAt(i) == '。' || input.charAt(i) == '？' || input.charAt(i) == '、') {
                    if (input.charAt(i) == '》')
                        output = output + "%E3%80%8B";
                    else if (input.charAt(i) == '，')
                        output = output + "%EF%BC%8C";
                    else if (input.charAt(i) == '。')
                        output = output + "%E3%80%82";
                    else if (input.charAt(i) == '？')
                        output = output + "%EF%BC%9F";
                    else
                        output = output + "%E3%80%81";
                } else {
                    output = output + input.charAt(i);
                }
                i++;
            }
        }

        return output;
    }

    public static String convertStringToUTF8(final String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        final StringBuffer sb = new StringBuffer();
        try {
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if (c >= 0 && c <= 255) {
                    sb.append(c);
                } else {
                    final byte[] b;
                    b = Character.toString(c).getBytes(Values.DEFAULT_CHARSET);
                    for (int j = 0; j < b.length; j++) {
                        int k = b[j];
                        k = k < 0 ? k + 256 : k;
                        sb.append("%" + Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static boolean isContainChinese(final String str) {
        final Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        final Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static String sha256code(final String input) throws UnsupportedEncodingException {
        String output = "";

        final byte[] inbyte = input.getBytes(Values.DEFAULT_CHARSET);
        byte[] outbyte;
        outbyte = hash(inbyte);
        outbyte = sha256X16(outbyte, Values.DEFAULT_CHARSET.name());
        output = new String(outbyte);


        return output;
    }

    public static byte[] hash(final byte[] message) {
        //let H=H0
        System.arraycopy(H0, 0, H, 0, H0.length);

        final int[] words = toIntArray(pad(message));

        for (int i = 0, n = words.length / 16; i < n; ++i) {
            System.arraycopy(words, i * 16, W, 0, 16);
            for (int t = 16; t < W.length; ++t) {
                W[t] = smallSig1(W[t - 2]) + W[t - 7] + smallSig0(W[t - 15]) + W[t - 16];
            }
            System.arraycopy(H, 0, TEMP, 0, H.length);
            for (int t = 0; t < W.length; ++t) {
                final int t1 = TEMP[7] + bigSig1(TEMP[4]) + ch(TEMP[4], TEMP[5], TEMP[6]) + K[t] + W[t];
                final int t2 = bigSig0(TEMP[0]) + maj(TEMP[0], TEMP[1], TEMP[2]);
                System.arraycopy(TEMP, 0, TEMP, 1, TEMP.length - 1);
                TEMP[4] += t1;
                TEMP[0] = t1 + t2;
            }
            for (int t = 0; t < H.length; ++t) {
                H[t] += TEMP[t];
            }
        }
        return toByteArray(H);
    }

    public static byte[] pad(final byte[] message) {
        final int blockBits = 512;
        final int blockBytes = blockBits / 8;

        // new message length: original + 1-bit and padding + 8-byte length
        int newMessageLength = message.length + 1 + 8;
        final int padBytes = blockBytes - (newMessageLength % blockBytes);
        newMessageLength += padBytes;

        // copy message to extended array
        final byte[] paddedMessage = new byte[newMessageLength];
        System.arraycopy(message, 0, paddedMessage, 0, message.length);

        // write 1-bit
        paddedMessage[message.length] = (byte) 0b10000000;

        // write 8-byte integer describing the original message length
        final int lenPos = message.length + 1 + padBytes;
        ByteBuffer.wrap(paddedMessage, lenPos, 8).putLong(message.length * 8);

        return paddedMessage;

    }

    public static int[] toIntArray(final byte[] bytes) {
        if (bytes.length % Integer.BYTES != 0) {
            throw new IllegalArgumentException("byte array length");
        }

        final ByteBuffer buf = ByteBuffer.wrap(bytes);

        final int[] result = new int[bytes.length / Integer.BYTES];
        for (int i = 0; i < result.length; ++i) {
            result[i] = buf.getInt();
        }

        return result;
    }

    public static byte[] toByteArray(final int[] ints) {
        final ByteBuffer buf = ByteBuffer.allocate(ints.length * Integer.BYTES);
        for (int i = 0; i < ints.length; ++i) {
            buf.putInt(ints[i]);
        }

        return buf.array();
    }

    private static int ch(final int x, final int y, final int z) {
        return (x & y) | ((~x) & z);
    }

    private static int maj(final int x, final int y, final int z) {
        return (x & y) | (x & z) | (y & z);
    }

    private static int bigSig0(final int x) {
        return Integer.rotateRight(x, 2) ^ Integer.rotateRight(x, 13) ^ Integer.rotateRight(x, 22);
    }

    private static int bigSig1(final int x) {
        return Integer.rotateRight(x, 6) ^ Integer.rotateRight(x, 11) ^ Integer.rotateRight(x, 25);
    }

    private static int smallSig0(final int x) {
        return Integer.rotateRight(x, 7) ^ Integer.rotateRight(x, 18) ^ (x >>> 3);
    }

    private static int smallSig1(final int x) {
        return Integer.rotateRight(x, 17) ^ Integer.rotateRight(x, 19) ^ (x >>> 10);
    }

    public static byte[] sha256X16(final byte[] bytes, final String encoding) {
        final StringBuilder sha256StrBuff = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                sha256StrBuff.append("0").append(
                        Integer.toHexString(0xFF & bytes[i]));
            } else {
                sha256StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        try {
            return sha256StrBuff.toString().getBytes(encoding);
        } catch (final UnsupportedEncodingException e) {
            //logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Test
    public void testmd5(final TestContext context) {
        final Random r = new Random();
        final int len = r.nextInt(10);
        final String input = getRandomString(len);
        final String exp = MD5EncodeUtf8(input);
        //System.out.println(input);
        //System.out.println(exp);
        //System.out.println(Codec.md5(input));
        context.assertEquals(exp, Codec.md5(input));
    }

    @Test
    public void sha256(final TestContext context) throws UnsupportedEncodingException {
        final Random r = new Random();
        final int len = r.nextInt(10);
        final String input = getRandomString(len);
        context.assertEquals(Codec.sha256(input), sha256code(input));
        //System.out.println(input);
        //System.out.println(Codec.sha256(input));
        //System.out.println(sha256code("a"));
    }

    @Test
    public void sha512(final TestContext context) throws Exception {
        //System.out.println(Codec.sha512("a"));
        //System.out.println(sha512code("a"));
        final Random r = new Random();
        final int len = r.nextInt(10);
        final String input = getRandomString(len);
        context.assertEquals(Codec.sha512(input), sha512code(input));

    }

    @Test
    public void testbase64(final TestContext context) {
        final Random r = new Random();
        final int len = r.nextInt(10);
        final String input = getRandomString(len);
        //System.out.println("1: "+Codec.base64(input,true));
        //System.out.println("2: "+base64code(input));
        context.assertEquals(base64code(input), Codec.base64(input, true));
        final String output = Codec.base64(input, true);

        //System.out.println("3: "+base64encode(base64code(input)));
        //System.out.println("input: "+input);
        context.assertEquals(this.base64encode(output), Codec.base64(output, false));
    }

    private String base64encode(final String input) {
        String output = "";
        Integer chr1 = null, chr2 = null, chr3 = null, enc1, enc2, enc3, enc4;
        int i = 0;
        final String _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        final String _ASCIIStr = "*********************************!*#$%&*()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[*]^_*abcdefghijklmnopqrstuvwxyz{|}~*";

        while (i < input.length()) {

            enc1 = _keyStr.indexOf(input.charAt(i++));
            if (i < input.length()) {
                enc2 = _keyStr.indexOf(input.charAt(i++));
                if (i < input.length()) {
                    if (enc2 != 64) {
                        enc3 = _keyStr.indexOf(input.charAt(i++));
                        if (i < input.length()) {
                            if (enc3 != 64) {
                                enc4 = _keyStr.indexOf(input.charAt(i++));
                                if (enc4 != 64) {
                                    chr1 = (enc1 & 63) << 2 | enc2 >> 4;
                                    chr2 = ((enc2 & 15) << 4) | enc3 >> 2;
                                    chr3 = ((enc3 & 3) << 6) | enc4;
                                    output = output + _ASCIIStr.charAt(chr1) + _ASCIIStr.charAt(chr2) + _ASCIIStr.charAt(chr3);
                                } else {
                                    //enc4=64
                                    chr1 = (enc1 & 63) << 2 | enc2 >> 4;
                                    chr2 = ((enc2 & 15) << 4) | enc3 >> 2;
                                    chr3 = ((enc3 & 3) << 6);
                                    if (chr3 != 0) {
                                        output = output + _ASCIIStr.charAt(chr1) + _ASCIIStr.charAt(chr2) + _ASCIIStr.charAt(chr3);
                                    } else {
                                        output = output + _ASCIIStr.charAt(chr1) + _ASCIIStr.charAt(chr2);
                                    }
                                }
                            } else {
                                //enc3=64
                                chr1 = (enc1 & 63) << 2 | enc2 >> 4;
                                chr2 = ((enc2 & 15) << 4);
                                if (chr2 != 0) {
                                    output = output + _ASCIIStr.charAt(chr1) + _ASCIIStr.charAt(chr2);
                                } else {
                                    output = output + _ASCIIStr.charAt(chr1);
                                }
                                i++;


                            }
                        } else {
                            chr1 = (enc1 & 63) << 2 | enc2 >> 4;
                            chr2 = ((enc2 & 15) << 4) | enc3 >> 2;
                            chr3 = ((enc3 & 3) << 6);
                            output = output + _ASCIIStr.charAt(chr1) + _ASCIIStr.charAt(chr2) + _ASCIIStr.charAt(chr3);
                        }
                    } else {
                        //enc2=64
                        chr1 = (enc1 & 63) << 2;
                        if (chr1 != 0) {
                            output = output + _ASCIIStr.charAt(chr1);
                        }
                    }


                } else {
                    chr1 = (enc1 & 63) << 2 | enc2 >> 4;
                    chr2 = ((enc2 & 15) << 4);
                    output = output + _ASCIIStr.charAt(chr1) + _ASCIIStr.charAt(chr2);
                }
            } else {
                chr1 = (enc1 & 63) << 2;
                output = output + _ASCIIStr.charAt(chr1);
            }

            //output = output + _ASCIIStr.charAt(chr1) + _ASCIIStr.charAt(chr2) + _ASCIIStr.charAt(chr3);
        }
        return output;
    }

    @Test
    public void testurl(final TestContext context) {
        final Random r = new Random();
        final String StrNew = "https://www." + getRandomString(r.nextInt(10)).toLowerCase() + ".com/" + getRandomString(r.nextInt(5)).toLowerCase() + "/" + getRandomString(r.nextInt(15)).toLowerCase();
        System.out.println(StrNew);
        final String StrOut = Codec.url(StrNew, true);
        //encode
        context.assertEquals(StrOut, urlcode(StrNew, false));
        //decode
        System.out.println(StrOut);
        context.assertEquals(Codec.url(StrOut, false), urlcode(StrOut, true));
    }


}