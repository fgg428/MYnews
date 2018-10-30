package work.wang.mynews;
/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class News {
    private String newsTitle;
    private String newsUrl;
    private String newsPict;
    private String newsSource;
    public News(String newsTitle, String newsUrl, String newsPict, String newsSource) {
        this.newsTitle = newsTitle;
        this.newsUrl = newsUrl;
        this.newsPict = newsPict;
        this.newsSource = newsSource;
    }

    public String getNewsPict(){
        return newsPict;
    }

    public void setNewsPict(String newsPict) {
        this.newsPict = newsPict;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}

