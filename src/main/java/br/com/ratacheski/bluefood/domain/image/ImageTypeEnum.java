package br.com.ratacheski.bluefood.domain.image;

import org.springframework.lang.Nullable;

public enum ImageTypeEnum {
    CATEGORIA("categoria"),
    COMIDA("comida"),
    LOGOTIPO("logotipo");

    private String id;

    ImageTypeEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ImageTypeEnum fromId(String id) {
        for (ImageTypeEnum at : ImageTypeEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
