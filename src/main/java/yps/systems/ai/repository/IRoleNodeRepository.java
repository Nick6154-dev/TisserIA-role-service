package yps.systems.ai.repository;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import yps.systems.ai.model.RoleNode;

@Repository
public interface IRoleNodeRepository extends ReactiveNeo4jRepository<RoleNode, Long> {

    Mono<RoleNode> findRoleNodeByIdRole(Long idRole);

    Mono<Void> deleteRoleNodeByIdRole(Long idRole);

}
