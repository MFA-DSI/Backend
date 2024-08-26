package com.mfa.report.utils;

import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.Role;
import com.mfa.report.endpoint.rest.model.SignUp;
import com.mfa.report.model.Direction;
import com.mfa.report.model.User;

public class UserUtils {
    public static final String USER1_ID = "user1_id";
    public static final String USER2_ID = "user2_id";
    public static final String USER3_ID = "user3_id";
    public static final String USER1_USERNAME = "user1_username";
    public static final String USER2_USERNAME = "user2_username";
    public static final String USER3_USERNAME = "user3_username";
    public static final String USER4_USERNAME = "user4_username";
    public static final String USER1_FIRSTNAME = "user1_firstname";
    public static final String USER2_FIRSTNAME = "user2_firstname";
    public static final String USER3_FIRSTNAME = "user3_firstname";
    public static final String USER1_LASTNAME = "user1_lastname";
    public static final String USER2_LASTNAME = "user2_lastname";
    public static final String USER3_LASTNAME = "user3_lastname";
    public static final String USER1_EMAIL = "user1@gmail.com";
    public static final String USER2_EMAIL = "user2@gmail.com";
    public static final String USER3_EMAIL = "user3@gmail.com";
    public static final String USER4_EMAIL = "user4@gmail.com";
    public static final String USER1_PASSWORD = "vide";
    public static final String USER2_PASSWORD = "vide";
    public static final String USER3_PASSWORD = "vide";
    public static final String USER4_PASSWORD = "vide";
    public static final String USER1_ROLE = "USER";
    public static final String USER2_ROLE = "USER";
    public static final String USER3_ROLE = "ADMIN";
    public  static final String DIRECTION1_ID= "direction1";
    public  static final String DIRECTION2_ID= "direction2";
    public  static final String DIRECTION1_NAME= "DSI";
    public  static final String DIRECTION2_NAME= "BAF";

    public static final String UPDATED_USER3_USERNAME = "user3_name";

    public static Auth auth4() {
        return Auth.builder().email(USER4_EMAIL).password(USER4_PASSWORD).build();
    }

    public static Auth auth1() {
        return Auth.builder().email(USER1_EMAIL).password(USER1_PASSWORD).build();
    }

    public static Direction direction(){
        return Direction.builder().id(DIRECTION1_ID).name(DIRECTION1_NAME).build();
    }

    public static SignUp signUp4() {
        return SignUp.builder()
                .username(USER4_USERNAME)
                .email(USER4_EMAIL)
                .password(USER4_PASSWORD)
                .build();
    }

    public static User user1() {
        return User.builder()
                .id(USER1_ID)
                .username(USER1_USERNAME)
                .firstname(USER1_FIRSTNAME)
                .lastname(USER1_LASTNAME)
                .email(USER1_EMAIL)
                .password(USER1_PASSWORD)
                .role(Role.valueOf(USER1_ROLE))
                .build();
    }

    public static User user2() {
        return User.builder()
                .id(USER2_ID)
                .username(USER2_USERNAME)
                .firstname(USER2_FIRSTNAME)
                .lastname(USER2_LASTNAME)
                .email(USER2_EMAIL)
                .password(USER2_PASSWORD)
                .role(Role.valueOf(USER2_ROLE))
                .build();
    }

    public static User user3() {
        return User.builder()
                .id(USER3_ID)
                .username(USER3_USERNAME)
                .firstname(USER3_FIRSTNAME)
                .lastname(USER3_LASTNAME)
                .email(USER3_EMAIL)
                .password(USER3_PASSWORD)
                .role(Role.valueOf(USER3_ROLE))
                .build();
    }

    public static User updatedUser3() {
        return User.builder()
                .id(USER3_ID)
                .username(UPDATED_USER3_USERNAME)
                .firstname(USER3_FIRSTNAME)
                .lastname(USER3_LASTNAME)
                .email(USER3_EMAIL)
                .password(USER3_PASSWORD)
                .role(Role.valueOf(USER3_ROLE))
                .build();
    }
}
