package com.example.rauliyrjana.raulinmetronomi;

public class Biisi {
    private long id;
    private String nimi;
    private String tempo;

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id=id;
    }
    public String getNimi(){
        return nimi;
    }
    public void setNimi(String nimi){
        this.nimi = nimi;
    }
    public String getTempo(){
        return tempo;
    }
    public void setTempo(String tempo){
        this.tempo=tempo;
    }


    @Override
    public String toString() {
        return nimi + " " + tempo ;
    }
}


