package com.example.ChallengeLiteraAlura.ONE.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "autor")
@JsonIgnoreProperties(ignoreUnknown = true)


public class DadosAutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "livro_id")
    private DadosLivro livro;

    @JsonAlias("name")
    private String nome;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DadosLivro getLivro() {
        return livro;
    }

    public void setLivro(DadosLivro livro) {
        this.livro = livro;
    }

}

