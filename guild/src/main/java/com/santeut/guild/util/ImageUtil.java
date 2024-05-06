package com.santeut.guild.util;

import com.santeut.guild.entity.Image;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Getter
public class ImageUtil {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String saveImage(MultipartFile multipartFile){

        String originalName = multipartFile.getOriginalFilename();

        Image image = new Image(originalName);
        String fileName = image.getStoredName();

        try{
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, multipartFile.getInputStream(), objectMetadata));
            String accessUrl = amazonS3Client.getUrl(bucketName, fileName).toString();
            image.setAccessUrl(accessUrl);

        }catch (IOException e){
            throw new RuntimeException("Failed to upload file to S3", e);
        }
        return image.getAccessUrl();
    }
}

