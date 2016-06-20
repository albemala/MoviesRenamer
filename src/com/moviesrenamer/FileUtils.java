package com.moviesrenamer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    public static List<File> getFilesInDirectory(File directory) {
        if (!directory.isDirectory()) return new ArrayList<>();
        File[] files = directory.listFiles();
        if (files == null) return new ArrayList<>();
        return Arrays.asList(files);
    }
}
