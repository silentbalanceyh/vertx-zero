package io.vertx.mod.rbac.refine;

import io.horizon.eon.VPath;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ScImage {

    private static final Random RANDOM = new Random();
    // BG /
    private static final Font FONT_DEFAULT = new Font("Times New Roman", Font.PLAIN, 18);
    private static final Font FONT_TEXT = new Font("Times New Roman", Font.BOLD, 28);

    static Future<Buffer> imageGenerate(final String code, final int width, final int height) {
        // Buffered Image
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Generate Image of Graphics
        final Graphics graphics = image.getGraphics();
        graphics.fillRect(0, 0, width, height);
        graphics.setFont(FONT_DEFAULT);
        graphics.setColor(colorRandom(160, 200));
        // Draw the line of handler
        for (int i = 1; i <= 188; i++) {
            drawLine(graphics, width, height);
        }
        drawText(graphics, code);
        // To InputStream
        return Fn.failOr(() -> {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, VPath.SUFFIX.IMG_PNG, baos);
            return Ux.future(Buffer.buffer(baos.toByteArray()));
        });
    }

    private static void drawText(final Graphics graphics, final String code) {
        for (int i = 0; i < code.length(); i++) {
            graphics.setFont(FONT_TEXT);
            graphics.setColor(new Color(RANDOM.nextInt(101), RANDOM.nextInt(111), RANDOM.nextInt(121)));
            graphics.translate(RANDOM.nextInt(3), RANDOM.nextInt(3));
            graphics.drawString(String.valueOf(code.charAt(i)), 24 + 28 * i, 24);
        }
    }

    private static void drawLine(final Graphics graphics, final int width, final int height) {
        final int x = RANDOM.nextInt(width);
        final int y = RANDOM.nextInt(height);
        final int xl = RANDOM.nextInt(13);
        final int yl = RANDOM.nextInt(15);
        graphics.drawLine(x, y, x + xl, y + yl);
    }

    @SuppressWarnings("all")
    private static Color colorRandom(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        final int r = fc + RANDOM.nextInt(bc - fc - 16);
        final int g = fc + RANDOM.nextInt(bc - fc - 14);
        final int b = fc + RANDOM.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }
}
