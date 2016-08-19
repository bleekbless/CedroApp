package com.example.paulo.provacedro;

import java.io.Serializable;

/**
 * Created by Paulo on 12/08/2016.
 */

public class Paises implements Serializable {

    public String id;
    public String iso;
    public String longname;
    public String callingcode;
    public String status;
    public String culture;
    public String shortname;
    public String fragment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getLongname() {
        return longname;
    }

    public void setLongname(String longname) {
        this.longname = longname;
    }

    public String getCallingcode() {
        return callingcode;
    }

    public void setCallingcode(String callingcode) {
        this.callingcode = callingcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }
}
