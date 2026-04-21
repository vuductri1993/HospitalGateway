package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.MedicalRecordRevision;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.MedicalRecordRevisionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalRecordRevisionService {

    private final MedicalRecordRevisionRepository medicalRecordRevisionRepository;

    public List<MedicalRecordRevision> findAll() {
        return medicalRecordRevisionRepository.findAll();
    }

    public Optional<MedicalRecordRevision> findById(Long id) {
        return medicalRecordRevisionRepository.findById(id);
    }

    @Transactional
    public MedicalRecordRevision save(MedicalRecordRevision entity) {
        return medicalRecordRevisionRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        medicalRecordRevisionRepository.deleteById(id);
    }
}
