package com.mehediFifo.CRM.service;

import com.mehediFifo.CRM.entity.DataTables;
import com.mehediFifo.CRM.repository.DataTablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataTablesService {
    @Autowired
    private DataTablesRepository repository;

    public DataTables saveDataTables(DataTables dataTables) {
        // Find the existing record by campaignName
        DataTables existingDataTables = repository.findByCampaignName(dataTables.getCampaignName());

        if (existingDataTables != null) {
            // If the campaign exists, sum the numberOfField from existing and new
            int updatedNumberOfField = existingDataTables.getNumberOfField() + dataTables.getNumberOfField();
            existingDataTables.setNumberOfField(updatedNumberOfField);
            return repository.save(existingDataTables);
        } else {
            // If no such campaign exists, create a new record
            dataTables.setName(dataTables.getCampaignName());
            return repository.save(dataTables);
        }
    }

    // Deleting a Data Tables
    public void deleteDataTables(Long id) {
        repository.deleteById(id);
    }

    // Calling all Data Tables
    public List<DataTables> getAllDataTables(){
        return repository.findAll();
    }

    public List<DataTables> search(String name, String campaignName, Integer numberOfField) {
        List<DataTables> dataTables = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            dataTables.addAll(repository.findByNameContainingIgnoreCase(name));
        }
        if (campaignName != null && !campaignName.isEmpty()) {
            dataTables.addAll(repository.findByCampaignNameContainingIgnoreCase(campaignName));
        }
        if (numberOfField != null) {
            dataTables.addAll(repository.findByNumberOfField(numberOfField)); // exact match
        }

        return dataTables.stream().distinct().collect(Collectors.toList());
    }

    public Page<DataTables> findDataTablesWithPagination(int offset, int pageSize) {
        return repository.findAll(PageRequest.of(offset, pageSize));
    }
}
