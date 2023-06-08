package io.horizon.util;

import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.fn.HFn;

import java.io.File;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

final class IoDirectory {
    private static final LogUtil LOG = LogUtil.from(IoDirectory.class);

    private IoDirectory() {
    }

    static List<String> listFiles(final String folder, final String extension) {
        return HFn.runOr(new ArrayList<>(), () -> list(folder, extension, false), folder);
    }

    static List<String> listDirectories(final String folder) {
        return HFn.runOr(new ArrayList<>(), () -> list(folder, null, true), folder);
    }

    static List<String> listFilesN(final String folder, final String extension, final String prefix) {
        final List<String> folders = listDirectoriesN(folder);
        return folders.stream()
            .flatMap(single -> list(single, extension, false)
                .stream()
                .filter(file -> TIs.isNil(prefix) || file.contains(prefix))
                .map(file -> {
                    if (single.endsWith("/")) {
                        return single + file;
                    } else {
                        return single + "/" + file;
                    }
                }))
            .filter(file -> {
                final InputStream in = IoStream.readDirect(file);
                return Objects.nonNull(in);
            })
            .collect(Collectors.toList());
    }

    static List<String> listDirectoriesN(final String folder) {
        final String root = IoPath.root();
        LOG.info("List directories: root = {0}, folder = {1}", root, folder);
        return HFn.runOr(new ArrayList<>(), () -> listDirectoriesN(folder, root), folder);
    }

    private static List<String> listDirectoriesN(final String folder, final String root) {
        final List<String> folders = new ArrayList<>();
        // root + folder
        File folderObj = new File(IoPath.resolve(root, folder));
        if (!folderObj.exists()) {
            final URL url = Io.ioURL(folder);
            if (Objects.nonNull(url)) {
                // Url Processing to File
                folderObj = new File(url.getPath());
            } else {
                LOG.warn("URL is null, please check your path here.");
                folderObj = null;
            }
        }
        if (Objects.nonNull(folderObj) && folderObj.isDirectory()) {
            folders.add(folder);
            // Else
            final String[] folderList = folderObj.list();
            assert folderList != null;
            final String relatedPath = folderObj.getAbsolutePath().replace("\\", "/");
            Arrays.stream(folderList).forEach(folderS -> {
                final String rootCopy = root.replace("\\", "/");
                final String pathReplaced = relatedPath.replace(rootCopy, VString.EMPTY);
                folders.addAll(listDirectoriesN(pathReplaced + "/" + folderS, root));
            });
            LOG.info("Directories found: size = {0}", String.valueOf(folders.size()));
        }
        return folders;
    }

    private static List<String> list(final String folder,
                                     final String extension,
                                     final boolean isDirectory) {
        /*
         * list folder by related file first
         * /folder here
         */
        final File folderObj = new File(folder);
        final Set<String> retSet = new TreeSet<>();
        if (folderObj.exists()) {
            /*
             * Related path here, it means that
             * such as /folder/extra/ etc.
             */
            retSet.addAll(getFiles(folderObj, extension, isDirectory));
        } else {
            URL url = Io.ioURL(folder);
            if (Objects.isNull(url)) {
                /*
                 * Fix jar path issue here.
                 */
                if (folder.contains(VPath.SUFFIX.JAR_DIVIDER)) {
                    url = HFn.failOr(() -> new URL(folder), folder);
                }
            }
            /*
             * Split steps for url extraction
             */
            if (Objects.isNull(url)) {
                LOG.info("The url of folder = `{0}` is null", folder);
            } else {
                /*
                 * Whether it's jar path or common path.
                 */
                final String protocol = url.getProtocol();
                if (VPath.PROTOCOL.FILE.equals(protocol)) {
                    /*
                     * Common file
                     */
                    retSet.addAll(getFiles(url, extension, isDirectory));
                } else if (VPath.PROTOCOL.JAR.equals(protocol)) {
                    /*
                     * Jar File
                     */
                    retSet.addAll(getJars(url, extension, isDirectory));
                } else {
                    LOG.error("protocol error! protocol = {0}, url = {1}", protocol, url);
                }
            }
        }
        return new ArrayList<>(retSet);
    }

    static List<String> getJars(final URL url, final String extension, final boolean isDirectory) {
        return HFn.failOr(() -> {
            final JarURLConnection jarCon = (JarURLConnection) url.openConnection();
            final JarFile jarFile = jarCon.getJarFile();
            /*
             * JarEntry iterator
             */
            final String[] specifics = url.getPath().split("!/");
            final String folder = specifics[1];     // Jar Specification
            if (isDirectory) {
                /*
                 * Get folder entry
                 */
                return getJarDirectories(jarFile, folder);
            } else {
                /*
                 * Get file entry
                 */
                return getJarFiles(jarFile, folder, extension);
            }
        }, url);
    }

    private static List<String> getJarDirectories(final JarFile jarFile, final String folder) {
        final List<String> retList = new ArrayList<>();
        final Enumeration<JarEntry> entities = jarFile.entries();
        while (entities.hasMoreElements()) {
            final JarEntry entry = entities.nextElement();
            /*
             * First, we must filter the folder
             */
            final String entryName = entry.getName();
            final String filtered = folder + "/";
            if (entryName.startsWith(folder) && entry.isDirectory()
                && !filtered.equals(entryName)  // This condition is for folder only
            ) {
                /*
                 * The condition is ok to pickup folder only
                 * Because folder is end with '/', it means that you must replace the last folder
                 */
                final String replaced = entryName.substring(0, entryName.lastIndexOf('/'));
                final String found = replaced.substring(replaced.lastIndexOf('/') + 1);
                if (!TIs.isNil(found)) {
                    retList.add(found);
                }
            }
        }
        return retList;
    }

    private static List<String> getJarFiles(final JarFile jarFile, final String folder, final String extension) {
        final List<String> retList = new ArrayList<>();
        final Enumeration<JarEntry> entities = jarFile.entries();
        while (entities.hasMoreElements()) {
            final JarEntry entry = entities.nextElement();
            /*
             * First, we must filter the folder
             */
            final String entryName = entry.getName();
            if (entryName.startsWith(folder) && !entry.isDirectory()) {
                if (Objects.isNull(extension)) {
                    /*
                     * No Extension
                     */
                    final String foundFile = entryName.substring(entryName.lastIndexOf('/') + 1);
                    if (!TIs.isNil(foundFile)) {
                        retList.add(foundFile);
                    }
                } else {
                    if (entryName.endsWith(extension)) {
                        /*
                         * Extension enabled
                         */
                        final String foundFile = entryName.substring(entryName.lastIndexOf('/') + 1);
                        if (!TIs.isNil(foundFile)) {
                            retList.add(foundFile);
                        }
                    }
                }
            }
        }
        return retList;
    }

    private static List<String> getFiles(final File directory, final String extension, final boolean isDirectory) {
        final List<String> retList = new ArrayList<>();
        if (directory.isDirectory() && directory.exists()) {
            final File[] files = (isDirectory) ?
                directory.listFiles(File::isDirectory) :
                (null == extension ?
                    directory.listFiles(File::isFile) :
                    directory.listFiles((item) -> item.isFile() && item.getName().endsWith(extension)));
            if (null != files) {
                retList.addAll(Arrays.stream(files)
                    .map(File::getName)
                    .collect(Collectors.toList()));
            }
        } else {
            LOG.error("The file doest not exist, file = `{0}`", directory.getAbsolutePath());
        }
        return retList;
    }

    private static List<String> getFiles(final URL url, final String extension, final boolean isDirectory) {
        final File directory = new File(url.getFile());
        return getFiles(directory, extension, isDirectory);
    }
}
