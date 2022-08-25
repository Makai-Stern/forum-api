package io.makai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.makai.entity.CommentEntity;
import io.makai.entity.DownVoteEntity;
import io.makai.entity.PostEntity;
import io.makai.entity.RoleEntity;
import io.makai.entity.ShareEntity;
import io.makai.entity.UpVoteEntity;
import io.makai.entity.UserEntity;
import io.makai.repository.CommentRepository;
import io.makai.repository.DownVoteRepository;
import io.makai.repository.PostRepository;
import io.makai.repository.RoleRepository;
import io.makai.repository.ShareRepository;
import io.makai.repository.UpVoteRepository;
import io.makai.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @Autowired
    private ObjectMapper objMapper;

    @Autowired
    private UpVoteRepository upVoteRepository;

    @Autowired
    private DownVoteRepository downVoteRepository;

    @Autowired
    private ShareRepository shareRepository;

    @Test
    void contextLoads() {
    }

    public UserEntity createUser() {
        String uuidStr = UUID.randomUUID().toString();
        String username = "test_user_" + uuidStr;
        String password = bCryptPasswordEncoder.encode("123456");

        UserEntity user = new UserEntity(username, password);
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(ROLE_USER.name()));
        user.setAuthorities(roles);
        UserEntity savedUser = userRepository.saveAndFlush(user);

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
            PostEntity post = postRepository.saveAndFlush(new PostEntity(title + " - post " + i, body, user));
            user.getPosts().add(post);
            data.put(key, post);
        }

        UserEntity savedUser = userRepository.saveAndFlush(user);
        data.put("user", savedUser);

        return data;
    }

    @Test
    public void createPostTest() {
        int numOfPosts = 12;
        Map<String, Object> map = createPost(12);

        UserEntity user = (UserEntity) map.get("user");

        for (int i = 0; i < numOfPosts; i++) {
            String key = "post" + (i + 1);
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
        CommentEntity comment = commentRepository.saveAndFlush(new CommentEntity(body, postEntity, userEntity));

        postEntity.getComments().add(comment);
        postRepository.saveAndFlush(postEntity);

        userEntity.getComments().add(comment);
        userRepository.saveAndFlush(userEntity);

        try {
            String commentJson = objMapper.writeValueAsString(comment);
            System.out.println(commentJson);
        }catch (Exception e) {
            System.out.println("Failed to create JSON for Comment entity");
        }

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

    public UpVoteEntity createUpVote(UserEntity user, Object obj) {

        UpVoteEntity upVote = new UpVoteEntity();
        upVote.setUser(user);

        if (obj.getClass() == PostEntity.class) {
            PostEntity post = (PostEntity) obj;
            upVote.setPost(post);

            UpVoteEntity savedUpVote = upVoteRepository.saveAndFlush(upVote);
            post.getUpVotes().add(savedUpVote);
            postRepository.saveAndFlush(post);

            user.getUpVotes().add(savedUpVote);
        }

        if (obj.getClass() == CommentEntity.class) {
            CommentEntity comment =  (CommentEntity) obj;
            upVote.setComment(comment);

            UpVoteEntity savedUpVote = upVoteRepository.saveAndFlush(upVote);
            comment.getUpVotes().add(savedUpVote);
            commentRepository.saveAndFlush(comment);

            user.getUpVotes().add(savedUpVote);
        }

        userRepository.saveAndFlush(user);
        return upVote;
    }

    public DownVoteEntity createDownVote(UserEntity user, Object obj) {

        DownVoteEntity downVote = new DownVoteEntity();
        downVote.setUser(user);

        if (obj.getClass() == PostEntity.class) {
            PostEntity post = (PostEntity) obj;
            downVote.setPost(post);

            DownVoteEntity savedDownVote = downVoteRepository.saveAndFlush(downVote);
            post.getDownVotes().add(savedDownVote);
            postRepository.saveAndFlush(post);

            user.getDownVotes().add(savedDownVote);
        }

        if (obj.getClass() == CommentEntity.class) {
            CommentEntity comment =  (CommentEntity) obj;
            downVote.setComment(comment);

            DownVoteEntity savedDownVote = downVoteRepository.saveAndFlush(downVote);
            comment.getDownVotes().add(savedDownVote);
            commentRepository.saveAndFlush(comment);

            user.getDownVotes().add(savedDownVote);
        }

        userRepository.saveAndFlush(user);
        return downVote;
    }

    @Test
    public void createUpVoteTest() {
        int numOfPosts = 1;

        Map<String, Object> map = createPost(numOfPosts);
        UserEntity user = (UserEntity) map.get("user");
        PostEntity post = (PostEntity) map.get("post" + numOfPosts);

        UpVoteEntity upVote = createUpVote(user, post);

        try {
            System.out.println(objMapper.writeValueAsString(upVote.getPost()));
        } catch (Exception e) {
            System.out.println("Failed to create JSON for Post entity");
        }

        assert upVote != null;
    }


    @Test
    public void createDownVoteTest() {
        int numOfPosts = 1;

        Map<String, Object> map = createPost(numOfPosts);
        UserEntity user = (UserEntity) map.get("user");
        PostEntity post = (PostEntity) map.get("post" + numOfPosts);
        CommentEntity comment = createComment(user,post);

        DownVoteEntity downVote = createDownVote(user, comment);

        try {
            System.out.println(objMapper.writeValueAsString(downVote.getComment()));
        } catch (Exception e) {
            System.out.println("Failed to create JSON for Comment entity");
        }

        assert downVote != null;
    }

    @Test
    @Transactional
    public void createShareCommentTest() {

        UserEntity userThatShares = createUser();

        int numOfPosts = 1;
        Map<String, Object> map = createPost(numOfPosts);
        UserEntity userThatPosts = (UserEntity) map.get("user");
        PostEntity post = (PostEntity) map.get("post" + numOfPosts);
        CommentEntity comment = createComment(userThatPosts, post);

        ShareEntity shareComment = shareRepository.saveAndFlush(new ShareEntity(comment, userThatShares));

        comment.getShares().add(shareComment);
        commentRepository.saveAndFlush(comment);

        userThatShares.getShares().add(shareComment);
        UserEntity updatedUserThatShares = userRepository.saveAndFlush(userThatShares);

        int pageNum = 0;
        int pageSize = 3;

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        // Why create unnecessarily long variable names? Because it's Java
        List<ShareEntity> updatedUserThatSharesCommentsList = shareRepository.findSharedCommentsByUserId(updatedUserThatShares.getId(), pageable);

        try {
            System.out.println("Share: " + objMapper.writeValueAsString(updatedUserThatSharesCommentsList.get(0)));
            System.out.println("Comment: " + objMapper.writeValueAsString(updatedUserThatSharesCommentsList.get(0).getComment()));
        } catch (Exception e) {
            System.out.println("\n" +  e.getMessage() + "\n");
            System.out.println("Failed to create JSON for ShareEntity entity list");
        }

        assert updatedUserThatSharesCommentsList.size() == 1;
        assert updatedUserThatShares.getShares() != null;
    }

    @Test
    @Transactional
    public void createSharePostTest() {

        UserEntity userThatShares = createUser();

        int numOfPosts = 1;
        Map<String, Object> map = createPost(numOfPosts);
        PostEntity post = (PostEntity) map.get("post" + numOfPosts);

        ShareEntity sharePost = shareRepository.saveAndFlush(new ShareEntity(post, userThatShares));
        userThatShares.getShares().add(sharePost);

        post.getShares().add(sharePost);
        postRepository.saveAndFlush(post);

        userThatShares.getShares().add(sharePost);
        UserEntity updatedUserThatShares = userRepository.saveAndFlush(userThatShares);

        int pageNum = 0;
        int pageSize = 3;

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        // Why create unnecessarily long variable names? Because it's Java
        List<ShareEntity> updatedUserThatSharesPostsList = shareRepository.findSharedPostsByUserId(updatedUserThatShares.getId(), pageable);

        try {
            System.out.println("Share: " + objMapper.writeValueAsString(updatedUserThatSharesPostsList.get(0)));
            System.out.println("Post: " + objMapper.writeValueAsString(updatedUserThatSharesPostsList.get(0).getPost()));
        } catch (Exception e) {
            System.out.println("\n" +  e.getMessage() + "\n");
            System.out.println("Failed to create JSON for ShareEntity entity list");
        }

        assert updatedUserThatSharesPostsList.size() == 1;
        assert updatedUserThatShares.getShares() != null;
    }
}
