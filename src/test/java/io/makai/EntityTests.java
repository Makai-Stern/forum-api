package io.makai;

import io.makai.entity.CommentEntity;
import io.makai.entity.PostEntity;
import io.makai.entity.UserEntity;
import io.makai.repository.CommentRepository;
import io.makai.repository.PostRepository;
import io.makai.repository.RoleRepository;
import io.makai.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.makai.enums.RoleType.ROLE_USER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class EntityTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void contextLoads() {
    }

    public UserEntity createUser() {
        String uuidStr = UUID.randomUUID().toString();
        String username = "test_user_" + uuidStr;
        String password = bCryptPasswordEncoder.encode("123456");

        UserEntity user = new UserEntity(username, password);
        user.setAuthorities(List.of(this.roleRepository.findByName(ROLE_USER.name())));
        UserEntity savedUser = userRepository.save(user);

        System.out.println(savedUser);

        return savedUser;
    }

    @Test
    public void createUserTest() {
        UserEntity user = createUser();
        assert user.getId() != null;
    }

    public Map<String, Object> createPost(int numOfPosts) {

        Map<String, Object> data = new HashMap<>();

        UserEntity user = createUser();

        String title = "Test Post by " + user.getUsername();
        String body = "Post body by "+ user.getUsername();

        for (int i = 0; i < numOfPosts; i++) {
            String key = "post" + (i + 1);
            PostEntity post = postRepository.save(new PostEntity(title + " - post " + i, body, user));
            user.getPosts().add(post);

            data.put(key, post);
        }

        UserEntity savedUser = userRepository.save(user);
        data.put("user", savedUser);

        return data;
    }

    @Test
    public void createPostTest() {
        int numOfPosts = 12;
        Map<String, Object> map = createPost(12);

        UserEntity user = (UserEntity) map.get("user");

        for (int i = 0; i < numOfPosts; i++) {
            String key = "post" + i + 1;
            PostEntity post = (PostEntity) map.get(key);
            assert post.getId() != null;
        }

        assert user.getPosts().size() != 0;
    }

    @Test
    public void getUserPosts() {

        int numOfPosts = 11;

        Map<String, Object> map = createPost(numOfPosts);
        UserEntity user = (UserEntity) map.get("user");

        int pageNum = 0;
        int pageSize = 10;

        Pageable pageable1 = PageRequest.of(pageNum, pageSize, Sort.by("created_at").descending());
        List<PostEntity> page1 = postRepository.findByUserId(user.getId(), pageable1);
        assert page1.size() == pageSize;

        Pageable pageable2 = PageRequest.of(pageNum + 1, pageSize, Sort.by("created_at").descending());
        List<PostEntity> page2 = postRepository.findByUserId(user.getId(), pageable2);
        assert page2.size() == 1;
    }

    public CommentEntity createComment(UserEntity userEntity, PostEntity postEntity) {

        String body = "Comment created by " + userEntity.getUsername() + " at " + LocalTime.now();
        CommentEntity comment = commentRepository.save(new CommentEntity(body, postEntity, userEntity));

        postEntity.getComments().add(comment);
        postRepository.save(postEntity);

        userEntity.getComments().add(comment);
        userRepository.save(userEntity);

        return comment;
    }

    @Test
    public void createCommentTest() {

        int numOfPosts = 1;

        Map<String, Object> map = createPost(numOfPosts);
        UserEntity user = (UserEntity) map.get("user");
        PostEntity post = (PostEntity) map.get("post" + numOfPosts);

        CommentEntity comment = createComment(user, post);

        assert comment.getId() != null;
    }
}
