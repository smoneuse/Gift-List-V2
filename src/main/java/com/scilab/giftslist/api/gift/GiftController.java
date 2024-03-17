package com.scilab.giftslist.api.gift;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scilab.giftslist.api.ApiConstants;
import com.scilab.giftslist.api.ApiOperationStatus;
import com.scilab.giftslist.api.GiftListRootController;
import com.scilab.giftslist.api.PageInputs;
import com.scilab.giftslist.api.gift.models.GiftModel;
import com.scilab.giftslist.api.gift.models.GiftPage;
import com.scilab.giftslist.api.shared.ApiResponse;
import com.scilab.giftslist.domain.gift.Gift;
import com.scilab.giftslist.domain.gift.GiftStatus;
import com.scilab.giftslist.domain.gift.services.GiftService;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.security.AuthenticationFacade;

@RestController
@RequestMapping(ApiConstants.API_V1_PREFIX+"/gift")
public class GiftController extends GiftListRootController{

    @Autowired
    private GiftService giftService;

    @GetMapping("/userlist")
    public ResponseEntity<GiftPage> userListGifts(@RequestParam("listId") String listId, @RequestParam(name="page", defaultValue = "0") int page, @RequestParam(name="pageSize", defaultValue = "20") int pageSize, @RequestParam(name="sort", defaultValue = "lastUpdate") String sort,@RequestParam(name="direction", defaultValue = "desc") String direction){
        if(StringUtils.isBlank(listId)){throw new BadRequestException("Can't find list gifts without list Id");}
        PageInputs paging = new PageInputs(page, pageSize);
        //A user can see all (reserved and available) gifts in his own lists
        Page<Gift> giftsPage= giftService.findUserGiftsInListWithStatus(AuthenticationFacade.userName(), listId, paging, sort, direction, GiftStatus.AVAILABLE, GiftStatus.RESERVED);
        GiftPage response  = GiftPage.builder()
            .total(giftsPage.getTotalElements())
            .gifts(giftsPage.getContent().stream().map(GiftModel::fromGift).toList())
            .build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/friendlist")
    public ResponseEntity<GiftPage> friendListGifts(@RequestParam("listId") String listId, @RequestParam(name="page", defaultValue = "0") int page, @RequestParam(name="pageSize", defaultValue = "20") int pageSize, @RequestParam(name="sort", defaultValue = "lastUpdate") String sort,@RequestParam(name="direction", defaultValue = "desc") String direction){
        if(StringUtils.isBlank(listId)){throw new BadRequestException("Can't find list gifts without list Id");}
        PageInputs paging = new PageInputs(page, pageSize);
        Page<Gift> giftsPage = giftService.findFriendGiftsInList(AuthenticationFacade.userName(), listId, paging,sort, direction);
        GiftPage response  = GiftPage.builder()
        .total(giftsPage.getTotalElements())
        .gifts(giftsPage.getContent().stream().map(GiftModel::fromGift).toList())
        .build();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/add/{listId}")
    public ResponseEntity<GiftModel> addGiftToList(@RequestBody GiftModel model, @PathVariable("listId") String listId){
        if(model == null || StringUtils.isAnyBlank(model.getTitle(), listId)){
            throw new BadRequestException("Can't create a gift without title or list identifier");
        }
        GiftModel response = GiftModel.fromGift(giftService.addGiftToList(AuthenticationFacade.userName(), listId, model));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{listId}")
    public ResponseEntity<ApiOperationStatus> deleteGift(@RequestBody GiftModel model, @PathVariable("listId") String listId){
        if(model == null || StringUtils.isAnyBlank(model.getId(),listId)){
            throw new BadRequestException("Can't delete a gift without ID or list identifier");
        }
        giftService.removeGiftFromList(AuthenticationFacade.userName(), listId, model);
        return ResponseEntity.ok(ApiOperationStatus.OK);
    }

    @PutMapping("/update/{listId}")
    public ResponseEntity<GiftModel> updateGift(@RequestBody GiftModel model, @PathVariable("listId") String listId){
        if(model == null || StringUtils.isAnyBlank(model.getId(),listId)){
            throw new BadRequestException("Can't update a gift without ID or list identifier");
        }
        GiftModel response = GiftModel.fromGift(giftService.updateGift(AuthenticationFacade.userName(), listId, model));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reserve/{listId}")
    public ResponseEntity<GiftModel> reserveGift(@RequestBody GiftModel model, @PathVariable("listId") String listId){
        if(model == null || StringUtils.isAnyBlank(model.getId(),listId)){
            throw new BadRequestException("Can't reserve a gift without ID or list identifier");
        }
        GiftModel response = GiftModel.fromGift(giftService.reserve(AuthenticationFacade.userName(), listId, model));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cancel/{listId}")
    public ResponseEntity<GiftModel> cancelGiftReservation(@RequestBody GiftModel model, @PathVariable("listId") String listId){
        if(model == null || StringUtils.isAnyBlank(model.getId(),listId)){
            throw new BadRequestException("Can't cancel a gift reservation without ID or list identifier");
        }
        GiftModel response = GiftModel.fromGift(giftService.cancelReservation(AuthenticationFacade.userName(), listId, model));
        return ResponseEntity.ok(response);
    }
}
