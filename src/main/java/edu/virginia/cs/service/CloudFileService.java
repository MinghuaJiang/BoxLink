package edu.virginia.cs.service;


import com.box.sdk.BoxFile;
import com.box.sdk.ProgressListener;

import java.util.List;

/**
 * Created by Administrator on 2016/9/10.
 */
public interface CloudFileService {
    public void uploadFile(String fileName);

    public void uploadFile(String fileName, ProgressListener listener);

    public void deleteFile(String id);

    public List<CloudFileRecord> getUploadedItems();

    public void saveRecords();

    public void grantConnectionIfNeccessary();
}
