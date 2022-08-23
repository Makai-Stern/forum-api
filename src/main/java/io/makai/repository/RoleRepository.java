package io.makai.repository;

import io.makai.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    RoleEntity findByName(String name);
}
