package com.sodved.itesteg.lib.service;

import com.sodved.itesteg.lib.domain.InnoTable;
import com.sodved.itesteg.lib.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    public InnoTable getTableByName(String name) throws Exception {
        List<InnoTable> tableList = tableRepository.findByName(name);
        if (tableList.isEmpty()) {
            throw new Exception("Table ["+name+"] not found");
        }
        else if (tableList.size() > 1) {
            throw new Exception("Multiple tables for name ["+name+"]");
        }
        else {
            return tableList.get(0);
        }
    }

}
