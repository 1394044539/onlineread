package com.wpy.dto;

import lombok.Data;

/**
 * @author 13940
 * @date
 */
@Data
public abstract class AbstractSplitPageDto {

    private Integer pageNum=1;

    private Integer pageSize=10;

    private Boolean asc=false;
}
