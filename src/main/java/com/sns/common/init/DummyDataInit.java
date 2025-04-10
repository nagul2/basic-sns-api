package com.sns.common.init;

import com.sns.api.auth.service.AuthService;
import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.comments.repository.CommentsRepository;
import com.sns.api.friends.service.FriendsService;
import com.sns.api.posts.domain.entity.Posts;
import com.sns.api.posts.repository.PostsRepository;
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
    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;
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

        List<Users> savedUsers = usersRepository.saveAll(users);
        log.info("저장 유저 개수: {}", savedUsers.size());
        for (Users savedUser : savedUsers) {
            log.info("저장된 유저 이메일: {}", savedUser.getEmail());
            log.info("저장된 유저 이름: {}", savedUser.getUsername());
        }
    }

    // 2. 게시글 추가 랜덤 회원이 35개 작성
    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    public void initPosts() {
        List<Posts> posts = new ArrayList<>();
        List<Users> users = usersRepository.findAll();

        for (int i = 0; i < 103; i++) {
            int randomUserId = random.nextInt(users.size());
            Users randomUser = users.get(randomUserId);
            Posts post = Posts.of("dummy 데이터 입니다" + (i + 1));
            post.setOnlyDummyCreatedBy(randomUser);
            post.setOnlyDummyModifiedBy(randomUser);

            posts.add(post);
        }

        List<Posts> savedPosts = postsRepository.saveAll(posts);

        log.info("저장 게시글 개수: {}", savedPosts.size());
        for (Posts savedPost : savedPosts) {
            log.info("저장된 게시글 내용: {}", savedPost.getContent());
            log.info("게시글 유저 이름: {}", savedPost.getCreatedBy().getUsername());
        }
    }

    // 3: 특정 게시글의 댓글 작성, 게시글마다 0 ~ 30개, 랜덤 회원이 작성
    @EventListener(ApplicationReadyEvent.class)
    @Order(3)
    public void initComments() {
        List<Comments> comments = new ArrayList<>();
        List<Users> users = usersRepository.findAll();
        List<Posts> posts = postsRepository.findAll();

        for (Posts post : posts) {
            int randomCommentQuantity = random.nextInt(31);

            for (int i = 0; i < randomCommentQuantity; i++) {
                Users randomUser = users.get(random.nextInt(users.size()));
                Comments comment = Comments.of(post, post.getId() + "번 게시글의 댓글 " + (i + 1));

                comment.setOnlyDummyCreatedBy(randomUser);
                comment.setOnlyDummyModifiedBy(randomUser);

                comments.add(comment);
            }
        }

        List<Comments> savedComments = commentsRepository.saveAll(comments);

        log.info("저장 댓글 개수: {}", savedComments.size());
        for (Comments savedComment : savedComments) {
            log.info("게시글 번호: {}", savedComment.getPost().getId());
            log.info("저장된 댓글 내용: {}", savedComment.getContent());
            log.info("댓글 유저 이름: {}", savedComment.getCreatedBy().getUsername());
        }
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

