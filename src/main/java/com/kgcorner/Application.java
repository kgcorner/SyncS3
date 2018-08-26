package com.kgcorner;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class);
    public static void main(String[] args) {
        S3Properties properties = S3Properties.getInstance();
        String accessKey = properties.getAccessKey();
        String secretKey = properties.getSecretKey();
        String bucketName = properties.getBucketName();
        String syncDir = properties.getPathToSyncDir();
        S3Services.init(accessKey, secretKey);
        S3Services s3Services = S3Services.getInstance();
        File file = new File(syncDir);
        if(file.isDirectory()) {
            if(file.listFiles() != null) {
                for (File sFile : Objects.requireNonNull(file.listFiles())) {
                    try {
                        s3Services.storeFileToS3(bucketName, sFile.getAbsolutePath(), sFile.length());
                    } catch (FileNotFoundException e) {
                        LOGGER.error("Application failed:" + e.getLocalizedMessage());
                        System.exit(10);
                    }
                }
            }
        } else {
            try {
                s3Services.storeFileToS3(bucketName, file.getAbsolutePath(), file.length());
            } catch (FileNotFoundException e) {
                LOGGER.error("Application failed:"+e.getLocalizedMessage());
                System.exit(10);
            }
        }
    }
}
