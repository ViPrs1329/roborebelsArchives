/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.scouter.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author dfuglsan
 */
public class StreamUtils {

    private static final int DEFAULT_READING_SIZE = 8192;
    /**
     * Eight-bit UCS Transformation Format
     */
    private static final String CHARSET_UTF8 = "UTF-8"; //$NON-NLS-1$

    // ==================================================================================
    //              C O N S T R U C T O R S
    // ==================================================================================
    private StreamUtils() {
        // do not allow an instance to be created
    }

    // ==================================================================================
    //               P U B L I C   M E T H O D S
    // ==================================================================================
    /**
     * Create an InputStream for the given byte array.
     * 
     * @param data The byte array
     * @return the resultant input stream
     * @throws IllegalArgumentException If there are problems reading the byte array
     */
    public static InputStream convertToInputStream(final byte[] data) throws IllegalArgumentException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        return new BufferedInputStream(bais);
    }

    /**
     * Create an InputStream for the given string.
     * 
     * @param data The string
     * @return the resultant input stream
     * @throws UnsupportedEncodingException If there are problems reading the string
     */
    public static InputStream convertToInputStream(final String data) throws UnsupportedEncodingException {
        return convertToInputStream(data.getBytes(CHARSET_UTF8));
    }

    /**
     * Create an InputStream for the given file.
     * 
     * @param file The file
     * @return the resultant input stream
     * @throws InternalFailureException If there are problems reading the file
     */
    public static InputStream convertToInputStream(final File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        InputStream isContent = new BufferedInputStream(fis);
        return isContent;
    }

    /**
     * Convert the given input stream into a byte array. It is the responsibility of the calling routine to close the stream.
     * 
     * @see oracle.oer.common.util.stream.StreamUtils#closeQuietly(Closeable)
     * @param stream The input stream
     * @return the resultant byte array
     * @throws IOException If there are problems reading the stream
     */
    public static byte[] convertToByteArray(final InputStream stream) throws IOException {
        byte[] contents = new byte[0];
        int contentsLength = 0;
        int amountRead = -1;

        do {
            int amountRequested = Math.max(stream.available(), DEFAULT_READING_SIZE); // read
            // at
            // least
            // 8K

            // Resize contents if needed
            if ((contentsLength + amountRequested) > contents.length) {
                byte[] newContents = new byte[contentsLength + amountRequested];
                System.arraycopy(contents, 0, newContents, 0, contentsLength);
                contents = newContents;
            }

            // Read as many bytes as possible
            amountRead = stream.read(contents, contentsLength, amountRequested);

            if (amountRead > 0) {
                // Remember length of contents
                contentsLength += amountRead;
            }
        } while (amountRead != -1);

        // Resize contents if necessary
        if (contentsLength < contents.length) {
            byte[] newContents = new byte[contentsLength];
            System.arraycopy(contents, 0, newContents, 0, contentsLength);
            contents = newContents;
        }

        return contents;
    }

    /**
     * Convert the given input stream into a string. It is the responsibility of the calling routine to close the stream.
     * 
     * @see oracle.oer.common.util.stream.StreamUtils#closeQuietly(Closeable)
     * @param stream The input stream
     * @return the resultant String
     * @throws IOException If there are problems reading the stream
     * @throws UnsupportedEncodingException If there are problems reading the string
     */
    public static String convertToString(final InputStream stream) throws IOException, UnsupportedEncodingException {
        return new String(StreamUtils.convertToByteArray(stream), CHARSET_UTF8);
    }

    /**
     * Create a temporary java.io.File from a resource with the specified name
     * @param resourceName The resource name
     * @return the temporary file
     * @throws IOException
     */
    public static File createTempFileFromResource(final Class<?> clazz, final String resourceName) throws IOException {
        InputStream istream = clazz.getResourceAsStream("defaultImage.png");
        if (istream == null) {
            throw new IOException("A resource with the name '"+resourceName+"' cannot be found");
        }
        File tempFile = Files.createTempFile("tmp_"+resourceName,"").toFile();
        tempFile.deleteOnExit();
        try (FileOutputStream ostream = new FileOutputStream(tempFile)) {
            StreamUtils.pipe(istream, ostream);
        }
        return tempFile;
    }
    
    /**
     * Close the given stream without throwing exceptions
     * 
     * @param stream The stream to close; may be null
     */
    public static void closeQuietly(final Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(StreamUtils.class.getName()).log(Level.WARNING, null, ex);
            }
        }
    }

    /**
     * Write the full contents of an InputStream to an OutputStream
     * 
     * @param is the input
     * @param os the output
     * @return the number of bytes written
     * @throws IOException if something bad happens
     */
    public static int pipe(final InputStream is, final OutputStream os) throws IOException {
        int nread = 0;
        int totalNRead = 0;
        byte[] buffer = new byte[DEFAULT_READING_SIZE];

        while (true) {
            nread = is.read(buffer);
            if (nread <= 0) {
                break;
            }
            totalNRead += nread;
            os.write(buffer, 0, nread);
        }
        os.flush();
        return totalNRead;
    }
}
