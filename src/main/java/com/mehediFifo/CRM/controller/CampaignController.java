package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.config.APIResponse;
import com.mehediFifo.CRM.entity.Campaign;
import com.mehediFifo.CRM.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cc")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class CampaignController {
    @Autowired
    private CampaignService service;

    // Adding new campaign
    @PostMapping("/addCampaign")
    public Campaign addCampaign(@RequestBody Campaign campaign) {
        return service.saveCampaign(campaign);
    }

    // Calling all campaigns
    @GetMapping("/campaigns")
    public List<Campaign> findAllCampaigns() {
        return service.getAllCampaigns();
    }

    // Calling a campaign by ID
    @GetMapping("/campaign/{id}")
    public Campaign findCampaignById(@PathVariable("id") Long id) {
        return service.getCampaignById(id);
    }

    // Calling a campaign by Name
    @RequestMapping(method = RequestMethod.GET)
    public Campaign findCampaignsByName(@RequestParam(value="name") String name) {
        return service.getCampaignByName(name);
    }

    // Updating a campaign
    @PutMapping("/updateCampaign")
    public Campaign updateCampaign(@RequestBody Campaign campaign) {
        return service.updateCampaign(campaign);
    }

    // Deleting a campaign
    @DeleteMapping("/delete/{id}")
    public void deleteCampaign(@PathVariable Long id){
        service.deleteCampaign(id);
    }

    @GetMapping("/searchCampaign")
    public List<Campaign> searchCampaign(@RequestParam(value = "campaignId", required = false) String campaignId,
                                         @RequestParam(value = "campaignName", required = false) String campaignName,
                                         @RequestParam(value = "callTarget", required = false) Integer callTarget,
                                         @RequestParam(value = "status", required = false) Boolean status) {
        return service.search(campaignId, campaignName, callTarget, status);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public APIResponse<Page<Campaign>> getCampaignListWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Campaign> campaignsWithPagination = service.findCampaignsWithPagination(offset, pageSize);
        return new APIResponse<>(campaignsWithPagination.getSize(), campaignsWithPagination);
    }

    @GetMapping("/totalCampaigns")
    public Integer getTotalCampaigns() {
        return service.getTotalCampaigns();
    }
}
