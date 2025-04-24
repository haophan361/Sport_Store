package com.sport_store.DTO.response.color_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class color_Response {
    private int color_id;
    private String color;
    List<String> image_url;
}
