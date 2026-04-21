package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.Doctor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.DoctorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }

    @Transactional
    public Doctor save(Doctor entity) {
        return doctorRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        doctorRepository.deleteById(id);
    }
}
