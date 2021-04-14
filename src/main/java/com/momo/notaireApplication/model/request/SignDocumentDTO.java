package com.momo.notaireApplication.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignDocumentDTO {
    private Long clientId;
    private String location;
}
