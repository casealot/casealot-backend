package kr.casealot.shop.global.util;

import java.util.UUID;

public class FileUtil {
    public static String generateSavedFileName(String orgFileName){
        return String.format("%s-%s",UUID.randomUUID(),orgFileName);
    }
}
