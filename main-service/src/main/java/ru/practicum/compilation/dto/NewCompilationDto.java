package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
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
public class NewCompilationDto {
    private long id;
    @NotBlank(message = "Title must be not blank")
    @Size(max = 50, min = 1, message = "The title must be between 1 and 51 characters long")
    private String title;
    @Builder.Default
    private boolean pinned = false;
    private Set<Integer> events;
}
