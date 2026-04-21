package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.Drug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.DrugRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DrugService {

    private final DrugRepository drugRepository;

    public List<Drug> findAll() {
        return drugRepository.findAll();
    }

    public Optional<Drug> findById(Long id) {
        return drugRepository.findById(id);
    }

    @Transactional
    public Drug save(Drug entity) {
        return drugRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        drugRepository.deleteById(id);
    }
}
