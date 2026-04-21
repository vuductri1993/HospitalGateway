package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.RevisionPrescription;
import com.example.hospitalenities.entity.RevisionPrescriptionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.RevisionPrescriptionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RevisionPrescriptionService {

    private final RevisionPrescriptionRepository revisionPrescriptionRepository;

    public List<RevisionPrescription> findAll() {
        return revisionPrescriptionRepository.findAll();
    }

    public Optional<RevisionPrescription> findById(RevisionPrescriptionId id) {
        return revisionPrescriptionRepository.findById(id);
    }

    @Transactional
    public RevisionPrescription save(RevisionPrescription entity) {
        return revisionPrescriptionRepository.save(entity);
    }

    @Transactional
    public void deleteById(RevisionPrescriptionId id) {
        revisionPrescriptionRepository.deleteById(id);
    }
}
