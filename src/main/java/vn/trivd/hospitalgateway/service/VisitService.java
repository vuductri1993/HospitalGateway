package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.Visit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.VisitRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitService {

    private final VisitRepository visitRepository;

    public List<Visit> findAll() {
        return visitRepository.findAll();
    }

    public Optional<Visit> findById(Long id) {
        return visitRepository.findById(id);
    }

    @Transactional
    public Visit save(Visit entity) {
        return visitRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        visitRepository.deleteById(id);
    }
}
