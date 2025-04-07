package com.sns.common.jpa;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.dto.UsersResponseDto;
import com.sns.api.users.domain.entity.Users;
import com.sns.api.users.repository.UsersRepository;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import com.sns.common.exception.CustomException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Users> {

    private final UsersRepository usersRepository;

    private final HttpSession httpSession;

    @Override
    public Optional<Users> getCurrentAuditor() {
        UserBaseDto userDto = (UserBaseDto) httpSession.getAttribute(Const.LOGIN_USER);
        if (userDto == null)
            return null;

        Users user = usersRepository.findById(userDto.getUserId())
                .orElseThrow(() -> new CustomException(ResultCode.AUTHENTICATION_FAILED));

        return Optional.ofNullable(user);
    }
}