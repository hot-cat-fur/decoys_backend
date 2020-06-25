package pesko.orgasms.app.domain.models.view;

public class AdminUrlViewModel {

    private String url;


    public AdminUrlViewModel(String url) {
        this.url = url;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
