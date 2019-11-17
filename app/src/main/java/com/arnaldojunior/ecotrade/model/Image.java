package com.arnaldojunior.ecotrade.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Image implements Serializable {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("anuncio")
    @Expose
    private Anuncio anuncio;

    public Image() {}

    public Image(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image1 = (Image) o;
        return Objects.equals(id, image1.id) &&
                Objects.equals(image, image1.image) &&
                Objects.equals(anuncio, image1.anuncio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image, anuncio);
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", anuncio=" + anuncio +
                '}';
    }
}
