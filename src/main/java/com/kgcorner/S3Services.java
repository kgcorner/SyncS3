package com.kgcorner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.apache.log4j.Logger;

import java.io.*;

public class S3Services {

    private static S3Services INSTANCE = null;
    private final AmazonS3Client amazonS3Client;
    private static final Logger LOGGER = Logger.getLogger(S3Services.class);
    /**
     * Constructor of {@link S3Services}
     * @param accessKey access key of AWS
     * @param secret Secret key of AWS
     */
    private S3Services(String accessKey, String secret) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secret);
        amazonS3Client = new AmazonS3Client(credentials);
    }

    /**
     * Initializes {@link S3Services}
     * @param accessKey access key of aws services
     * @param secretKey Secret key of aws services
     */
    public static void init(String accessKey, String secretKey) {
        if(Strings.isNullOrEmpty(accessKey) || Strings.isNullOrEmpty(secretKey)) {
            throw new IllegalArgumentException("Access key or Secret key cant be null");
        }
        INSTANCE = new S3Services(accessKey, secretKey);
    }

    /**
     * Get an Instance of {@link S3Services}
     * @return an instance of {@link S3Services}
     */
    public static S3Services getInstance() {
        if(INSTANCE == null) {
            throw new IllegalStateException("S3Service is not initialized. Call S3Service.init() to initialize.");
        }
        return INSTANCE;
    }

    /**
     * Stores given file to Amazon S3 in given bucket
     * @param bucketName name of the bucket in which file will be stored
     * @param filePath absolute path of the file
     * @param size size of the file
     */
    public void storeFileToS3(String bucketName, String filePath, long size) throws FileNotFoundException {
        LOGGER.info("Saving file:"+filePath+" in bucket:"+bucketName+" file size:"+size);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);

        File file = new File(filePath);
        if(!file.isAbsolute()) {
            throw new IllegalArgumentException("file path:"+filePath+" is not absolute");
        }

        if(!file.exists()) {
            throw new IllegalArgumentException("file path:"+filePath+" not found");
        }

        InputStream fileStream = new FileInputStream(file);
        PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), fileStream, objectMetadata);
        PutObjectResult putObjectResult = amazonS3Client.putObject(request);
        LOGGER.info("File :"+filePath+" uploaded sucessfully");
        if(LOGGER.isDebugEnabled()) {
            logDetails(putObjectResult);
        }
    }

    /**
     * Stores given file to Amazon S3 in given bucket
     * @param bucketName name of the bucket in which file will be stored
     * @param fileName name with which files will be stored
     * @param inputStream stream of the file to be stored
     * @param size size of the file
     */
    public void storeFileToS3(String bucketName, String fileName, InputStream inputStream, long size) {
        LOGGER.info("Saving file:"+fileName+" in bucket:"+bucketName+" file size:"+size);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);

        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata);
        PutObjectResult putObjectResult = amazonS3Client.putObject(request);
        LOGGER.info("File :"+fileName+" uploaded sucessfully");
        if(LOGGER.isDebugEnabled()) {
            logDetails(putObjectResult);
        }
    }

    private void logDetails(PutObjectResult putObjectResult) {
        LOGGER.debug("MD5:"+ putObjectResult.getContentMd5());
        LOGGER.debug("Etag:"+ putObjectResult.getETag());
        LOGGER.debug("ExpiryTime:"+ putObjectResult.getExpirationTime());
        LOGGER.debug("is encrypted:"+Strings.isNullOrEmpty(putObjectResult.getSSEAlgorithm()));
    }
}
