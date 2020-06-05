package com.example.imageuploaddropbox;

import com.google.gson.annotations.SerializedName;

public class FileData {
    @SerializedName("link")
    String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
