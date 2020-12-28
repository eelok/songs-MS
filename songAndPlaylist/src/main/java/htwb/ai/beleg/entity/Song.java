package htwb.ai.beleg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "imageUrl")
    private String imageUrl;

    @NotNull(message = "title can not be null")
    @NotBlank(message = "title can not be empty")
    @Column(name = "title")
    private String title;
    @Column(name = "artist")
    private String artist;
    @Column(name = "label")
    private String label;
    @Column(name = "released")
    private int released;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<SongList> songLists;

    public void removeSongFromLists(Song song) {
        if (this.songLists == null) {
            return;
        }
        this.songLists.forEach(songList -> songList.getSongs().remove(song));
    }

    public void addSongList(SongList songList) {
        if (this.songLists == null) {
            this.songLists = List.of(songList);
        } else {
            this.songLists.add(songList);
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(int released) {
        this.released = released;
    }

    public List<SongList> getSongLists() {
        return songLists;
    }

    public void setSongLists(List<SongList> songLists) {
        this.songLists = songLists;
    }
}
