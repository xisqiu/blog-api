package com.six.yoblog.vo.params;

import lombok.Data;


@Data
public class PageParams {

    // 初始化 默认的页码 1
    private int page=1;

    // 初始化 默认的每页数据 10
    private  int pagesize=10;


}
