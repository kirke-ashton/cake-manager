package com.waracle.cakemanager.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "CAKE", uniqueConstraints = {@UniqueConstraint(columnNames = "ID")})
public class CakeEntity implements Serializable, Comparable<CakeEntity> {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "TITLE", unique = false, nullable = false, length = 100)
    private String title;

    @Column(name = "DESC", unique = false, nullable = false, length = 100)
    private String desc;

    @Column(name = "IMAGE", unique = false, nullable = false, length = 300)
    private String image;

    public CakeEntity() {}

    public CakeEntity(String title, String desc, String image) {

        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int compareTo(CakeEntity cake) {
        //return cake.getTitle().compareTo(this.getTitle());
        return this.getTitle().compareTo(cake.getTitle());
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (!(obj instanceof Cake))
            return false;
        CakeEntity cake = (CakeEntity) obj;
        return Objects.equals(this.id, cake.id)
                && Objects.equals(this.title, cake.title)
                && Objects.equals(this.desc, cake.desc)
                && Objects.equals(this.image, cake.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.desc, this.image);
    }

    @Override
    public String toString() {
        return "CakeEntity{" + "id=" + this.id
                + ", title='" + this.title + '\''
                + ", description='" + this.desc + '\''
                + ", image='" + this.title + '\'' + '}';
    }
}