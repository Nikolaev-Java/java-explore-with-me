package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NewUserRequestDto {
    @NotBlank(message = "Name must be not blank")
    @Size(min = 2, max = 250)
    private String name;
    @Email(message = "Incorrect email")
    @Size(min = 6, max = 254)
    @NotNull
    private String email;
}
