package com.vazzarmoviedb.radarrbackend.model.enums;

public enum MovieCategory {
    GENERAL(2000, "Movies"),
    SD(2030, "Movies/SD"),
    HD(2040, "Movies/HD"),
    UHD(2045, "Movies/UHD"),
    BLURAY(2050, "Movies/BluRay"),
    THREE_D(2060, "Movies/3D"),
    DVD(2070, "Movies/DVD"),
    WEB_DL(2080, "Movies/WEB-DL");

    private final int code;
    private final String label;

    MovieCategory(int code, String label) {
        this.code = code;
        this.label = label;
    }
    public int getCode() {
        return code;
    }
    public String getLabel() {
        return label;
    }
}
