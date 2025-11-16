package org.example.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.enums.ClaimStatus;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClaimTestCase {
    Map<String, String> product_specs;
    String claim;
    ClaimStatus label;
}

