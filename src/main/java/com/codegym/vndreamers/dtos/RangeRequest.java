package com.codegym.vndreamers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RangeRequest {
    @JsonProperty(value = "start_date")
    private LocalDate startDate;
    @JsonProperty(value = "end_date")
    private LocalDate endDate;
}
