package eventy.utils;

import java.util.Arrays;

public class UploadFileUtils {
    public static boolean hasExtension(String filename, String... formats) {
        String[] parts = filename.split("\\.");

        return Arrays.asList(formats).contains(parts[1]);
    }
}
