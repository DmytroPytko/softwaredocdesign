package com.pytko.microsoft.repository;

import com.pytko.microsoft.domain.ContractType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ContractType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractTypeRepository extends JpaRepository<ContractType, Long>, JpaSpecificationExecutor<ContractType> {
}
