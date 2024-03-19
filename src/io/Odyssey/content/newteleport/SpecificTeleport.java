package io.Odyssey.content.newteleport;


import io.Odyssey.model.entity.player.Position;

public class SpecificTeleport {

    public int button;

    public   Position tile;
  public  String text;
    public  String description;
    public  boolean favorited;
    public int regionunlock;

    public int favoritebutton;
    public SpecificTeleport(int button, Position tile, String text, String description, boolean favorited, int favoritebutton, int regionunlock){
        this.button = button;
        this.tile = tile;
        this.text = text;
        this.description = description;
        this.favorited = favorited;
        this.favoritebutton = favoritebutton;
        this.regionunlock = regionunlock;
    }
}
