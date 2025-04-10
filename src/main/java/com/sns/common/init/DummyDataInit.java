package com.sns.common.init;

import com.sns.api.auth.service.AuthService;
import com.sns.api.comments.service.CommentsService;
import com.sns.api.friends.service.FriendsService;
import com.sns.api.posts.service.PostsService;
import com.sns.api.users.domain.entity.MBTI;
import com.sns.api.users.domain.entity.Users;
import com.sns.api.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DummyDataInit {

    private static final String[] EMAIL_DOMAINS = {"@google.com", "@naver.com", "@kakao.com"};
    private static final String[] USER_FIRST_NAMES = {"김", "박", "유", "안", "정", "전", "이"};

    private final AuthService authService;
    private final UsersRepository usersRepository;

    private final PostsService postsService;
    private final CommentsService commentsService;
    private final FriendsService friendsService;

    Random random = new Random();
    Faker koFaker = new Faker((new Locale("ko")));

    // 1. 회원 25개 생성
    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void initUsers() {
        List<Users> users = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {

            int randomEmailDomain = random.nextInt(EMAIL_DOMAINS.length);
            int randomFirstName = random.nextInt(USER_FIRST_NAMES.length);
            String email = "test" + i + EMAIL_DOMAINS[randomEmailDomain];
            String username = USER_FIRST_NAMES[randomFirstName] + koFaker.name().firstName();

            int year = 1985 + random.nextInt(20);
            int month = 1 + random.nextInt(12);
            int day = 1 + random.nextInt(28);
            String birthStr = LocalDate.of(year, month, day).format(DateTimeFormatter.ISO_DATE);
            String mbti = MBTI.values()[random.nextInt(MBTI.values().length)].name();

            users.add(new Users(
                    email,
                    username,
                    "qwer1234!@#$",
                    birthStr,
                    mbti
            ));
        }

        List<Users> saveUsers = usersRepository.saveAll(users);
        log.info("저장 유저 개수: {}", saveUsers.size());
        for (Users saveUser : saveUsers) {
            log.info("저장된 유저 이메일: {}", saveUser.getEmail());
            log.info("저장된 유저 이름: {}", saveUser.getUsername());
        }
    }

    // todo: 그다음 게시글 추가 랜덤 회원이 45개(안쓴 회원도 있음)
    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    public void initPosts() {

    }


    // todo: 그다음 게시글의 댓글 작성, 게시글마다 0 ~ 30개 랜덤 회원이 작성
    @EventListener(ApplicationReadyEvent.class)
    @Order(3)
    public void initComments() {

    }

    // todo: 그다음 회원의 친구 상태(랜덤 회원이 0 ~ 여러 회원에게 친구 요청 전송 및 수락 진행)
    @EventListener(ApplicationReadyEvent.class)
    @Order(4)
    public void initFriends() {

    }


    // todo: 그다음 팔로우 상태(랜덤 회원이 0 ~ 여러 회원에게 팔로우)
    @EventListener(ApplicationReadyEvent.class)
    @Order(5)
    public void initFollow() {

    }

    // todo: 그다음 좋아요 상태(랜덤 회원이 0 ~ 여러 회원에게 팔로우)
    @EventListener(ApplicationReadyEvent.class)
    @Order(6)
    public void initLike() {

    }
}

