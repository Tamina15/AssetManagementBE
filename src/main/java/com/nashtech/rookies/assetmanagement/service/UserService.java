package com.nashtech.rookies.assetmanagement.service;

import com.nashtech.rookies.assetmanagement.dto.UserDetailsDto;
import com.nashtech.rookies.assetmanagement.dto.UserDto;
import com.nashtech.rookies.assetmanagement.dto.request.ChangePasswordRequest;
import com.nashtech.rookies.assetmanagement.dto.request.User.CreateUserRequest;
import com.nashtech.rookies.assetmanagement.dto.request.User.UpdateUserRequest;
import com.nashtech.rookies.assetmanagement.dto.request.User.UserGetRequest;
import com.nashtech.rookies.assetmanagement.dto.response.PageableDto;
import com.nashtech.rookies.assetmanagement.dto.response.ResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
public interface UserService extends UserDetailsService {
    ResponseDto<PageableDto<List<UserDto>>> getAll(UserGetRequest params, Pageable pageable, UserDetailsDto requestUser);
    ResponseDto<UserDto> getUserById(Integer id);
    ResponseDto<UserDto> getUserByStaffCode(String staffCode);
    ResponseDto<UserDto> saveUser(CreateUserRequest request, UserDetailsDto requestUser);
    ResponseDto<UserDto> updateUser(UpdateUserRequest request, String staffCode);
    ResponseDto<Void> changePassword(Integer userId, ChangePasswordRequest request);
    ResponseDto<Void> disableUser(String staffCode);
}
