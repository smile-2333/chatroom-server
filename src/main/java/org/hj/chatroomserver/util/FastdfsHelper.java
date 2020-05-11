package org.hj.chatroomserver.util;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class FastdfsHelper {

    private StorageClient storageClient;

    @Value("${deployIP}")
    private String deployIP;

    public FastdfsHelper(@Value("${fastdfs.tracker}") String tracker) throws IOException, MyException {
        Properties props = new Properties();
        props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, tracker);
        ClientGlobal.initByProperties(props);
        storageClient = new StorageClient(new TrackerClient().getTrackerServer());
    }

    public String upload(InputStream inputStream, String extName) throws Exception {
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String[] strings = storageClient.upload_file(bytes, extName, new NameValuePair[0]);
        if (strings[1] != null) {
            return String.format("http://%s/%s",deployIP,strings[1]);
        }
        return null;
    }

}
