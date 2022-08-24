package io.makai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column(name = "body", nullable = false)
    private String body;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "comment")
    private List<UpVoteEntity> upVotes = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "comment")
    private List<DownVoteEntity> downVotes = new ArrayList<>();

    public CommentEntity() {
    }

    public CommentEntity(String body, PostEntity post, UserEntity user) {
        this.body = body;
        this.post = post;
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<UpVoteEntity> getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(List<UpVoteEntity> upVotes) {
        this.upVotes = upVotes;
    }

    public List<DownVoteEntity> getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(List<DownVoteEntity> downVotes) {
        this.downVotes = downVotes;
    }

    public int getUpVoteCount() {
        return upVotes.size();
    }

    public int getDownVoteCount() {
        return downVotes.size();
    }
}
