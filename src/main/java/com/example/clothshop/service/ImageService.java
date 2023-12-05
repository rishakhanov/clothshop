package com.example.clothshop.service;

import com.example.clothshop.entity.Image;
import com.example.clothshop.repository.ImageRepository;
import com.example.clothshop.util.exception.ImageNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image getImage(String name) {
        Optional<Image> image = imageRepository.findByName(name);
        if (image.isPresent() && image.get().getData() != null) {
            return image.get();
        } else {
            throw new ImageNotFoundException();
        }
    }

    public Image getImageById(long id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            return image.get();
        } else {
            throw new ImageNotFoundException();
        }
    }

    @Transactional
    public Image save(MultipartFile file) throws Exception {
        if (imageRepository.existsByName(file.getOriginalFilename())) {
            log.info("Image {} already exists", file.getOriginalFilename());
            return imageRepository.findByName(file.getOriginalFilename()).get();
        }

        var image = Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(file.getBytes())
                .build();

        return imageRepository.save(image);
    }
}
