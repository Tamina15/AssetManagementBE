package com.nashtech.rookies.assetmanagement.dto.request.User;

import com.nashtech.rookies.assetmanagement.util.GenderConstant;
import com.nashtech.rookies.assetmanagement.util.LocationConstant;
import com.nashtech.rookies.assetmanagement.util.PrefixConstant;
import lombok.*;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private int roleId;
    private String firstName;
    private String lastName;
    private GenderConstant gender;
    private LocalDate joinedDate;
    private LocalDate dateOfBirth;
    private LocationConstant location;
    private PrefixConstant prefix;
}