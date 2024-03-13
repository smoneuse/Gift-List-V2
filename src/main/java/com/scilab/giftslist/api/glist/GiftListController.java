package com.scilab.giftslist.api.glist;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scilab.giftslist.api.ApiConstants;
import com.scilab.giftslist.api.GiftListRootController;
import com.scilab.giftslist.api.PageInputs;
import com.scilab.giftslist.api.glist.model.GiftListModel;
import com.scilab.giftslist.api.glist.model.GiftListPage;
import com.scilab.giftslist.api.shared.ApiResponse;
import com.scilab.giftslist.domain.gift.GiftStatus;
import com.scilab.giftslist.domain.gift.services.GiftService;
import com.scilab.giftslist.domain.lists.model.GiftList;
import com.scilab.giftslist.domain.lists.service.GiftListService;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.security.AuthenticationFacade;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(ApiConstants.API_V1_PREFIX+"/giftlist")
public class GiftListController extends GiftListRootController{

    @Autowired
    private GiftListService giftListService;
    @Autowired
    private GiftService giftService;
    
    @GetMapping
    public ResponseEntity<GiftListPage> userLists(@RequestParam(name = "page", defaultValue = "0") int page,@RequestParam(name="pageSize", defaultValue = "20") int pageSize, @RequestParam(name="sort", defaultValue = "name") String sort){
        PageInputs paging = new PageInputs(page, pageSize);
        Page<GiftList> userGiftLists = giftListService.userLists(AuthenticationFacade.userName(), paging, sort);
        GiftListPage response = GiftListPage.builder()
                .total(userGiftLists.getTotalElements())
                .giftLists(userGiftLists.getContent().stream().map(aList->{
                        return modelForOwner(aList);
                    }).toList())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/friend/{friendId}")
    public ResponseEntity<GiftListPage> friendList(@PathVariable("friendId") String friendId, @RequestParam(name = "page", defaultValue = "0") int page,@RequestParam(name="pageSize", defaultValue = "20") int pageSize, @RequestParam(name="sort", defaultValue = "name") String sort){
        if(StringUtils.isBlank(friendId)){  throw new BadRequestException("Can't list friend's list without friend ID"); }
        PageInputs paging = new PageInputs(page, pageSize);
        Page<GiftList> friendLists = giftListService.friendLists(AuthenticationFacade.userName(), friendId, paging, sort);
        GiftListPage response = GiftListPage.builder()
                .total(friendLists.getTotalElements())
                .giftLists(friendLists.getContent().stream().map(aList->{
                        return modelForFriend(aList);
                    }).toList())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<GiftListModel> createList(@RequestBody GiftListModel model) {
        if(model==null || StringUtils.isBlank(model.getTitle())){
            throw new BadRequestException("Can't create new list without title");
        }
        GiftListModel response = modelForOwner(giftListService.createList(AuthenticationFacade.userName(), model));
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<GiftListModel> updateList(@RequestBody GiftListModel model){
        if(model==null || StringUtils.isAnyBlank(model.getTitle(), model.getId())){
            throw new BadRequestException("Can't update new list without title or ID");
        }
        return ResponseEntity.ok(modelForOwner(giftListService.updateList(AuthenticationFacade.userName(), model)));
    }

    @DeleteMapping()
    ResponseEntity<ApiResponse> deleteGiftList(@RequestParam("listId") String giftListId){
        if(StringUtils.isBlank(giftListId)){
            throw new BadRequestException("Can't delete a list without ID");
        }
        return ResponseEntity.ok(giftListService.deleteGiftList(AuthenticationFacade.userName(), giftListId));
    }


    
    private GiftListModel modelForOwner(GiftList giftList){
        return GiftListModel.builder()
        .title(giftList.getName())
        .id(giftList.getId())
        .description(giftList.getDescription())
        .lastUpdate(giftList.getLastUpdate())
        .availableGifts(giftService.countGiftsInList(giftList, GiftStatus.AVAILABLE, GiftStatus.RESERVED))
        .reservedGifts(BigDecimal.ZERO.intValue())
        .pictureId(giftList.getPicture() == null ? StringUtils.EMPTY : giftList.getPicture().getId())
        .build();
    }

    private GiftListModel modelForFriend(GiftList giftList){
        return GiftListModel.builder()
        .title(giftList.getName())
        .id(giftList.getId())
        .description(giftList.getDescription())
        .lastUpdate(giftList.getLastUpdate())
        .availableGifts(giftService.countGiftsInList(giftList, GiftStatus.AVAILABLE))
        .reservedGifts(giftService.countUserReservedGiftsInList(giftList, AuthenticationFacade.userName()))
        .pictureId(giftList.getPicture() == null ? StringUtils.EMPTY : giftList.getPicture().getId())
        .build();
    }
}
