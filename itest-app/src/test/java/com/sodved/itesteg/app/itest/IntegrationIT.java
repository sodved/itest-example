package com.sodved.itesteg.app.itest;

import com.sodved.itesteg.app.service.ApplicationTableService;
import com.sodved.itesteg.lib.domain.InnoTable;
import com.sodved.itesteg.lib.service.TableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("itest")
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith({SpringExtension.class})
@SpringBootTest(classes=DummyApplicationContext.class)
public class IntegrationIT {

    private static final String tableName = "SYS_VIRTUAL";

    @Autowired
    private DataSource mariadb;

    //@Qualifier("verticaDataSource")
    //@Autowired
    //DataSource vertica;

    @Autowired
    TableService tableService;

    //@Autowired
    //private ApplicationTableService applicationTableService;

    @Test
    public void testApplicationLoaded() {
        log.info("Application loaded OK");
    }

    @Test
    public void testMariadb() throws Exception {
        try (Connection conn = mariadb.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 'fred'")) {
            Assertions.assertTrue(rs.next());
            Assertions.assertEquals("fred", rs.getString(1));
        }
    }

    //@Test
    //public void testVertica() throws Exception {
        //try (Connection conn = vertica.getConnection();
             //Statement stmt = conn.createStatement();
             //ResultSet rs = stmt.executeQuery("SELECT 'fred'")) {
            //Assertions.assertTrue(rs.next());
            //Assertions.assertEquals("fred", rs.getString(1));
        //}
    //}

    @Test
    public void testLibService() throws Exception {
        InnoTable table = tableService.getTableByName(tableName);
        Assertions.assertEquals(tableName, table.getName());
    }

    //@Test
    //public void testAppService() throws Exception {
        //InnoTable table = applicationTableService.getSpecialTable();
        //Assertions.assertEquals("SYS_VIRTUAL", table.getName());
    //}

}
