package fh.sem.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MapIO {
    private MapIO() {};

    public static void save(File file, String mapData) {
        try(BufferedOutputStream bos = new BufferedOutputStream(
            new FileOutputStream(file))) {

            for(char c : mapData.toCharArray())
                bos.write(c);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static String load(File file) {
        StringBuilder data = new StringBuilder();

        try(BufferedInputStream bis = new BufferedInputStream(
            new FileInputStream(file))) {

            int d;
            while((d = bis.read()) >= 0)
                data.append((char)d);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return data.toString();
    }
}