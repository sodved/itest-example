package com.sodved.itesteg.app.service;

import com.sodved.itesteg.lib.domain.InnoTable;
import com.sodved.itesteg.lib.repository.TableRepository;
import com.sodved.itesteg.lib.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Qualifier("verticaJdbcTemplate")
    @Autowired
    JdbcTemplate vertica;

    @Value("${sodved.specialTable}")
    private String specialTable;


    private static final String VERTICA_TABLE_SQL = "SELECT table_name FROM v_catalog.tables ORDER BY table_name";

    public void listMariadbTables() {
        log.info("Mariadb Tables");
        for (InnoTable table: tableRepository.findAll()) {
            log.info("- Table[{}]: {}", table.getTableId(), table.getName());
        }
    }

    public void listVerticaTables() {
        log.info("Vertica Tables");
        vertica.query(VERTICA_TABLE_SQL
            , (rs) -> { log.info("- Table {}", rs.getString(1)); }
            );
    }

    public InnoTable getSpecialTable() throws Exception {
        return tableService.getTableByName(specialTable);
    }
}
