/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.scouter.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author dfuglsan
 */
public class FileUtils {

    private static final int BUFFER_CAPACITY = 1000;
    private static final int SLEEPTIME = 100;
    private static final int RETRIES = 10;
    /**
     * Describes the separator between the file name and it's extension: '.'
     */
    public static final char EXTENSION_SEPARATOR = '.';
    /**
     * Eight-bit UCS Transformation Format
     */
    public static final String CHARSET_UTF8 = "UTF-8"; //$NON-NLS-1$
    /**
     * Default size of character/string buffers for file content operations.
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    /**
     * Property name for temporary directory for file operations. *
     */
    public static final String DIR_TEMP = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
    /**
     * System-specific line separator character.
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator"); //$NON-NLS-1$

    /**
     * Cannot construct - only static helper methods
     */
    private FileUtils() {
        // cannot construct
    }

    /**
     * Find the file of the given complete name in the given list of search directories. Does not recurse sub-directories. Returns the first
     * file matching <code>fileName</code>.
     * 
     * @param searchDirs the list of directories to search.
     * @param fileName the case-insensitive file name to search for.
     * @return the file found or null if not found.
     */
    public static File findFile(final List<File> searchDirs, final String fileName) {
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(final File directory, final String name) {
                // Check whether this is a file or directory
                File f = new File(directory, name);
                if (f.isDirectory()) {
                    return false;
                }

                // Compare the names
                return fileName.equalsIgnoreCase(name);
            }
        };

        File foundFile = null;
        for (File searchDir : searchDirs) {
            File[] files = searchDir.listFiles(filenameFilter);
            if ((files != null) && (files.length > 0)) {
                // TODO: Returns the first match. Should check all searchDirs
                // for possibility of multiple matches?
                foundFile = files[0];
                break;
            }
        }
        return foundFile;
    }

    /**
     * Slurp the contents of a Reader and return as a string. The Reader should be closed by the caller - it is NOT closed in the method.
     * 
     * @param in The reader
     * @return Contents of Reader or null if Reader is null
     * @throws IOException If an error occurs reading the Reader
     */
    public static String readAsString(final Reader in) throws IOException {
        if (in == null) {
            return null;
        }
        StringBuilder fileData = new StringBuilder(BUFFER_CAPACITY);
        BufferedReader reader = new BufferedReader(in);
        char[] buf = new char[DEFAULT_BUFFER_SIZE];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[DEFAULT_BUFFER_SIZE];
        }
        return fileData.toString();
    }

    /**
     * Write the contents of the specified system resource to the designated file. If the file already exists it will be replaced the the
     * contents of the resource.
     * 
     * @param resourceName name of resource to be written to file
     * @param f file to be write to
     * @throws IOException if an error occurs writing to file
     */
    public static void writeResourceToFile(final String resourceName, final File f) throws IOException {
        InputStream is = null;
        FileOutputStream fio = null;
        BufferedOutputStream bos = null;
        try {
            is = FileUtils.class.getClassLoader().getResourceAsStream(resourceName);

            if (f.exists()) {
                delete(f);
            }
            if (is == null) {
                throw new IOException("Resource '" + resourceName + "' not found.");
            }

            fio = new FileOutputStream(f);
            bos = new BufferedOutputStream(fio);
            byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead;

            while (-1 != (bytesRead = is.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();

        } finally {
            close(is);
            close(bos);
        }
    }

    /**
     * Read in file contents as a string.
     * 
     * @param file File to be read
     * @return file contents as a string
     * @throws IOException if an error occurs reading file
     */
    public static String readFileAsString(final File file) throws IOException {

        char[] cbuf = new char[DEFAULT_BUFFER_SIZE];
        StringBuilder sb = new StringBuilder();
        int len = 0;
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, CHARSET_UTF8);

        while (len != -1) {
            len = isr.read(cbuf, 0, DEFAULT_BUFFER_SIZE);
            if (len > 0) {
                sb.append(cbuf, 0, len);
            }
        }
        close(isr);
        close(fis);
        return sb.toString();
    }

    /**
     * Write the content to the file using UTF-8 encoding
     * 
     * @param file the file
     * @param content the content
     * @throws IOException If an error occurs writing the content
     */
    public static void writeFile(final File file, final String content) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, CHARSET_UTF8);
        try {
            osw.write(content);
        } finally {
            close(osw);
            close(fos);
        }
    }

    /**
     * Write the content to the file.
     * 
     * @param file the file
     * @param content the content
     * @throws IOException If an error occurs writing the content
     */
    public static void writeFile(final File file, final byte[] content) throws IOException {

        // Write the results out to the specified file
        // System.out.println("Temp file at " + file.getAbsolutePath());
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(content);
            bos.flush();
        } finally {
            close(bos);
            close(fos);
        }
    }

    /**
     * Finds subdirectories within a given directory. All directories found are filtered by an FileFilter.
     * 
     * @param directory The starting directory to search in
     * @param dirFilter Filter to apply when finding subdirectories; may not be null
     * @return an list of java.io.File with the matching files
     */
    public static List<File> listDirs(final File directory, final FileFilter dirFilter) {
        List<File> result = new ArrayList<>();
        if (directory == null) {
            return result;
        }
        if (!directory.isDirectory()) {
            throw new RuntimeException("The input directory argument '" + directory.getName() + "' is not a directory.");
        }
        if (dirFilter == null) {
            throw new RuntimeException("The input filter argument cannot be null.");
        }

        innerListDirs(result, directory, dirFilter);

        return result;
    }

    private static void innerListDirs(final List<File> files, final File dir, final FileFilter dirFilter) {
        for (File f : dir.listFiles(dirFilter)) {
            if (f.isDirectory() && dirFilter.accept(f)) {
                files.add(f);
                innerListDirs(files, f, dirFilter);
            }
        }
    }

    /**
     * Finds files within a given directory (and optionally its subdirectories). All files found are filtered by an FileFilter.
     * <p>
     * If your search should recurse into subdirectories you must pass in an FileFilter for directories.
     * </p>
     * 
     * @param directory The directory to search in
     * @param fileFilter Filter to apply when finding files; may not be null
     * @param dirFilter Optional filter to apply when finding subdirectories. If this parameter is <code>null</code>, subdirectories will
     *            not be included in the search.
     * @return an list of java.io.File with the matching files
     */
    public static List<File> listFiles(final File directory, final FileFilter fileFilter, final FileFilter dirFilter) {
        List<File> result = new ArrayList<File>();
        if (directory == null) {
            return result;
        }
        if (!directory.isDirectory()) {
            throw new RuntimeException("The input directory argument '" + directory.getName() + "' is not a directory.");
        }
        if (fileFilter == null) {
            throw new RuntimeException("The input filter argument cannot be null.");
        }

        innerListFiles(result, directory, fileFilter, dirFilter);

        return result;
    }

    private static void innerListFiles(final List<File> files, final File dir, final FileFilter fileFilter, final FileFilter dirFilter) {
        for (File f : dir.listFiles(dirFilter)) {
            if (f.isDirectory() && (dirFilter != null)) {
                innerListFiles(files, f, fileFilter, dirFilter);

            } else if (f.isFile() && ((fileFilter == null) || fileFilter.accept(f))) {
                files.add(f);
            }
        }
    }

    /**
     * Get all subdirectories under a directory
     * 
     * @param dir Directory
     * @return Array of subdirectory Files at dir or empty if not a dir, doesn't exist, etc
     * @throws IOException If an error occurs while processing files
     */
    public static File[] getSubdirectories(final File dir) throws IOException {
        if ((dir == null) || !dir.exists() || dir.isFile()) {
            return new File[0];
        }

        return dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return file.isDirectory();
            }
        });
    }

    /**
     * Recursively delete all files and subdirectories under a directory.
     * 
     * @param dir Starting directory
     * @throws IOException If an error occurs while processing files
     */
    public static void deleteRecursive(final File dir) throws IOException {
        if ((dir == null) || !dir.exists() || dir.isFile()) {
            return;
        }

        // Delete all subdirectories recursively
        File[] subdirs = FileUtils.getSubdirectories(dir);
        for (File subdir : subdirs) {
            deleteRecursive(subdir);
        }

        // Delete all files
        File[] files = dir.listFiles();
        for (File file : files) {
            delete(file);
        }

        // Delete self
        delete(dir);
    }

    /**
     * Creates a temp directory below the java.io.tmpdir directory.
     * 
     * @param dirPath relative path of temp directory
     * @return File representing new dir
     * @throws IOException if an error occurs creating temporary directory
     */
    public static File createTempDir(final String dirPath) throws IOException {
        File tmp = new File(DIR_TEMP, dirPath);
        mkdirs(tmp);
        return tmp;
    }

    /**
     * Creates a temp directory below the java.io.tmpdir directory. The method creates a uniquely named directory using a combination of
     * current time and a character offset.
     * 
     * @return File representing new directory
     * @throws IOException of an error occurs creating temporary directory
     */
    public static File createTempDir() throws IOException {
        // Build a file with a unique name using current time and an character offset
        Character offset = 'a';
        String name = "" + offset + System.currentTimeMillis(); //$NON-NLS-1$
        File tmp = new File(DIR_TEMP + File.separator + name);
        int count = 0;
        while (tmp.exists() && (count < RETRIES)) {
            try {
                Thread.sleep(SLEEPTIME);
            } catch (final InterruptedException e) {
                // ignore
            }
            if (!Character.isLetter(offset)) {
                offset = 'a';
            } else {
                offset++;
            }
            name = "" + offset + System.currentTimeMillis(); //$NON-NLS-1$
            tmp = new File(DIR_TEMP + File.separator + name);
            count++;
        }
        mkdirs(tmp);
        return tmp;
    }

    /**
     * Close the specified stream, ignoring any errors. This is useful to call in finally blocks.
     * 
     * @param stream The stream to close
     */
    public static void close(final Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    /**
     * Delete the specified file.
     * 
     * @param file file to be deleted
     * @throws IOException if the file can't be created
     */
    public static void delete(final File file) throws IOException {
        boolean success = file.delete();
        if (!success) {
            if (!file.exists()) {
                return; // the file doesn't even exist, of course delete will
                // fail
            }
            if (!file.canRead()) {
                return; // the file isn't readable, delete will fail
            }
            if (!file.canWrite()) {
                return; // the file isn't writable, delete will fail
            }
            throw new RuntimeException("Cannot delete '" + file.getAbsolutePath() + "'");
        }
    }

    /**
     * Recursively delete all files and subdirectories under the temp directory, if the directory exists.
     * 
     * @param dirName Starting directory, Directory must be under directory specified by DIR_TEMP
     * @throws IOException If an error occurs while processing files
     */
    public static void deleteTempDir(final String dirName) throws IOException {
        File dir = new File(DIR_TEMP, dirName);
        deleteRecursive(dir);
    }

    /**
     * Create the specified directory and its parents
     * 
     * @param file directory to be created
     * @throws IOException if the directory can't be created
     */
    public static void mkdirs(final File file) throws IOException {
        boolean success = file.mkdirs();
        if (!success) {
            throw new RuntimeException("Cannot make directory for '" + file.getAbsolutePath() + "'");
        }
    }

    /**
     * Create the specified file
     * 
     * @param file file to be created.
     * @throws IOException if the file can't be created
     */
    public static void createNewFile(final File file) throws IOException {
        boolean success = file.createNewFile();
        if (!success) {
            throw new RuntimeException("Cannot write file '" + file.getAbsolutePath() + "'");
        }
    }
}
