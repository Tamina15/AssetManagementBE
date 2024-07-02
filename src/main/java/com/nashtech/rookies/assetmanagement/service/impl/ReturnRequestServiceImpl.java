/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nashtech.rookies.assetmanagement.service.impl;

import com.nashtech.rookies.assetmanagement.dto.UserDetailsDto;
import com.nashtech.rookies.assetmanagement.dto.request.ReturnRequest.ReturnRequestRequestDTO;
import com.nashtech.rookies.assetmanagement.dto.response.PageableDto;
import com.nashtech.rookies.assetmanagement.dto.response.ResponseDto;
import com.nashtech.rookies.assetmanagement.dto.response.ReturnRequestResponseDTO;
import com.nashtech.rookies.assetmanagement.entity.ReturnRequest;
import com.nashtech.rookies.assetmanagement.entity.ReturnRequest_;
import com.nashtech.rookies.assetmanagement.repository.ReturnRequestRepository;
import com.nashtech.rookies.assetmanagement.service.ReturnRequestService;
import com.nashtech.rookies.assetmanagement.specifications.ReturnRequestSpecification;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 * @author Tamina
 */
@Service
@AllArgsConstructor
public class ReturnRequestServiceImpl implements ReturnRequestService {

    public ReturnRequestRepository repository;

    @Override
    public ResponseDto getAll(ReturnRequestRequestDTO requestParams, Pageable pageable, UserDetailsDto requestUser) {
        Specification<ReturnRequest> specs = ReturnRequestSpecification.filterSpecs(requestUser.getLocation(), requestParams.getSearch(), requestParams.getReturnedDate(), requestParams.getState());
        PageRequest pageRequest = (PageRequest) pageable;
        if (pageRequest.getSort().equals(Sort.unsorted())) {
            pageRequest = pageRequest.withSort(Sort.Direction.ASC, ReturnRequest_.ID);
        }
        Page<ReturnRequest> returnRequest = repository.findAll(specs,pageRequest);
        List<ReturnRequestResponseDTO> list = new ArrayList();
        for (ReturnRequest rr : returnRequest) {
            ReturnRequestResponseDTO response = new ReturnRequestResponseDTO(
                    rr.getAssignment().getAsset().getAssetCode(),
                    rr.getAssignment().getAsset().getName(),
                    rr.getAuditMetadata().getCreatedBy().getUsername(),
                    rr.getAuditMetadata().getCreatedOn().toLocalDate().toString(),
                    rr.getAcceptedBy().getUsername(),
                    rr.getReturnedDate().toString(),
                    rr.getStatus());
            list.add(response);
        }
        PageableDto page = new PageableDto(list, returnRequest.getNumber(), returnRequest.getTotalPages(), returnRequest.getTotalElements());
        return ResponseDto.builder().data(page).message("Get All Return Request Succesfully").build();
    }

}
