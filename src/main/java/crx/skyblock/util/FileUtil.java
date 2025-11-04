package crx.skyblock.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class FileUtil {
    public static void copy(File from, File to) throws IOException {
        if(!from.exists()) return;

        if(from.isDirectory()) {
            to.mkdir();
            for(File f : Objects.requireNonNull(from.listFiles())) {
                copy(f, new File(to, f.getName()));
            }
        } else {
            Files.copy(from.toPath(), to.toPath());
        }
    }

    public static void delete(File file) {
        if(file.exists()) {
            if(file.isDirectory()) {
                for(File f : Objects.requireNonNull(file.listFiles())) {
                    delete(f);
                }
                file.delete();
            } else {
                file.delete();
            }
        }
    }
}
