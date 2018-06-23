package com.comunityapp.service;

import com.comunityapp.domain.PublicSpace;
import com.comunityapp.repository.PublicSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublicSpaceService {
    private final PublicSpaceRepository publicSpaceRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    public PublicSpace savePublicSpace(PublicSpace publicSpace) {
        LOGGER.info("savePublicSpace called:{}", publicSpace);

        publicSpace = publicSpaceRepository.save(publicSpace);

        LOGGER.info("savePublicSpace result:{}", publicSpace);
        return publicSpace;
    }

    public List<PublicSpace> getPublicSpaces() {
        LOGGER.info("getPublicSpaces called");

        List<PublicSpace> publicSpaces = publicSpaceRepository.findAll();

        LOGGER.info("getPublicSpaces result:{}", publicSpaces);
        return publicSpaces;
    }

    public Optional<PublicSpace> findById(long id) {
        LOGGER.info("findById Called:{}", id);

        Optional<PublicSpace> publicSpace = publicSpaceRepository.findById(id);

        LOGGER.info("findById result:{}", publicSpace);
        return publicSpace;
    }
}
