package com.geoffledak.recordsource.model;

import org.parceler.Parcel;

/**
 * Created by geoff on 4/20/16.
 */

@Parcel
public class Section {

    private String id;
    private String title;

    public Section() { }


    public Section(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
