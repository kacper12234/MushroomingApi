package com.example.mushrooming.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@UtilityClass
public class Constants {

    public static final String ACTIVATION_EMAIL =  ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/api/auth/accountVerification";
    public static final String PASS = "YouWillNotGuess";
}
