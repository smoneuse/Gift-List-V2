package com.scilab.giftslist.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scilab.giftslist.api.ApiConstants;
import com.scilab.giftslist.api.GiftListRootController;
import com.scilab.giftslist.api.PageInputs;
import com.scilab.giftslist.api.user.model.SearchUsersModel;
import com.scilab.giftslist.api.user.model.UserModel;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.services.UserService;
import com.scilab.giftslist.infra.errors.NotFoundException;
import com.scilab.giftslist.infra.security.AuthenticationFacade;

@RestController
@RequestMapping(ApiConstants.API_V1_PREFIX+"/user")
public class UserController extends GiftListRootController {


    @Autowired
    private UserService userService;


    @GetMapping("/search")
    public ResponseEntity<SearchUsersModel> search(@RequestParam(name="filter", defaultValue = "") String filter, @RequestParam(name="page", defaultValue = "0") int page, @RequestParam(name="pageSize", defaultValue = "20" )int pageSize){
        PageInputs paging = new PageInputs(page, pageSize);
        Page<User> searchResult =userService.searchByUserName(filter, paging.page(), paging.pageSize());
        SearchUsersModel response = SearchUsersModel.builder()
            .total(searchResult.getTotalElements())
            .users(searchResult.getContent().stream().map(aUser->{
                return UserModel.builder()
                    .username(aUser.getUsername())
                    .usagename(aUser.usageName())
                    .listCount(aUser.getGiftLists().size())
                    .pictureId(aUser.getProfilePicture().getId())
                    .build();
            }).toList())
            .build();
            return ResponseEntity.ok().body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserModel> profile(){
        User currentUser =userService.findByUsername(AuthenticationFacade.userName()).orElseThrow(()->new NotFoundException("User not fount : "+AuthenticationFacade.userName()));
        UserModel response = UserModel.fromUser(currentUser);
        return ResponseEntity.ok(response);                    
    }

    @PutMapping("/profile")
    public ResponseEntity<UserModel> updateProfile(@RequestBody UserModel updatedProfile){
        updatedProfile.checkInput();
        User updatedUser=userService.updateUser(AuthenticationFacade.userName(), updatedProfile);
        UserModel response = UserModel.fromUser(updatedUser);
        return ResponseEntity.ok(response);     
    }
}
