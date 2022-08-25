package io.makai.entity;

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
@Table(name = "posts")
public class PostEntity extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "post")
    private List<UpVoteEntity> upVotes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "post")
    private List<DownVoteEntity> downVotes = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "post")
    private List<ShareEntity> shares = new ArrayList<>();

    public PostEntity() {
    }

    public PostEntity(String title, String body, UserEntity user) {
        this.title = title;
        this.body = body;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
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

    public List<ShareEntity> getShares() {
        return shares;
    }

    public void setShares(List<ShareEntity> shares) {
        this.shares = shares;
    }


    public int getUpVoteCount() {
        return upVotes.size();
    }

    public int getDownVoteCount() {
        return downVotes.size();
    }

    public int getShareCount() {
        return shares.size();
    }
}
