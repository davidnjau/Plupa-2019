package com.ben.planninact.helper_class;

public class PlupaPojo {

    private String id;
    private String part_heading;
    private String part_description;
    private String part_id;

    public PlupaPojo() {
    }

    public PlupaPojo(String id, String part_heading, String part_description, String part_id) {
        this.id = id;
        this.part_heading = part_heading;
        this.part_description = part_description;
        this.part_id = part_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPart_heading() {
        return part_heading;
    }

    public void setPart_heading(String part_heading) {
        this.part_heading = part_heading;
    }

    public String getPart_description() {
        return part_description;
    }

    public void setPart_description(String part_description) {
        this.part_description = part_description;
    }

    public String getPart_id() {
        return part_id;
    }

    public void setPart_id(String part_id) {
        this.part_id = part_id;
    }
}
