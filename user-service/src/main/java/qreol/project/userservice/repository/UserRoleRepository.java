package qreol.project.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import qreol.project.userservice.model.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("select u.roleId from UserRole u where u.userId = ?1")
    List<Long> findAllRolesIdsFromUserId(Long id);

    @Query("select u from UserRole u where u.userId = ?1 and u.roleId = ?2")
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

}
