package com.scilab.giftslist.api.offering;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scilab.giftslist.api.ApiConstants;
import com.scilab.giftslist.api.GiftListRootController;
import com.scilab.giftslist.api.PageInputs;
import com.scilab.giftslist.api.offering.model.OfferingModel;
import com.scilab.giftslist.api.offering.model.OfferingPageModel;
import com.scilab.giftslist.domain.offering.model.Offering;
import com.scilab.giftslist.domain.offering.service.OfferingService;
import com.scilab.giftslist.infra.security.AuthenticationFacade;

@RestController
@RequestMapping(ApiConstants.API_V1_PREFIX+"/offering")
public class OfferingController extends GiftListRootController {

    @Autowired
    private OfferingService offeringService;


    @GetMapping("/recieved")
    public ResponseEntity<OfferingPageModel> allRecieved(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value="pageSize", defaultValue = "20") int pageSize,
                                                        @RequestParam(value="giftTitleFilter", defaultValue = "") String giftTitle,
                                                        @RequestParam(value="sortBy", defaultValue = "givenDate") String sortBy,
                                                        @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        PageInputs pageSettings = new PageInputs(page, pageSize);
        Page<Offering> offerings =offeringService.allOfferedTo(AuthenticationFacade.userName(), giftTitle, pageSettings, sortBy, direction);
        OfferingPageModel response = OfferingPageModel.builder()
            .total(offerings.getTotalElements())
            .page(offerings.getNumber())
            .offerings(offerings.getContent().stream().map(OfferingModel::fromOffering).toList())
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recieved/{giver}")
    public ResponseEntity<OfferingPageModel> allRecievedBy(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value="pageSize", defaultValue = "20") int pageSize,
                                                        @PathVariable("giver") String giver,
                                                        @RequestParam(value="giftTitleFilter", defaultValue = "") String giftTitle,
                                                        @RequestParam(value="sortBy", defaultValue = "givenDate") String sortBy,
                                                        @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        PageInputs pageSettings = new PageInputs(page, pageSize);
        Page<Offering> offerings =offeringService.allOfferedByTo(giver, AuthenticationFacade.userName(), giftTitle, pageSettings, sortBy, direction);
        OfferingPageModel response = OfferingPageModel.builder()
            .total(offerings.getTotalElements())
            .page(offerings.getNumber())
            .offerings(offerings.getContent().stream().map(OfferingModel::fromOffering).toList())
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recievedOnList")
    public ResponseEntity<OfferingPageModel> allRecievedOnList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value="pageSize", defaultValue = "20") int pageSize,
                                                        @RequestParam(value="giftTitleFilter", defaultValue = "") String giftTitle,
                                                        @RequestParam(value = "listTitle") String listTitle,
                                                        @RequestParam(value="sortBy", defaultValue = "givenDate") String sortBy,
                                                        @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        PageInputs pageSettings = new PageInputs(page, pageSize);
        Page<Offering> offerings =offeringService.allOfferedToWithListTitle(AuthenticationFacade.userName(), giftTitle, listTitle, pageSettings, sortBy, direction);
        OfferingPageModel response = OfferingPageModel.builder()
            .total(offerings.getTotalElements())
            .page(offerings.getNumber())
            .offerings(offerings.getContent().stream().map(OfferingModel::fromOffering).toList())
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recievedOnList/{giver}")
    public ResponseEntity<OfferingPageModel> allRecievedByOnList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value="pageSize", defaultValue = "20") int pageSize,
                                                        @PathVariable("giver") String giver,
                                                        @RequestParam(value="giftTitleFilter", defaultValue = "") String giftTitle,
                                                        @RequestParam(value = "listTitle") String listTitle,
                                                        @RequestParam(value="sortBy", defaultValue = "givenDate") String sortBy,
                                                        @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        PageInputs pageSettings = new PageInputs(page, pageSize);
       Page<Offering> offerings =offeringService.allOfferedByToWithListTitle(AuthenticationFacade.userName(), giver, giftTitle, listTitle, pageSettings, sortBy, direction);
        OfferingPageModel response = OfferingPageModel.builder()
            .total(offerings.getTotalElements())
            .page(offerings.getNumber())
            .offerings(offerings.getContent().stream().map(OfferingModel::fromOffering).toList())
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offered")
    public ResponseEntity<OfferingPageModel> allOffered(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value="pageSize", defaultValue = "20") int pageSize,
                                                        @RequestParam(value="giftTitleFilter", defaultValue = "") String giftTitle,
                                                        @RequestParam(value="sortBy", defaultValue = "givenDate") String sortBy,
                                                        @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        PageInputs pageSettings = new PageInputs(page, pageSize);
        Page<Offering> offerings =offeringService.allOfferedBy(AuthenticationFacade.userName(), giftTitle, pageSettings, sortBy, direction);
        OfferingPageModel response = OfferingPageModel.builder()
            .total(offerings.getTotalElements())
            .page(offerings.getNumber())
            .offerings(offerings.getContent().stream().map(OfferingModel::fromOffering).toList())
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offered/{reciever}")
    public ResponseEntity<OfferingPageModel> allOfferedTo(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value="pageSize", defaultValue = "20") int pageSize,
                                                        @PathVariable("reciever") String reciever,
                                                        @RequestParam(value="giftTitleFilter", defaultValue = "") String giftTitle,
                                                        @RequestParam(value="sortBy", defaultValue = "givenDate") String sortBy,
                                                        @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        PageInputs pageSettings = new PageInputs(page, pageSize);
        Page<Offering> offerings =offeringService.allOfferedByTo(AuthenticationFacade.userName(), reciever, giftTitle, pageSettings, sortBy, direction);
        OfferingPageModel response = OfferingPageModel.builder()
            .total(offerings.getTotalElements())
            .page(offerings.getNumber())
            .offerings(offerings.getContent().stream().map(OfferingModel::fromOffering).toList())
            .build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/offeredOnList/{reciever}")
    public ResponseEntity<OfferingPageModel> allOffered(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value="pageSize", defaultValue = "20") int pageSize,
                                                        @PathVariable("reciever") String reciever,
                                                        @RequestParam(value="giftTitleFilter", defaultValue = "") String giftTitle,                                                        
                                                        @RequestParam(value = "listTitle") String listTitle,
                                                        @RequestParam(value="sortBy", defaultValue = "givenDate") String sortBy,
                                                        @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        PageInputs pageSettings = new PageInputs(page, pageSize);
        Page<Offering> offerings =offeringService.allOfferedByToWithListTitle(AuthenticationFacade.userName(), reciever, giftTitle, listTitle, pageSettings, sortBy, direction);
        OfferingPageModel response = OfferingPageModel.builder()
            .total(offerings.getTotalElements())
            .page(offerings.getNumber())
            .offerings(offerings.getContent().stream().map(OfferingModel::fromOffering).toList())
            .build();
        return ResponseEntity.ok(response);
    }  
}
