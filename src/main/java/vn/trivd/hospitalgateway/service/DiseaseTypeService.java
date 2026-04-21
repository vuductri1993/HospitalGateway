package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.DiseaseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.DiseaseTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiseaseTypeService {

    private final DiseaseTypeRepository diseaseTypeRepository;

    public List<DiseaseType> findAll() {
        return diseaseTypeRepository.findAll();
    }

    public Optional<DiseaseType> findById(Long id) {
        return diseaseTypeRepository.findById(id);
    }

    @Transactional
    public DiseaseType save(DiseaseType entity) {
        return diseaseTypeRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        diseaseTypeRepository.deleteById(id);
    }
}
