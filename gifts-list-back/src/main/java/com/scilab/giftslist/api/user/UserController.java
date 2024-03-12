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
import com.scilab.giftslist.api.user.response.SearchUsersResponse;
import com.scilab.giftslist.api.user.response.UserSearchResult;
import com.scilab.giftslist.api.user.shared.UserProfileData;
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
    public ResponseEntity<SearchUsersResponse> search(@RequestParam(name="filter", defaultValue = "") String filter, @RequestParam(name="page", defaultValue = "0") int page, @RequestParam(name="pageSize", defaultValue = "20" )int pageSize){
        PageInputs paging = new PageInputs(page, pageSize);
        Page<User> searchResult =userService.searchByUserName(filter, paging.page(), paging.pageSize());
        SearchUsersResponse response = SearchUsersResponse.builder()
            .total(searchResult.getTotalElements())
            .users(searchResult.getContent().stream().map(aUser->{
                return UserSearchResult.builder()
                    .username(aUser.getUsername())
                    .listCount(aUser.getGiftLists().size())
                    .pictureId(aUser.getProfilePicture().getId())
                    .build();
            }).toList())
            .build();
            return ResponseEntity.ok().body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileData> profile(){
        User currentUser =userService.findByUsername(AuthenticationFacade.userName()).orElseThrow(()->new NotFoundException("User not fount : "+AuthenticationFacade.userName()));
        UserProfileData response = UserProfileData.builder()
                    .firstName(currentUser.getFirstname())
                    .lastName(currentUser.getLastname())
                    .username(currentUser.getUsername())
                    .email(currentUser.getEmail())
                    .pictureId(currentUser.getProfilePicture().getId())
                    .build();
        return ResponseEntity.ok(response);                    
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileData> updateProfile(@RequestBody UserProfileData updatedProfile){
        updatedProfile.checkInput();
        User updatedUser=userService.updateUser(AuthenticationFacade.userName(), updatedProfile);
        UserProfileData response = UserProfileData.builder()
                    .firstName(updatedUser.getFirstname())
                    .lastName(updatedUser.getLastname())
                    .username(updatedUser.getUsername())
                    .email(updatedUser.getEmail())
                    .pictureId(updatedUser.getProfilePicture().getId())
                    .build();
        return ResponseEntity.ok(response);     
    }
}
