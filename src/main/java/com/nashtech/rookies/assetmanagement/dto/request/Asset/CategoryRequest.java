package com.nashtech.rookies.assetmanagement.dto.request.Asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private String name;
    private String prefix;
}
