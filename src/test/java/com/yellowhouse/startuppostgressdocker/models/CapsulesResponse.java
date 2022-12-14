package com.yellowhouse.startuppostgressdocker.models;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class CapsulesResponse {
    private UUID id;
    private String type;
    private int price;
    private int size;
    private String clothesSize;
    private String status;
    private Set<UUID> clothesInCapsulaIds;
}
