package com.momo.notaireApplication.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JWTResponse {

    private String token;

    private String type = "Bearer";
}
