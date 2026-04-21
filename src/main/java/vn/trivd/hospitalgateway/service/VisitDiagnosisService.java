package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.VisitDiagnosis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.VisitDiagnosisRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitDiagnosisService {

    private final VisitDiagnosisRepository visitDiagnosisRepository;

    public List<VisitDiagnosis> findAll() {
        return visitDiagnosisRepository.findAll();
    }

    public Optional<VisitDiagnosis> findById(Long id) {
        return visitDiagnosisRepository.findById(id);
    }

    @Transactional
    public VisitDiagnosis save(VisitDiagnosis entity) {
        return visitDiagnosisRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        visitDiagnosisRepository.deleteById(id);
    }
}
