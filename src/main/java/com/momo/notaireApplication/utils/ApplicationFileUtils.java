package com.momo.notaireApplication.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ApplicationFileUtils {
    public static File initTempFile(byte[] byteArray) throws IOException {
        File tempFile = File.createTempFile("pdf", "", null);
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(byteArray);
        return tempFile;
    }
}
