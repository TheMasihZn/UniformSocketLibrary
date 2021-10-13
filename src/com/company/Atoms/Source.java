package com.company.Atoms;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Source {
    private final File file;

    public Source(String fileAddress) {
        this.file = new File(fileAddress);
    }

//    todo: depersonalize this!!
    public Source() {
        this.file = new File("C:\\Users\\masih\\OneDrive\\Desktop\\Sample.wav");
    }

    public File getFile() {
        return file;
    }

    public BufferedInputStream getBufferedStream() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        return new BufferedInputStream(fileInputStream);
    }

}
