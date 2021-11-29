package com.ben.planninact.helper_class;

public class PlupaPojo {

    private String dbId;
    private String dbHeading;
    private String dbBody;
    private String dbType;

    public PlupaPojo() {
    }

    public PlupaPojo(String dbId, String dbHeading, String dbBody, String dbType) {
        this.dbId = dbId;
        this.dbHeading = dbHeading;
        this.dbBody = dbBody;
        this.dbType = dbType;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getDbHeading() {
        return dbHeading;
    }

    public void setDbHeading(String dbHeading) {
        this.dbHeading = dbHeading;
    }

    public String getDbBody() {
        return dbBody;
    }

    public void setDbBody(String dbBody) {
        this.dbBody = dbBody;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
