package htwb.ai.beleg.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SongTextDTO {

    private String id;

    @Positive
    @NotNull(message = "songId can not be null")
    private int songId;

    @NotBlank(message = "ownerId can not be empty")
    @NotNull(message = "ownerId can not be null")
    private String ownerId;

    @NotBlank(message = "songId can not be empty")
    @NotNull(message = "songId can not be null")
    private String text;

    public SongTextDTO() {
    }

    public SongTextDTO(String id, int songId, String ownerId, String text) {
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
