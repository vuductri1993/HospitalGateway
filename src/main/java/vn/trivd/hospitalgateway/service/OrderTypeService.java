package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.OrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.OrderTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderTypeService {

    private final OrderTypeRepository orderTypeRepository;

    public List<OrderType> findAll() {
        return orderTypeRepository.findAll();
    }

    public Optional<OrderType> findById(Long id) {
        return orderTypeRepository.findById(id);
    }

    @Transactional
    public OrderType save(OrderType entity) {
        return orderTypeRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        orderTypeRepository.deleteById(id);
    }
}
