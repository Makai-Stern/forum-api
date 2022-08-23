package io.makai.service.impl;

import io.makai.entity.RoleEntity;
import io.makai.enums.RoleType;
import io.makai.repository.RoleRepository;
import io.makai.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.seed();
    }

    @Override
    public void seed() {
        if (this.roleRepository.count() > 0) {
            return;
        }

        for (RoleType roleType : RoleType.values()) {
            RoleEntity role = new RoleEntity();
            role.setName(roleType.name());
            this.roleRepository.save(role);
        }

        this.roleRepository.flush();
    }

    @Override
    public RoleEntity find(RoleType roleType) {
        return this.roleRepository.findByName(roleType.name());
    }

    @Override
    public List<RoleEntity> findAll() {
        return this.roleRepository.findAll();
    }
}
