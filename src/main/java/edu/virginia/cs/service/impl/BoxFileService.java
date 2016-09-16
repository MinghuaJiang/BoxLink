package edu.virginia.cs.service.impl;

import com.box.sdk.*;
import edu.virginia.cs.security.OAuthManager;
import edu.virginia.cs.service.CloudFileRecord;
import edu.virginia.cs.util.ClipboardUtil;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Properties;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.secure;


/**
 * Created by Administrator on 2016/9/10.
 */
public class BoxFileService extends AbstractCloudFileService {
    private BoxAPIConnection connection;
    private static final String CLIENT_ID = "0o9m9en0s21s5mn9v6t9b2a3yv53y0f5";
    private static final String CLIENT_SECRET = "BqoiS3icTBZKJ7La9PVPbV9d2aBDBgHG";
    private OAuthManager manager;
    private Properties tokenProperties;
    private boolean isGranted;

    public BoxFileService() {
        port(10086);
        try {
            secure(Paths.get(ClassLoader.getSystemResource("keystore.jks").toURI()).toFile().getAbsolutePath(), "uvabox", null, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        get("/", (req, res) -> {
            if(req.queryParams().contains("code")){
                connection = new BoxAPIConnection(CLIENT_ID, CLIENT_SECRET,req.queryParams("code"));
                tokenProperties.put("accessToken",connection.getAccessToken());
                tokenProperties.put("refreshToken",connection.getRefreshToken());
                try {
                    tokenProperties.store(new FileOutputStream(new File("token.properties")),"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isGranted = true;
            }
            return "Successfully granted access";
        });
        manager = new OAuthManager();
        tokenProperties = new Properties();
        try {
            File file = new File("token.properties");
            if (!file.exists()) {
                file.createNewFile();
            }
            tokenProperties.load(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String accessToken = tokenProperties.getProperty("accessToken");
        String refreshToken = tokenProperties.getProperty("refreshToken");
        connection = new BoxAPIConnection(CLIENT_ID, CLIENT_SECRET, accessToken, refreshToken);
    }

    private void grantAccess() {
        isGranted = false;
        manager.requestAuthCode();
        while(!isGranted){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected CloudFileRecord doUploadFile(File file) {
        BoxFolder rootFolder = BoxFolder.getRootFolder(connection);
        try (FileInputStream stream = new FileInputStream(file)) {
            BoxFile.Info info = rootFolder.uploadFile(stream, file.getName());
            String id = info.getID();
            String link = generateSharedLink(id);
            ClipboardUtil.copyStringtoClipBoard(link);
            CloudFileRecord item = new CloudFileRecord();
            item.setId(id);
            item.setFileName(info.getName());
            item.setCreatedBy(info.getCreatedBy().getName());
            item.setCreatedTime(info.getCreatedAt());
            item.setUrl(link);
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CloudFileRecord doUploadFile(File file, ProgressListener listener) {
        BoxFolder rootFolder = BoxFolder.getRootFolder(connection);
        try (FileInputStream stream = new FileInputStream(file)) {
            BoxFile.Info info = rootFolder.uploadFile(stream, file.getName(), file.length(), listener);
            String id = info.getID();
            String link = generateSharedLink(id);
            ClipboardUtil.copyStringtoClipBoard(link);
            CloudFileRecord item = new CloudFileRecord();
            item.setId(id);
            item.setFileName(info.getName());
            item.setCreatedBy(info.getCreatedBy().getName());
            item.setCreatedTime(info.getCreatedAt());
            item.setUrl(link);
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String generateSharedLink(String id) {
        BoxFile file = new BoxFile(connection, id);
        BoxSharedLink.Permissions permissions = new BoxSharedLink.Permissions();
        permissions.setCanDownload(true);
        permissions.setCanPreview(true);
        BoxSharedLink sharedLink = file.createSharedLink(BoxSharedLink.Access.OPEN, null, permissions);
        return sharedLink.getURL();
    }

    public void deleteFile(String id) {
        BoxFile file = new BoxFile(connection, id);
        file.delete();
        deleteRecord(id);
    }

    @Override
    public void grantConnectionIfNeccessary() {
        try {
            BoxFolder folder = BoxFolder.getRootFolder(connection);
            folder.getCollaborations();
        } catch (BoxAPIException e) {
            grantAccess();
        }
    }

}
