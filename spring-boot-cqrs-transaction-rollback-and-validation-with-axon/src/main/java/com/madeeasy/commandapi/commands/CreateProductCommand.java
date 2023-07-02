package com.madeeasy.commandapi.commands;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Data
@Builder
public class CreateProductCommand {

    @TargetAggregateIdentifier
    @NotNull
    private String productId;
    @NotBlank
    private String name;
    @DecimalMin("0.0")
    private BigDecimal price;
    @Min(0)
    private Integer quantity;
}
