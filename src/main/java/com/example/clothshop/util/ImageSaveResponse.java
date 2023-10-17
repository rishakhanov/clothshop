package com.example.clothshop.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageSaveResponse {
    private boolean success;
    private String filename;
    private String link;
}
