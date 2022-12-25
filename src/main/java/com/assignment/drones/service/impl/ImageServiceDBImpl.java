package com.assignment.drones.service.impl;

import com.assignment.drones.exception.RuntimeValidationException;
import com.assignment.drones.model.domain.Medication;
import com.assignment.drones.model.dto.BaseResponseDTO;
import com.assignment.drones.repository.MedicationRepository;
import com.assignment.drones.service.ImageService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static com.assignment.drones.util.Constants.RequestStatus.SUCCESS;

/**
 * Implementation of image operations using Database storage
 */
@Service("imageService")
public class ImageServiceDBImpl implements ImageService {
    private final Logger LOGGER = LoggerFactory.getLogger(ImageServiceDBImpl.class);
    private final MedicationRepository medicationRepository;

    public ImageServiceDBImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public BaseResponseDTO uploadMedicationImage(long medicationId, MultipartFile image) {
        String imageName = image.getOriginalFilename();
        String extension = imageName.substring(imageName.lastIndexOf(".") + 1);

        if (!extension.equalsIgnoreCase("jpeg") && !extension.equalsIgnoreCase("jpg")) {
            throw new RuntimeValidationException("Unsupported file type. Only JPG/JPEG supported at the moment");
        }

        Optional<Medication> optionalMedication = medicationRepository.findById(medicationId);
        if (optionalMedication.isEmpty()) {
            throw new EntityNotFoundException("No medication exists for the given id: " + medicationId);
        }

        Medication medication = optionalMedication.get();
        try {
            medication.setImage(image.getBytes());
        } catch (IOException e) {
            LOGGER.error("{}", ExceptionUtils.getStackTrace(e));
        }

        medicationRepository.save(medication);

        BaseResponseDTO dto = new BaseResponseDTO();
        dto.setStatus(SUCCESS);
        dto.setMessage("File uploaded successfully");
        return dto;
    }

    @Override
    public byte[] downloadMedicationPicture(long medicationId) {
        Optional<Medication> optionalMedication = medicationRepository.findById(medicationId);
        if (optionalMedication.isEmpty()) {
            throw new EntityNotFoundException("No medication exists for the given id: " + medicationId);
        }

        Medication medication = optionalMedication.get();
        if (medication.getImage() == null) {
            throw new EntityNotFoundException("No image found for given medication id: " + medicationId);
        }

        return medication.getImage();
    }
}
