package com.sodved.itesteg.app.service;

import com.sodved.itesteg.lib.domain.InnoTable;
import com.sodved.itesteg.lib.repository.TableRepository;
import com.sodved.itesteg.lib.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApplicationTableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private TableService tableService;

    public void listTables() {
        for (InnoTable table: tableRepository.findAll()) {
            log.info("Table[{}]: {}", table.getTableId(), table.getName());
        }
    }

}
