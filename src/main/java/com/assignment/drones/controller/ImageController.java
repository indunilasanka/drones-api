package com.assignment.drones.controller;

import com.assignment.drones.model.dto.BaseResponseDTO;
import com.assignment.drones.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Image controller interfaces
 */
@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(path = "/medications")
    public ResponseEntity<BaseResponseDTO> uploadMedicationImage(@RequestParam("medicationId") long medicationId, @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(imageService.uploadMedicationImage(medicationId, file), HttpStatus.OK);
    }

    @GetMapping(value = "/medications/{medication_id}")
    public ResponseEntity<byte[]> downloadMedicationImage(@PathVariable("medication_id") long id) {
        byte[] imageBytes = imageService.downloadMedicationPicture(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
}
