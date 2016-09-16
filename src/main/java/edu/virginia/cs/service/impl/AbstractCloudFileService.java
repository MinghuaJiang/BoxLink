package edu.virginia.cs.service.impl;

import com.box.sdk.ProgressListener;
import edu.virginia.cs.service.CloudFileRecord;
import edu.virginia.cs.service.CloudFileService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/9/15.
 */
public abstract class AbstractCloudFileService implements CloudFileService {
    private Map<String, CloudFileRecord> records;
    public AbstractCloudFileService(){
        File file = new File("records.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                records = (Map<String, CloudFileRecord>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                records = new HashMap<>();
            }
        } else {
            records = new HashMap<>();
        }
    }

    protected abstract CloudFileRecord doUploadFile(File file);
    protected abstract CloudFileRecord doUploadFile(File file, ProgressListener listener);

    @Override
    public void uploadFile(String fileName){
        File file = new File(fileName);
        CloudFileRecord info = doUploadFile(file);
        saveRecord(info);
    }

    @Override
    public void uploadFile(String fileName, ProgressListener listener){
        File file = new File(fileName);
        CloudFileRecord info = doUploadFile(file, listener);
        saveRecord(info);
    }

    @Override
    public List<CloudFileRecord> getUploadedItems() {
        List<CloudFileRecord> result = records.values().stream().sorted(((x, y) -> y.getCreatedTime().compareTo(x.getCreatedTime()))).collect(Collectors.toList());
        return result;
    }

    protected void saveRecord(CloudFileRecord info) {
        records.put(info.getId(), info);
    }
    protected void deleteRecord(String id) {
        records.remove(id);
    }

    @Override
    public void saveRecords() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("records.dat")))) {
            oos.writeObject(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
