package org.example.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ClaimStatus {
    ACCEPT("ACCEPT"),
    REJECT("REJECT");

    String value;

    ClaimStatus(String value) {
        this.value = value;
    }
}
