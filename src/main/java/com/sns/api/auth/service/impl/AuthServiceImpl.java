package com.sns.api.auth.service.impl;

import com.sns.api.auth.domain.dto.LoginRequestDto;
import com.sns.api.auth.domain.dto.SignupRequestDto;
import com.sns.api.auth.service.AuthService;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.dto.UsersResponseDto;
import com.sns.api.users.domain.entity.Users;
import com.sns.api.users.repository.UsersRepository;
import com.sns.common.component.ResultCode;
import com.sns.common.config.PasswordEncoder;
import com.sns.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("authService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UsersResponseDto signup(SignupRequestDto dto) {
        String email = dto.getEmail();

        Optional<Users> userOpt = usersRepository.findByEmailIncludeDeleted(email); // 이메일 존재 여부 조회
        if (userOpt.isPresent()) {
            throw new CustomException(ResultCode.VALID_FAIL, "이미 사용 중인 이메일입니다.");
        }

        String encodePassword = passwordEncoder.encode(dto.getPassword()); // 비밀번호 암호화

        // User 생성
        Users user = new Users(email, dto.getUsername(), encodePassword, dto.getBirth(), dto.getMbti());

        // User 저장
        Users saveUser = usersRepository.save(user);

        if (saveUser.getId() == null) {
            throw new CustomException(ResultCode.DB_FAIL, "회원 가입에 실패했습니다.");
        }

        return UsersResponseDto.fromEntity(saveUser);
    }

    @Override
    public UserBaseDto login(LoginRequestDto dto) {
        Users findUser = usersRepository.findByEmail(dto.getEmail()) // email로 User 조회
                .orElseThrow(() -> new CustomException(ResultCode.LOGIN_FAILED)); // 없을 경우 throw

        if (!passwordEncoder.matches(dto.getPassword(), findUser.getPassword())) { // 비밀번호 검증
            throw new CustomException(ResultCode.LOGIN_FAILED); // 비밀번호 검증 실패 시 throw
        }

        return UserBaseDto.fromEntity(findUser); // UserBaseDto 로 반환
    }
}
