package com.six.yoblog.vo;

import lombok.Data;

@Data
public class LoginUserVO {
    private Long id;

    private String account;

    private String nickname;

    private String avatar;
}
