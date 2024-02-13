package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yps.systems.ai.model.RoleEntity;
import yps.systems.ai.model.RoleNode;
import yps.systems.ai.repository.IRoleEntityRepository;
import yps.systems.ai.repository.IRoleNodeRepository;

@RestController
@RequestMapping("/roleService")
public class RoleController {

    @Autowired
    private IRoleEntityRepository repository;

    @Autowired
    private IRoleNodeRepository nodeRepository;

    @GetMapping("/findAll")
    public Flux<RoleEntity> findAll() {
        return repository.findAll();
    }

    @GetMapping("/findById/{idRole}")
    public Mono<RoleEntity> findById(@PathVariable Long idRole) {
        return repository.findById(idRole)
                .onErrorResume(error -> {
                    System.out.println(error.getMessage());
                    return Mono.just(new RoleEntity());
                });
    }

    @GetMapping("/findByRoleName/{roleName}")
    public Mono<RoleEntity> findByRoleName(@PathVariable String roleName) {
        return repository.findRoleEntityByRoleName(roleName)
                .switchIfEmpty(repository
                        .save(new RoleEntity(roleName.toUpperCase()))
                        .flatMap(roleSaved -> nodeRepository
                                .save(new RoleNode(roleSaved.getIdRole()))
                                .thenReturn(roleSaved) 
                        )
                );
    }

    @PostMapping("/save")
    public Mono<RoleEntity> save(@RequestBody RoleEntity roleEntity) {
        return repository.save(roleEntity)
                .flatMap(roleSaved -> nodeRepository
                        .save(new RoleNode(roleSaved.getIdRole()))
                        .thenReturn(roleSaved)
                )
                .onErrorResume(error -> {
                    System.out.println(error.getMessage());
                    return Mono.just(new RoleEntity());
                });
    }

    @DeleteMapping("/delete")
    public Mono<Boolean> delete(@RequestBody RoleEntity roleEntity) {
        return repository.findById(roleEntity.getIdRole())
                .flatMap(roleFounded -> repository
                        .delete(roleFounded)
                        .then(nodeRepository.deleteRoleNodeByIdRole(roleFounded.getIdRole()))
                        .thenReturn(true)
                )
                .onErrorResume(error -> {
                    System.out.println(error.getMessage());
                    return Mono.just(false);
                });
    }

    @DeleteMapping("/deleteById/{idRole}")
    public Mono<Boolean> deleteById(@PathVariable Long idRole) {
        return repository.findById(idRole)
                .flatMap(roleFounded -> repository
                        .delete(roleFounded)
                        .then(nodeRepository.deleteRoleNodeByIdRole(roleFounded.getIdRole()))
                        .thenReturn(true)
                )
                .onErrorResume(error -> {
                    System.out.println(error.getMessage());
                    return Mono.just(false);
                });
    }

    @PatchMapping("/updateRoleName/{idRole},{roleName}")
    public Mono<RoleEntity> updateRoleName(@PathVariable Long idRole, @PathVariable String roleName) {
        return repository.findById(idRole)
                .flatMap(roleFounded -> {
                    roleFounded.setRoleName(roleName);
                    return repository.save(roleFounded);
                })
                .onErrorResume(error -> {
                    System.out.println(error.getMessage());
                    return Mono.just(new RoleEntity());
                });
    }

}
