package com.kgcorner;

import java.io.IOException;
import java.util.Properties;


/**
 * Class for fetching properties of Aws account
 */
public class S3Properties {
    private static S3Properties instance = new S3Properties();
    private static final String ACCESS_KEY = "aws.access.key";
    private static final String SECRET_KEY = "aws.secret.key";
    private static final String BUCKET_NAME = "aws.bucket.name";
    private static final String PATH_TO_SYNC_DIR = "path.to.directory.to.sync";

    private Properties properties;

    private S3Properties() {
        properties = new Properties();
        try {
            properties.load(S3Properties.class.getResourceAsStream("/s3.properties"));
        } catch (IOException e) {
            throw new IllegalStateException("s3.properties file not found");
        }
    }

    public static S3Properties getInstance() {
        return instance;
    }

    /**
     * Aws user account's access key to access AWS services
     * @return access key to access AWS services
     */
    public String getAccessKey() {
        return properties.getProperty(ACCESS_KEY);
    }

    /**
     * Aws user account's secret key to access AWS services
     * @return secret key to access AWS services
     */
    public String getSecretKey() {
        return properties.getProperty(SECRET_KEY);
    }

    /**
     * returns bucket's name
     * @return bucket's name
     */
    public String getBucketName() {
        return properties.getProperty(BUCKET_NAME);
    }

    /**
     * Returns path to sync directory
     * @return path to sync directory
     */
    public String getPathToSyncDir() {
        return properties.getProperty(PATH_TO_SYNC_DIR);
    }
}
