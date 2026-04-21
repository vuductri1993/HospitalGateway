package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.ServiceOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.ServiceOrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;

    public List<ServiceOrder> findAll() {
        return serviceOrderRepository.findAll();
    }

    public Optional<ServiceOrder> findById(Long id) {
        return serviceOrderRepository.findById(id);
    }

    @Transactional
    public ServiceOrder save(ServiceOrder entity) {
        return serviceOrderRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        serviceOrderRepository.deleteById(id);
    }
}
