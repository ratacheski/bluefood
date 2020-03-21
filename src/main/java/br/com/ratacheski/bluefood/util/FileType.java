package br.com.ratacheski.bluefood.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
    PNG("image/png", "png"),
    JPG("image/jpeg", "jpg");

    String mimeType;
    String extension;

    public boolean sameOf(String mimeType) {
        return this.mimeType.equalsIgnoreCase(mimeType);
    }

    public static FileType of(String mimeType) {
        for (FileType fileType : values()) {
            if (fileType.sameOf(mimeType)) {
                return fileType;
            }
        }
        return null;
    }
}
