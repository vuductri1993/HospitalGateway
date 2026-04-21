package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.MedicalRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.MedicalRecordRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public List<MedicalRecord> findAll() {
        return medicalRecordRepository.findAll();
    }

    public Optional<MedicalRecord> findById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    @Transactional
    public MedicalRecord save(MedicalRecord entity) {
        return medicalRecordRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        medicalRecordRepository.deleteById(id);
    }
}
