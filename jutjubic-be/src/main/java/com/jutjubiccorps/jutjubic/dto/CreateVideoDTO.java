package com.jutjubiccorps.jutjubic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateVideoDTO {
    private String title;
    private String description;
    private List<String> tags;
    private MultipartFile thumbnail;
    private MultipartFile videoFile;
    private String location;
}
