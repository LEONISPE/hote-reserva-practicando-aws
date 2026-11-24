package com.phegondev.PhegonHotel.service;


import com.phegondev.PhegonHotel.exception.OurException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
public class AwsS3Service {

    @Value("${aws.s3.bucket.name}")
    private  String bucketName;


    private final S3Client s3Client = S3Client.builder()
            .region(Region.US_EAST_2)
            .build();

    public String saveImageToS3(MultipartFile photo) {
        try {
            String s3Filename = photo.getOriginalFilename();
            InputStream inputStream = photo.getInputStream();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Filename)
                    .contentType(photo.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, photo.getSize()));

            return "https://" + bucketName + ".s3.amazonaws.com/" + s3Filename;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to upload image to s3 bucket: " + e.getMessage());
        }
    }
}

















