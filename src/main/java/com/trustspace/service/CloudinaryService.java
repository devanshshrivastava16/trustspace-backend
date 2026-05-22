package com.trustspace.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    
    private final Cloudinary cloudinary;
    
    public Map<String, String> uploadImage(MultipartFile file, String folder) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "trustspace/" + folder,
                            "resource_type", "auto"
                    ));
            
            String imageUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            
            log.info("Image uploaded successfully: {}", publicId);
            
            return Map.of(
                    "imageUrl", imageUrl,
                    "publicId", publicId
            );
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }
    
    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Image deleted successfully: {}", publicId);
        } catch (IOException e) {
            log.error("Failed to delete image: {}", publicId, e);
            throw new RuntimeException("Failed to delete image: " + e.getMessage());
        }
    }
}
