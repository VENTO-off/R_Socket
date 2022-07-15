package relevant_craft.vento.r_launcher.r_socket.manager.download;

public class CF_File {

    private int id;
    private String url;
    private String directUrl;

    public CF_File(int id, String url, String directUrl) {
        this.id = id;
        this.url = url;
        this.directUrl = directUrl;
    }

    public CF_File(String url, String directUrl) {
        this.id = 0;
        this.url = url;
        this.directUrl = directUrl;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getDirectUrl() {
        return directUrl;
    }
}
