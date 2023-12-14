package deu.cpt.team_project;

public class Comment {
    private String CommentKey;

    public String getCommentKey() {
        return CommentKey;
    }

    public void setCommentKey(String commentKey) {
        CommentKey = commentKey;
    }

    private String Writer;
    private String Comment;

    public Comment() {
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
