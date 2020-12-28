package htwb.ai.beleg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.util.Collection;

@Entity
@JsonPropertyOrder({"id", "ownerId", "name", "isPrivate", "songList"})
@Table(name = "songList")
public class SongList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "imgUrl")
    private String imgUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "isPrivate")
    private boolean isPrivate;

    @ManyToMany(mappedBy = "songLists", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("songLists")
    private Collection<Song> songs;

    @Column(name = "ownerId")
    private String ownerId;


    public void removeFromSongs() {
        songs.forEach(song -> song.getSongLists().remove(this));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("isPrivate")
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @JsonProperty("songs")
    public Collection<Song> getSongs() {
        return songs;
    }

    public void setSongs(Collection<Song> songs) {
        this.songs = songs;
    }

    @JsonProperty("ownerId")
    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String userId) {
        this.ownerId = userId;
    }
}


