package com.scilab.giftslist.domain.user.model;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.scilab.giftslist.domain.lists.model.GiftList;
import com.scilab.giftslist.domain.photo.model.Photo;

import lombok.Builder;
import lombok.Data;

@Document
@Data @Builder
public class User implements UserDetails {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String pwdHash;
    @Indexed(unique = true)
    private String email;
    private String firstname;
    private String lastname;    
    private UserProfile profile;
    private AccountStatus status;
    private String renewPasswordCode;
    @DocumentReference(lazy = true)
    private List<GiftList> giftLists;
    @DocumentReference(lazy = true)
    private List<User> friends;
    @DocumentReference(lazy = true)
    private List<User> friendsRequest;
    @DocumentReference(lazy = true)
    private Photo profilePicture;


    public String usageName(){
        return StringUtils.isAnyBlank(firstname, lastname) ? username : String.format("%s %s", firstname, lastname);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(profile.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    //Required by the authentication manager
    public String getPassword(){
        return this.pwdHash;
    }
}
