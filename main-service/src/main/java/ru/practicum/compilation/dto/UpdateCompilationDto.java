package ru.practicum.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCompilationDto {
    @Size(max = 50, min = 1, message = "The title must be between 1 and 50 characters long")
    private String title;
    @Builder.Default
    private boolean pinned = false;
    private Set<Integer> events;
}
