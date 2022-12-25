package com.assignment.drones.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.assignment.drones.model.domain.Medication;
import com.assignment.drones.model.dto.BaseResponseDTO;
import com.assignment.drones.repository.MedicationRepository;
import com.assignment.drones.service.ImageService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Implementation of image operations using AWS S3 storage
 */
@Service("s3ImageService")
public class S3ImageServiceImpl implements ImageService {
    private final Logger LOGGER = LoggerFactory.getLogger(S3ImageServiceImpl.class);
    private final AmazonS3 amazonS3Client;
    private final MedicationRepository medicationRepository;
    @Value("${application.bucket.name}")
    private String bucketName;
    @Value("${cloud.aws.region}")
    private String region;

    @Autowired
    public S3ImageServiceImpl(MedicationRepository medicationRepository) {
        this.amazonS3Client = null; //AmazonS3ClientBuilder.standard().withRegion(region).build();
        this.medicationRepository = medicationRepository;
    }

    /**
     * Downloads image from S3 bucket
     *
     * @param medicationId Medication Id
     * @return ByteArrayOutputStream
     */
    public byte[] downloadMedicationPicture(long medicationId) {
        Optional<Medication> optionalMedication = medicationRepository.findById(medicationId);
        if (optionalMedication.isEmpty()) {
            throw new EntityNotFoundException(String.format("No medication exists for the given id: {}", medicationId));
        }

        Medication medication = optionalMedication.get();
        String keyName = "test"; //medication.getImage();

        int len = 0;
        byte[] buffer = new byte[4096];

        S3Object s3object = amazonS3Client.getObject(new GetObjectRequest(bucketName, keyName));
        InputStream is = s3object.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        while (true) {
            try {
                if ((len = is.read(buffer, 0, buffer.length)) == -1) break;
            } catch (IOException e) {
                LOGGER.error(ExceptionUtils.getStackTrace(e));
            }
            outputStream.write(buffer, 0, len);
        }

        return outputStream.toByteArray();
    }

    /**
     * Upload image into AWS S3
     *
     * @param image File in Multipart format
     * @return Upload status
     */
    @Transactional
    public BaseResponseDTO uploadMedicationImage(long medicationId, MultipartFile image) {
        Optional<Medication> optionalMedication = medicationRepository.findById(medicationId);
        if (optionalMedication.isEmpty()) {
            throw new EntityNotFoundException(String.format("No medication exists for the given id: {}", medicationId));
        }
        Medication medication = optionalMedication.get();

        String keyName = image.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());

        try {
            amazonS3Client.putObject(bucketName, keyName, image.getInputStream(), metadata);
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }

        //medication.setImage(keyName);
        medicationRepository.save(medication);
        BaseResponseDTO dto = new BaseResponseDTO();
        dto.setStatus("success");
        dto.setMessage("File uploaded successfully");
        return dto;
    }
}
