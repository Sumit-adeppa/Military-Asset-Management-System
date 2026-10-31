package com.example.demo.controller;

import com.example.demo.dto.request.BaseRequest;
import com.example.demo.dto.response.BaseResponse;
import com.example.demo.entity.Base;
import com.example.demo.service.BaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bases")
public class BaseController {

    private final BaseService baseService;

    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createBase(@Valid @RequestBody BaseRequest request) {
        Base base = baseService.createBase(request.getName(), request.getLocation());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(base));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getBaseById(@PathVariable Long id) {
        Base base = baseService.getBaseById(id);
        return ResponseEntity.ok(toResponse(base));
    }

    @GetMapping
    public ResponseEntity<List<BaseResponse>> getAllBases() {
        return ResponseEntity.ok(baseService.getAllBases().stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateBase(@PathVariable Long id,
                                                   @RequestBody BaseRequest request) {
        Base base = baseService.updateBase(id, request.getName(), request.getLocation());
        return ResponseEntity.ok(toResponse(base));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBase(@PathVariable Long id) {
        baseService.deleteBase(id);
        return ResponseEntity.noContent().build();
    }

    private BaseResponse toResponse(Base base) {
        return new BaseResponse(base.getId(), base.getName(), base.getLocation());
    }
}
