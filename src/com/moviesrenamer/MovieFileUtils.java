package com.moviesrenamer;

import javafx.collections.ObservableList;

import java.io.File;
import java.util.List;

public class MovieFileUtils {

    public final static String[] EXTENSIONS = {"*.avi", "*.mp4", "*.mkv"};

    public static boolean isMovieFile(File file) {
        for (String extension : EXTENSIONS)
            if (file.getName().endsWith(extension))
                return true;
        return false;
    }

}
