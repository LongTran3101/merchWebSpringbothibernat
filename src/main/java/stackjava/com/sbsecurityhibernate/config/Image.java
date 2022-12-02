/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stackjava.com.sbsecurityhibernate.config;

import java.util.Objects;

/**
 *
 * @author me
 */
public class Image {

    public Image(String url) {
        this.url = url;
    }
      public Image() {
       
    } 
    
    String name;
    String url;
    String alt;
    String des;
    String maintag;
    String tagchuan;
    String urlpng;
    String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrlpng() {
        return urlpng;
    }

    public void setUrlpng(String urlpng) {
        this.urlpng = urlpng;
    }

   

    public String getTagchuan() {
        return tagchuan;
    }

    public void setTagchuan(String tagchuan) {
        this.tagchuan = tagchuan;
    }

    public String getMaintag() {
        return maintag;
    }

    public void setMaintag(String maintag) {
        this.maintag = maintag;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Image other = (Image) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
    
}
