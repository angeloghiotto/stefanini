package com.angeloprogramador.stefanini;

public class ListaCidades {

    private String nome;
    private Double lon;
    private Double lat;
    private String clima;
    private String temperatura;

    public ListaCidades(String nome, Double lon, Double lat) {
        this.nome = nome;
        this.lon = lon;
        this.lat = lat;
        this.clima = "-";
        this.temperatura = "-";
    }

    public String getClima() { return clima; }

    public void setClima(String clima) { this.clima = clima; }

    public String getTemperatura() { return temperatura; }

    public void setTemperatura(String temperatura) { this.temperatura = temperatura; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() { return lat; }

    public void setLat(Double lat) {
        this.lat = lat;
    }

}
