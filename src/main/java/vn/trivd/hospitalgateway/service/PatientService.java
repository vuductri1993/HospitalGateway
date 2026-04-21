package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.PatientRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Transactional
    public Patient save(Patient entity) {
        return patientRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }
}
