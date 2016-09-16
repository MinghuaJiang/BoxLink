package edu.virginia.cs.service;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/14.
 */
public class CloudFileRecord implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id;
    private String fileName;
    private Date createdTime;
    private String createdBy;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
