/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wijesekara.stores.pos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Samintha Kaveesh
 */
public class Base64Converter {
    JFileChooser fr = new JFileChooser();
    FileSystemView fw = fr.getFileSystemView();
    
    //___________________Base 64 Converter____________________
    private static final boolean IS_CHUNKED = true;

    public void encode_to_base64() {
        try {
            encode("config.dat", fw.getDefaultDirectory() + "\\WPOS\\config.dat", IS_CHUNKED);
        } catch (Exception ec) {

        }

    }

    public void decode_from_base64() {
        try {
            decode(fw.getDefaultDirectory() + "\\WPOS\\config.dat", fw.getDefaultDirectory() + "\\WPOS\\config_temp.dat");
        } catch (Exception ec) {

        }

    }

    /**
     * This method converts the content of a source file into Base64 encoded
     * data and saves that to a target file. If isChunked parameter is set to
     * true, there is a hard wrap of the output encoded text.
     */
    private static void encode(String sourceFile, String targetFile, boolean isChunked) throws Exception {

        byte[] base64EncodedData = Base64.encodeBase64(loadFileAsBytesArray(sourceFile), isChunked);

        writeByteArraysToFile(targetFile, base64EncodedData);
    }

    public static void decode(String sourceFile, String targetFile) throws Exception {

        byte[] decodedBytes = Base64.decodeBase64(loadFileAsBytesArray(sourceFile));

        writeByteArraysToFile(targetFile, decodedBytes);
    }

    /**
     * This method loads a file from file system and returns the byte array of
     * the content.
     */
    public static byte[] loadFileAsBytesArray(String fileName) throws Exception {

        File file = new File(fileName);
        int length = (int) file.length();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();
        return bytes;

    }

    /**
     * This method writes byte array content into a file.
     */
    public static void writeByteArraysToFile(String fileName, byte[] content) throws IOException {

        File file = new File(fileName);
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
        writer.write(content);
        writer.flush();
        writer.close();

    }
    //________________End of Base 64 Converter________________

}
