package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.RevisionVisit;
import com.example.hospitalenities.entity.RevisionVisitId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.RevisionVisitRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RevisionVisitService {

    private final RevisionVisitRepository revisionVisitRepository;

    public List<RevisionVisit> findAll() {
        return revisionVisitRepository.findAll();
    }

    public Optional<RevisionVisit> findById(RevisionVisitId id) {
        return revisionVisitRepository.findById(id);
    }

    @Transactional
    public RevisionVisit save(RevisionVisit entity) {
        return revisionVisitRepository.save(entity);
    }

    @Transactional
    public void deleteById(RevisionVisitId id) {
        revisionVisitRepository.deleteById(id);
    }
}
