package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.RevisionLabResult;
import com.example.hospitalenities.entity.RevisionLabResultId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.RevisionLabResultRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RevisionLabResultService {

    private final RevisionLabResultRepository revisionLabResultRepository;

    public List<RevisionLabResult> findAll() {
        return revisionLabResultRepository.findAll();
    }

    public Optional<RevisionLabResult> findById(RevisionLabResultId id) {
        return revisionLabResultRepository.findById(id);
    }

    @Transactional
    public RevisionLabResult save(RevisionLabResult entity) {
        return revisionLabResultRepository.save(entity);
    }

    @Transactional
    public void deleteById(RevisionLabResultId id) {
        revisionLabResultRepository.deleteById(id);
    }
}
