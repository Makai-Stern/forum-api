package io.makai.service;

import io.makai.entity.RoleEntity;
import io.makai.enums.RoleType;

import java.util.List;

public interface RoleService {
    void seed();

    RoleEntity find(RoleType roleType);

    List<RoleEntity> findAll();
}
