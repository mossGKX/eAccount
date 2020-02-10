package com.ucsmy.eaccount.config.properties;

import lombok.Data;

@Data
public class Redirect {
    private String pageUrl = "";
    private String apiUrl = "";
    private String loginUrl = "";
}
