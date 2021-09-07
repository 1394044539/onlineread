package com.wpy.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author 13940
 * @date 2021/03/02
 */

@Configuration
@Data
@Component
public class FileUploadConfig {

    @Value("${upload.img.rootpath}")
    private String imgRootPath;

    @Value("${upload.img.novelpath}")
    private String novelPath;

    @Value("${upload.img.photopath}")
    private String photoPath;

    @Value("${upload.file.rootpath}")
    private String fileRootPath;
}
