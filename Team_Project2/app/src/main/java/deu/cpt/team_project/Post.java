package deu.cpt.team_project;

public class Post {
    private String PostToken;
    private String Title;
    private String Detail;
    private String Writer;
    private String Date;

    private Comment Post_Comment;

    public Post() {
    }

    public String getPostToken() {
        return PostToken;
    }

    public void setPostToken(String postToken) {
        PostToken = postToken;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
    public Comment getPost_Comment() {
        return Post_Comment;
    }

    public void setPost_Comment(Comment post_Comment) {
        Post_Comment = post_Comment;
    }
    public String getCombined(){
        return Title + " " + Detail;
    }
}
