package yps.systems.ai.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import yps.systems.ai.model.RoleEntity;

@Repository
public interface IRoleEntityRepository extends ReactiveCrudRepository<RoleEntity, Long> {

    Mono<RoleEntity> findRoleEntityByRoleName(String roleName);

}
