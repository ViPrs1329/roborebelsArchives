/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.scouter.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

/**
 *
 */
public class XmlUtils {
    /**
     * Eight-bit UCS Transformation Format
     */
    public static final String CHARSET_UTF8 = "UTF-8"; //$NON-NLS-1$
    /**
     * Default byte encoding used when creating JDOM documents from a String
     */
    public final static String DEFAULT_CHARSET_NAME = CHARSET_UTF8;

    public static Document createDocument(final File xmlFile) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        return builder.build(xmlFile);
    }

    public static Document createDocument(final String xmlFrag) throws SAXException, IOException {
        SAXBuilder builder = new SAXBuilder();
        InputStream is = null;
        try {
            is = StreamUtils.convertToInputStream(xmlFrag);
            return builder.build(is);
        } catch (JDOMException ex) {
            Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            StreamUtils.closeQuietly(is);
        }
        return null;
    }

    public static Document createDocument(final String xmlFrag, final String charsetName) throws IOException, JDOMException {

        final SAXBuilder builder = new SAXBuilder();
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(xmlFrag.getBytes(charsetName));
            return builder.build(is);
        } finally {
            StreamUtils.closeQuietly(is);
        }
    }

    public static Document createNewDocument(final String rootElementName) {
        return new Document(new Element(rootElementName));
    }

    public static String toFormattedString(final String xml) throws IOException, JDOMException {
        if ((xml != null) && (xml.length() > 0)) {
            // Use a Format object that performs whitespace beautification with 2-space 
            // indents, uses the UTF-8 encoding, doesn't expand empty elements, includes 
            // the declaration and encoding, and uses the default entity escape strategy.
            final XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            return out.outputString(createDocument(xml, DEFAULT_CHARSET_NAME));
        }
        return ""; //$NON-NLS-1$
    }

    public static String toFormattedString(final Document doc) throws IOException, JDOMException {
        if (doc != null) {
            // Use a Format object that performs whitespace beautification with 2-space 
            // indents, uses the UTF-8 encoding, doesn't expand empty elements, includes 
            // the declaration and encoding, and uses the default entity escape strategy.
            final XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            return out.outputString(doc);
        }
        return ""; //$NON-NLS-1$
    }

    public static String toString(final Document doc) {
        return toString(doc, DEFAULT_CHARSET_NAME);
    }

    public static String toString(final Document doc, final String charsetName) {
        // Use a Format object that performs no whitespace changes, uses 
        // the UTF-8 encoding, doesn't expand empty elements, includes the 
        // declaration and encoding, and uses the default entity escape strategy.
        Format format = Format.getRawFormat();
        format.setEncoding(charsetName);

        final XMLOutputter out = new XMLOutputter(format);
        return ((doc != null) ? out.outputString(doc) : ""); //$NON-NLS-1$
    }

    public static void prettyPrint(final Document doc) throws IOException {
        XMLOutputter fmt = new XMLOutputter();
        fmt.output(doc, System.out);
    }

}
