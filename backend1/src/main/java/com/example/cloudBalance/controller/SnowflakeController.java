//package com.example.cloudBalance.controller;
//
//import com.example.cloudBalance.dto.UsageAmountResult;
//import com.example.cloudBalance.service.SnowflakeColumnService;
//import jakarta.validation.constraints.NotNull;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/snowflake-columns")
//public class SnowflakeController {
//
//    private final SnowflakeColumnService service;
//
//    public SnowflakeController(SnowflakeColumnService service) {
//        this.service = service;
//    }
//
//    // Get all display names
//    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'READ_ONLY')")
//    @GetMapping("/display-names")
//    public ResponseEntity<List<String>> getDisplayNames() {
//        List<String> displayNames = service.getAllDisplayNames();
//        return ResponseEntity.ok(displayNames);
//    }
//
//    // Get data for a specific display name
//    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'READ_ONLY')")
//    @GetMapping("/data")
//    public ResponseEntity<List<Object>> getDataByDisplayName(@RequestParam String displayName) {
//        List<Object> data = service.getDataByDisplayName(displayName);
//        if (data.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(data);
//    }
//
//    // Get usage amount grouped by the display name
//    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'READ_ONLY')")
//    @PostMapping("/usage")
//    public ResponseEntity<List<UsageAmountResult>> getUsageAmountGroupedBy(
//            @RequestParam String displayName,
//            @RequestParam String monthYear,
//            @RequestParam String linkedAccountId,
//            @RequestParam List<String> selectedFilters,
//            @RequestBody Map<String, List<String>> filterValues
//    ) {
//        List<UsageAmountResult> results = service.getUsageAmountGroupedBy(
//                displayName, monthYear, linkedAccountId, selectedFilters, filterValues
//        );
//
//        if (results.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok(results);
//    }
//}

package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.UsageAmountResult;
import com.example.cloudBalance.dto.ApiResponse;
import com.example.cloudBalance.service.SnowflakeColumnService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/snowflake-columns")
public class SnowflakeController {

    private final SnowflakeColumnService service;

    public SnowflakeController(SnowflakeColumnService service) {
        this.service = service;
    }

    // Get all display names
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'READ_ONLY')")
    @GetMapping("/display-names")
    public ResponseEntity<ApiResponse<List<String>>> getDisplayNames() {
        List<String> displayNames = service.getAllDisplayNames();
        ApiResponse<List<String>> response = new ApiResponse<>(200, "Display names fetched successfully", displayNames);
        return ResponseEntity.ok(response);
    }

    // Get data for a specific display name
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'READ_ONLY')")
    @GetMapping("/data")
    public ResponseEntity<ApiResponse<List<Object>>> getDataByDisplayName(@RequestParam String displayName) {
        List<Object> data = service.getDataByDisplayName(displayName);

        if (data.isEmpty()) {
            ApiResponse<List<Object>> response = new ApiResponse<>(204, "No data found for the given display name", null);
            return ResponseEntity.status(204).body(response);
        }

        ApiResponse<List<Object>> response = new ApiResponse<>(200, "Data fetched successfully", data);
        return ResponseEntity.ok(response);
    }

    // Get usage amount grouped by the display name
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'READ_ONLY')")
    @PostMapping("/usage")
    public ResponseEntity<ApiResponse<List<UsageAmountResult>>> getUsageAmountGroupedBy(
            @RequestParam String displayName,
            @RequestParam String monthYear,
            @RequestParam String linkedAccountId,
            @RequestParam List<String> selectedFilters,
            @RequestBody Map<String, List<String>> filterValues
    ) {
        List<UsageAmountResult> results = service.getUsageAmountGroupedBy(
                displayName, monthYear, linkedAccountId, selectedFilters, filterValues
        );

        if (results.isEmpty()) {
            ApiResponse<List<UsageAmountResult>> response = new ApiResponse<>(204, "No usage data found for the provided criteria", null);
            return ResponseEntity.status(204).body(response);
        }

        ApiResponse<List<UsageAmountResult>> response = new ApiResponse<>(200, "Usage data fetched successfully", results);
        return ResponseEntity.ok(response);
    }
}

