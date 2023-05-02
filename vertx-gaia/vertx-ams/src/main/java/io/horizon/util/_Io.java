package io.horizon.util;

import io.horizon.eon.VString;
import io.horizon.eon.em.Environment;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author lang : 2023/4/28
 */
class _Io extends _Inverse {
    protected _Io() {
    }

    /**
     * 文件夹和文件拼接，去掉多余字符的版本，如多个 // 或 \\
     *
     * @param folder 文件夹
     * @param file   文件
     *
     * @return 拼接后的路径
     */
    public static String ioPath(final String folder, final String file) {
        return IoPath.resolve(folder, file);
    }

    /**
     * （开发专用）在不同环境中进行切换
     * <pre><code>
     * - 开发环境：Development       -> src/main/resources
     * - 生产环境：Production        -> /
     * - 测试环境：Mockito           -> src/test/resources
     * </code></pre>
     *
     * @param path        路径
     * @param environment 环境
     *
     * @return 路径
     */
    public static String ioPath(final String path, final Environment environment) {
        return IoPath.resolve(path, environment);
    }

    /**
     * 提取文件绝对路径
     *
     * @param filename 文件名
     *
     * @return 绝对路径
     */
    public static String ioPath(final String filename) {
        return Io.ioPath(filename);
    }

    /**
     * 按文件路径读取某个文件目录中的内容，并返回排序过的路径集合
     *
     * @param path 文件路径
     *
     * @return 排序过的路径集合
     */
    public static List<String> ioPathSet(final String path) {
        return IoPath.ladder(path);
    }

    /**
     * 返回当前目录下的根路径
     *
     * @param path 路径
     *
     * @return 根路径
     */
    public static String ioPathRoot(final String path) {
        return IoPath.first(path, VString.SLASH);
    }

    /**
     * 返回当前目录下的根路径（跨操作系统平台）
     *
     * @param path      路径
     * @param separator 分隔符
     *
     * @return 根路径
     */
    public static String ioPathRoot(final String path, final String separator) {
        return IoPath.first(path, separator);
    }

    /**
     * 返回当前目录下的最后一个路径
     *
     * @param path 路径
     *
     * @return 最后一个路径
     */
    public static String ioPathLeaf(final String path) {
        return IoPath.last(path, VString.SLASH);
    }

    /**
     * 返回当前目录下的最后一个路径（跨操作系统平台）
     *
     * @param path      路径
     * @param separator 分隔符
     *
     * @return 最后一个路径
     */
    public static String ioPathLeaf(final String path, final String separator) {
        return IoPath.last(path, separator);
    }

    /**
     * 读取当前目录（一级）下的文件（可以读jar压缩包内部）
     *
     * @param folder 目录
     *
     * @return 文件集合
     */
    public static List<String> ioFiles(final String folder) {
        return IoDirectory.listFiles(folder, null);
    }

    /**
     * 读取当前目录（一级）下的满足扩展名条件的文件（可读jar压缩包内部）
     *
     * @param folder    目录
     * @param extension 扩展名
     *
     * @return 文件集合
     */
    public static List<String> ioFiles(final String folder, final String extension) {
        return IoDirectory.listFiles(folder, extension);
    }

    /**
     * 递归读取当前目录（N级）下的满所有文件（可读jar压缩包内部）
     *
     * @param folder 目录
     *
     * @return 文件集合
     */
    public static List<String> ioFilesN(final String folder) {
        return IoDirectory.listFilesN(folder, null, null);
    }

    /**
     * 递归读取当前目录（N级）下的满足扩展名的文件（可读jar压缩包内部）
     *
     * @param folder    目录
     * @param extension 扩展名
     *
     * @return 文件集合
     */
    public static List<String> ioFilesN(final String folder, final String extension) {
        return IoDirectory.listFilesN(folder, extension, null);
    }

    /**
     * 递归读取当前目录（N级）下的满足扩展名和前缀的文件（可读jar压缩包内部）
     *
     * @param folder    目录
     * @param extension 扩展名
     * @param prefix    前缀
     *
     * @return 文件集合
     */
    public static List<String> ioFilesN(final String folder, final String extension, final String prefix) {
        return IoDirectory.listFilesN(folder, extension, prefix);
    }

    /**
     * 读取当前目录（一级）下的文件夹（可读jar压缩包内部）
     *
     * @param folder 目录
     *
     * @return 文件夹集合
     */
    public static List<String> ioDirectories(final String folder) {
        return IoDirectory.listDirectories(folder);
    }

    /**
     * 递归读取当前目录（N级）下的文件夹（可读jar压缩包内部）
     *
     * @param folder 目录
     *
     * @return 文件夹集合
     */
    public static List<String> ioDirectoriesN(final String folder) {
        return IoDirectory.listDirectoriesN(folder);
    }

    /**
     * 直接使用文件对象读取文件流
     *
     * @param file 文件对象
     *
     * @return 文件流
     */
    public static InputStream ioStream(final File file) {
        return IoStream.readDirect(file);
    }

    /**
     * 根据文件路径和传入的类读取文件流数据
     *
     * @param filename 文件路径
     * @param clazz    类
     *
     * @return 文件流
     */
    public static InputStream ioStream(final String filename, final Class<?> clazz) {
        return IoStream.read(filename, clazz);
    }

    /**
     * 根据文件路径读取文件流数据
     *
     * @param filename 文件路径
     *
     * @return 文件流
     */
    public static InputStream ioStream(final String filename) {
        return IoStream.read(filename, IoStream.class);
    }

    /**
     * 读取传入文件（YAML文件格式），读取成想要的JsonObject/JsonArray对象
     *
     * @param filename 文件路径
     * @param <T>      泛型
     *
     * @return 文件对象
     */
    public static <T> T ioYaml(final String filename) {
        return Io.ioYaml(filename);
    }

    /**
     * 读取文件路径中，读取成File对象
     *
     * @param filename 文件路径
     *
     * @return 文件对象
     */
    public static File ioFile(final String filename) {
        return Io.ioFile(filename);
    }

    /**
     * 检查文件路径中的文件是否存在
     *
     * @param filename 文件路径
     *
     * @return 是否存在
     */
    public static boolean ioExist(final String filename) {
        return Io.isExist(filename);
    }

    /**
     * 读取文件路径中的文件，读取成属性文件对象
     *
     * @param filename 文件路径
     *
     * @return 属性文件对象
     */
    public static Properties ioProperties(final String filename) {
        return Io.ioProp(filename);
    }

    /**
     * 读取文件路径中的文件，读取成JsonArray对象
     *
     * @param filename 文件路径
     *
     * @return JsonArray对象
     */
    public static JsonArray ioJArray(final String filename) {
        return _Value.valueJArray(Io.ioJArray(filename));
    }

    /**
     * 读取文件路径中的文件，读取成JsonObject对象
     *
     * @param filename 文件路径
     *
     * @return JsonObject对象
     */
    public static JsonObject ioJObject(final String filename) {
        return _Value.valueJObject(Io.ioJObject(filename));
    }

    /**
     * 读取文件流中的数据，读取成String对象
     *
     * @param in 文件流
     *
     * @return String对象
     */
    public static String ioString(final InputStream in) {
        return Io.ioString(in, null);
    }

    /**
     * 读取文件流中的数据，按joined分隔符连接，读取成String对象
     *
     * @param in     文件流
     * @param joined 分隔符
     *
     * @return String对象
     */
    public static String ioString(final InputStream in, final String joined) {
        return Io.ioString(in, joined);
    }

    /**
     * 读取文件名路径中的文件，读取成String对象
     *
     * @param filename 文件路径
     *
     * @return String对象
     */
    public static String ioString(final String filename) {
        return Io.ioString(filename, null);
    }

    /**
     * 读取文件名路径中的文件，按joined分隔符连接，读取成String对象
     *
     * @param filename 文件路径
     * @param joined   分隔符
     *
     * @return String对象
     */
    public static String ioString(final String filename, final String joined) {
        return Io.ioString(filename, joined);
    }

    /**
     * 读取文件名路径中的文件，读取成String对象
     *
     * @param filename 文件路径
     *
     * @return String对象
     */
    public static Buffer ioBuffer(final String filename) {
        return Io.ioBuffer(filename);
    }

    /**
     * 读取压缩文件中的数据
     *
     * @param filename 文件路径
     *
     * @return 压缩文件流
     */
    public static String ioCompress(final String filename) {
        return Io.ioCompress(filename);
    }

    /**
     * 创建目录
     *
     * @param file 目录路径
     *
     * @return 是否创建成功
     */
    public static boolean ioOut(final String file) {
        return IoOut.make(file);
    }

    /**
     * 将数据写入到文件 file 中，数据类型为 String
     *
     * @param file 文件路径
     * @param data 数据
     */
    public static void ioOut(final String file, final String data) {
        IoOut.write(file, data);
    }

    /**
     * 将数据写入到文件 file 中，数据类型为 JsonObject
     *
     * @param file 文件路径
     * @param data 数据
     */
    public static void ioOut(final String file, final JsonObject data) {
        IoOut.write(file, data);
    }

    /**
     * 将数据写入到文件 file 中，数据类型为 JsonArray
     *
     * @param file 文件路径
     * @param data 数据
     */
    public static void ioOut(final String file, final JsonArray data) {
        IoOut.write(file, data);
    }

    /**
     * 将数据写入到文件 file 中，数据类型为 String
     *
     * @param file 文件路径
     * @param data 数据
     */
    public static void ioOutCompress(final String file, final String data) {
        IoOut.writeCompress(file, data);
    }

    /**
     * 将数据写入到文件 file 中，数据类型为 JsonObject
     *
     * @param file 文件路径
     * @param data 数据
     */
    public static void ioOutCompress(final String file, final JsonArray data) {
        IoOut.writeCompress(file, data);
    }

    /**
     * 将数据写入到文件 file 中，数据类型为 JsonArray
     *
     * @param file 文件路径
     * @param data 数据
     */
    public static void ioOutCompress(final String file, final JsonObject data) {
        IoOut.writeCompress(file, data);
    }

    /**
     * 将数据文件中的数据写入到输出流，拷贝时专用
     *
     * @param file   文件路径
     * @param output 输出流
     */
    public static void ioOut(final String file, final OutputStream output) {
        IoOut.writeBig(file, output);
    }
}
