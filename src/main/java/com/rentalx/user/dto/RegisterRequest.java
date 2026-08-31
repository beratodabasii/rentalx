package com.rentalx.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Ad alanı boş bırakılamaz")
    private String firstName;
    @NotBlank(message = "Soyad alanı boş bırakılamaz")
    private String lastName;
    @NotBlank(message = "Email alanı boş bırakılamaz")
    @Email(message = "Geçerli bir e-posta formatı giriniz")
    private String email;
    @NotBlank(message = "Şifre boş bırakılamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    private String password;
    @NotBlank(message = "Telefon numarası boş bırakılamaz")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Geçerli bir telefon numarası giriniz")
    private String phoneNumber;

}
