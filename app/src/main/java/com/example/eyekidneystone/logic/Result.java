package com.example.eyekidneystone.logic;

public class Result {
    private String status, type;
    private Double rasioHitam, rasioPutih;

    public Result() {
    }

    public Result(String status, String type, Double rasioHitam, Double rasioPutih) {
        this.status = status;
        this.type = type;
        this.rasioHitam = rasioHitam;
        this.rasioPutih = rasioPutih;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public Double getRasioHitam() {
        return rasioHitam;
    }

    public Double getRasioPutih() {
        return rasioPutih;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRasioHitam(Double rasioHitam) {
        this.rasioHitam = rasioHitam;
    }

    public void setRasioPutih(Double rasioPutih) {
        this.rasioPutih = rasioPutih;
    }
}
