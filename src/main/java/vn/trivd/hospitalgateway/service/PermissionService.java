package vn.trivd.hospitalgateway.service;

import com.example.hospitalenities.entity.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trivd.hospitalgateway.repository.PermissionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Transactional
    public Permission save(Permission entity) {
        return permissionRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }
}
