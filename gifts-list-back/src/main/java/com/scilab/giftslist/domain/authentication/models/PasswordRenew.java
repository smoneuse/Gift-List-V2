package com.scilab.giftslist.domain.authentication.models;

public record PasswordRenew(String usernameOrEmail, String renewCode, String newPassord) {

}
