package spring.security.init.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import spring.security.init.entity.UserType;

@Slf4j
@Builder
@Getter
@ToString
public class ResponseUserSignInDto {
    private String username;
    private UserType type;
}
