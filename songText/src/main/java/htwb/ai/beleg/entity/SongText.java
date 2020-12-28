package htwb.ai.beleg.entity;


import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Document(collection = "songText")
public class SongText {

    @Id
    private String id;

    @Positive
    @NotNull
    private int songId;

    @NotBlank
    @NotNull
    private String ownerId;

    @NotBlank
    @NotNull
    private String text;

    public SongText() {
    }

    public SongText(String id, int songId, String ownerId, String text) {
        this.id = id;
        this.songId = songId;
        this.ownerId = ownerId;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
