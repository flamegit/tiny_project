package com.flame.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */

public class FileUtils {


    private  void getAllFiles(File root, String ext) {
        List<File> list = new ArrayList<>();
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getAllFiles(f, ext);
                } else {
                    if (f.getName().endsWith(ext) && f.length() > 50)
                        list.add(f);
                }
            }
        }
    }
}
