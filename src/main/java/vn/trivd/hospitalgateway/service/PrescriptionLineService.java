package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.PrescriptionLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.PrescriptionLineRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrescriptionLineService {

    private final PrescriptionLineRepository prescriptionLineRepository;

    public List<PrescriptionLine> findAll() {
        return prescriptionLineRepository.findAll();
    }

    public Optional<PrescriptionLine> findById(Long id) {
        return prescriptionLineRepository.findById(id);
    }

    @Transactional
    public PrescriptionLine save(PrescriptionLine entity) {
        return prescriptionLineRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        prescriptionLineRepository.deleteById(id);
    }
}
