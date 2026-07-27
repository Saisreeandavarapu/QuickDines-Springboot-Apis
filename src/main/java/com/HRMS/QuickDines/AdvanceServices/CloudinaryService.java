package com.HRMS.QuickDines.AdvanceServices;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;


//    public String uploadFile(MultipartFile file) {
//
//        try {
//
//            Map<?, ?> uploadResult =
//                    cloudinary.uploader()
//                            .upload(file.getBytes(),
//                                    ObjectUtils.emptyMap());
//
//            return uploadResult.get("secure_url").toString();
//
//        } catch (Exception e) {
//
//            throw new RuntimeException("File Upload Failed");
//        }
//
//    }
    public String uploadFile(MultipartFile file) {

        try {

            Map<String, Object> options = new HashMap<>();

            options.put("resource_type", "auto");

            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    options
            );

            return result.get("secure_url").toString();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Cloudinary File Upload Failed");
        }

    }

}
