package cz.ophite.mimic.vhackos.botnet.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Informace o aktualizaci Botnetu.
 *
 * @author mimic
 */
public final class BotnetUpdateData {

    @SerializedName("version")
    private String version;

    @SerializedName("download")
    private String downloadLink;

    @SerializedName("discord")
    private String discordLink;

    @SerializedName("news")
    private List<String> news;

    @SerializedName("notice")
    private String notice;

    private transient boolean newVersionAvailable;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getNews() {
        return news;
    }

    public void setNews(List<String> news) {
        this.news = news;
    }

    public boolean isNewVersionAvailable() {
        return newVersionAvailable;
    }

    public void setNewVersionAvailable(boolean newVersionAvailable) {
        this.newVersionAvailable = newVersionAvailable;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getDiscordLink() {
        return discordLink;
    }

    public void setDiscordLink(String discordLink) {
        this.discordLink = discordLink;
    }
}
