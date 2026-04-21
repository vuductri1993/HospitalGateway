package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.LabImagingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.LabImagingResultRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LabImagingResultService {

    private final LabImagingResultRepository labImagingResultRepository;

    public List<LabImagingResult> findAll() {
        return labImagingResultRepository.findAll();
    }

    public Optional<LabImagingResult> findById(Long id) {
        return labImagingResultRepository.findById(id);
    }

    @Transactional
    public LabImagingResult save(LabImagingResult entity) {
        return labImagingResultRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        labImagingResultRepository.deleteById(id);
    }
}
