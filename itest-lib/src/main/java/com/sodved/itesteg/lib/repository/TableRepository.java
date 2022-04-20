package com.sodved.itesteg.lib.repository;

import com.sodved.itesteg.lib.domain.InnoTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TableRepository extends CrudRepository<InnoTable, Long> {

    public List<InnoTable> findByName(String name);
    public List<InnoTable> findByNameLike(String name);

}

