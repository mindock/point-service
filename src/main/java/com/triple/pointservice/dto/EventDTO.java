package com.triple.pointservice.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class EventDTO {
    private static final String ID_REGEX = "([a-z0-9]){8}-([a-z0-9]){4}-([a-z0-9]){4}-([a-z0-9]){4}-([a-z0-9]){12}";

    @NotBlank
    @Pattern(regexp = ID_REGEX)
    private String type;
    @NotBlank
    private String action;
    @NotBlank
    @Pattern(regexp = ID_REGEX)
    private String reviewId;
    @NotBlank
    private String content;
    @Size(max = 20)
    private List<@Pattern(regexp = ID_REGEX) String> attachedPhotoIds;
    @NotBlank
    @Pattern(regexp = ID_REGEX)
    private String userId;
    @NotBlank
    @Pattern(regexp = ID_REGEX)
    private String placeId;
}
