package com.sns.common.init;

import com.sns.api.auth.service.AuthService;
import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.comments.repository.CommentsRepository;
import com.sns.api.follow.domain.entity.Follows;
import com.sns.api.follow.repository.FollowsRepository;
import com.sns.api.friends.domain.entity.Friends;
import com.sns.api.friends.domain.entity.FriendsStatus;
import com.sns.api.friends.repository.FriendsRepository;
import com.sns.api.friends.service.FriendsService;
import com.sns.api.likes.domain.entity.LikeType;
import com.sns.api.likes.domain.entity.Likes;
import com.sns.api.likes.repository.LikesRepository;
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
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DummyDataInit {

    private static final String[] EMAIL_DOMAINS = {"@google.com", "@naver.com", "@kakao.com"};
    private static final String[] USER_FIRST_NAMES = {"김", "박", "유", "안", "정", "전", "이"};

    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;
    private final FriendsRepository friendsRepository;
    private final FollowsRepository followsRepository;
    private final LikesRepository likesRepository;

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

    // 4. 친구 요청 랜덤 생성
    @EventListener(ApplicationReadyEvent.class)
    @Order(4)
    public void initFriends() {
        List<Friends> friends = new ArrayList<>();
        List<Users> users = usersRepository.findAll();
        Set<String> existingFriendPairs = new HashSet<>();

        for (Users user : users) {
            int randomFriendsCount = random.nextInt(5);
            for (int i = 0; i < randomFriendsCount; i++) {
                Users receiverUser = users.get(random.nextInt(users.size()));

                if (user.equals(receiverUser)) {
                    continue;
                }

                String key1 = user.getId() + ":" + receiverUser.getId();
                String key2 = receiverUser.getId() + ":" + user.getId();

                if (existingFriendPairs.contains(key1) || existingFriendPairs.contains(key2)) {
                    continue;
                }
                existingFriendPairs.add(key1);

                Friends friend = Friends.builder()
                        .fromUser(user)
                        .toUser(receiverUser)
                        .status(i % 2 == 0 ? FriendsStatus.ACCEPT : FriendsStatus.PENDING)  // 짝수 -> 친구, 홀수 -> 요청만
                        .build();

                friends.add(friend);
            }
        }
        List<Friends> savedFriends = friendsRepository.saveAll(friends);

        log.info("저장 좋아요 개수: {}", savedFriends.size());
        for (Friends savedFriend : savedFriends) {
            log.info("보낸 친구: {}", savedFriend.getFromUser().getUsername());
            log.info("받은 친구: {}", savedFriend.getFromUser().getUsername());
        }
    }


    // 5. 랜덤한 회원끼리 팔로우 진행
    @EventListener(ApplicationReadyEvent.class)
    @Order(5)
    public void initFollow() {
        List<Follows> follows = new ArrayList<>();
        List<Users> users = usersRepository.findAll();
        Set<String> existingFollowPair = new HashSet<>();

        for (Users follower : users) {
            int randomFollowCount = random.nextInt(5);
            for (int i = 0; i < randomFollowCount; i++) {
                Users randomFollowing = users.get(random.nextInt(users.size()));

                if (follower.equals(randomFollowing)) {
                    continue;
                }

                String key = follower.getId() + ":" + randomFollowing.getId();
                if (existingFollowPair.contains(key)) {
                    continue;
                }

                existingFollowPair.add(key);

                Follows follow = new Follows(randomFollowing);
                follow.setOnlyDummyCreatedBy(follower);

                follows.add(follow);
            }
        }

        List<Follows> savedFollows = followsRepository.saveAll(follows);
        log.info("저장 팔로우 개수: {}", savedFollows.size());
        for (Follows savedFollow : savedFollows) {
            log.info("팔로워: {}", savedFollow.getFollower());
            log.info("팔로잉: {}", savedFollow.getFollowing());
        }

    }

    // 6.랜덤한 댓글, 게시글에 랜덤 회원이 좋아요 클릭
    @EventListener(ApplicationReadyEvent.class)
    @Order(6)
    public void initLike() {
        List<Users> users = usersRepository.findAll();
        List<Posts> posts = postsRepository.findAll();
        List<Comments> comments = commentsRepository.findAll();

        List<Likes> likes = new ArrayList<>();
        Set<String> existingLikeKeys = new HashSet<>();

        // 게시글 좋아요
        for (Posts post : posts) {
            int randomPostLikeCount = random.nextInt(10);
            for (int i = 0; i < randomPostLikeCount; i++) {
                Users randomUser = users.get(random.nextInt(users.size()));
                String key = "게시글" + post.getId() + ":" + randomUser.getId();
                if (existingLikeKeys.contains(key)) {
                    continue;
                }
                existingLikeKeys.add(key);

                Likes like = new Likes(LikeType.POST, post.getId());
                like.setOnlyDummyCreatedBy(randomUser);
                likes.add(like);
            }

        }

        // 댓글 좋아요
        for (Comments comment : comments) {
            int randomCommentLikeCount = random.nextInt(8);
            for (int i = 0; i < randomCommentLikeCount; i++) {
                Users randomUser = users.get(random.nextInt(users.size()));
                String key = "댓글" + comment.getId() + ":" + randomUser.getId();
                if (existingLikeKeys.contains(key)) {
                    continue;
                }
                existingLikeKeys.add(key);

                Likes like = new Likes(LikeType.COMMENT, comment.getId());
                like.setOnlyDummyCreatedBy(randomUser);
                likes.add(like);
            }
        }

        List<Likes> savedLikes = likesRepository.saveAll(likes);
        log.info("저장 좋아요 수: {}", savedLikes.size());
        for (Likes savedLike : savedLikes) {
            log.info("좋아요 타입: {}", savedLike.getLikeType());
            log.info("좋아요 누른 유저 이름: {}", savedLike.getCreatedBy());
        }

    }
}

