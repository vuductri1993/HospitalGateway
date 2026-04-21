package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.Prescription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.PrescriptionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public List<Prescription> findAll() {
        return prescriptionRepository.findAll();
    }

    public Optional<Prescription> findById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Transactional
    public Prescription save(Prescription entity) {
        return prescriptionRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        prescriptionRepository.deleteById(id);
    }
}
