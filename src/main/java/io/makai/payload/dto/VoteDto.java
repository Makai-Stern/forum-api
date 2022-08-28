package io.makai.payload.dto;

public class VoteDto {

    private String commentId;

    private String postId;

    private int pageNumber;

    private int pageSize;

    private boolean isUpVote;

    private boolean isDownVote;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isUpVote() {
        return isUpVote;
    }

    public void setIsUpVote(boolean isUpVote) {
        this.isUpVote = isUpVote;
    }

    public boolean isDownVote() {
        return isDownVote;
    }

    public void setIsDownVote(boolean isDownVote) {
        this.isDownVote = isDownVote;
    }
}
