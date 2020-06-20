package ma01_20160939.final_project.mobile.lecture;

public class ReviewDto {
    private String title;       //후기 제목
    private String content;     //후기 내용
    private String photoPath;

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
        return title + ", " + content;
    }
}
