package com.assignment.drones.service;

import com.assignment.drones.model.dto.BaseResponseDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Define operations to manipulate images
 */
public interface ImageService {

    /**
     * Upload given image into the storage
     */
    BaseResponseDTO uploadMedicationImage(long medicationId, MultipartFile image);

    /**
     * Download image related to the given id from the storage
     */
    byte[] downloadMedicationPicture(long medicationId);
}
