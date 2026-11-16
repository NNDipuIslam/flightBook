
package com.classz.flightbook.user.repository;

import com.classz.flightbook.user.entity.Role;
import com.classz.flightbook.user.entity.RoleName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName);

}
