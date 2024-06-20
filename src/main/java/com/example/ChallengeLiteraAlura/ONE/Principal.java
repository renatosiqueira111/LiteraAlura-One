package com.example.ChallengeLiteraAlura.ONE;

import com.example.ChallengeLiteraAlura.ONE.modelos.DadosAutor;
import com.example.ChallengeLiteraAlura.ONE.modelos.DadosLivro;
import com.example.ChallengeLiteraAlura.ONE.modelos.TodosOsDados;
import com.example.ChallengeLiteraAlura.ONE.repository.DadosRepository;
import com.example.ChallengeLiteraAlura.ONE.repository.DadosRepositoryAutor;
import com.example.ChallengeLiteraAlura.ONE.servicos.ConsumoAPI;
import com.example.ChallengeLiteraAlura.ONE.servicos.ConverteDados;
import com.example.ChallengeLiteraAlura.ONE.servicos.Mensagem;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    String urlBase = "https://gutendex.com/books/?search=";
    List<DadosLivro> livros = new ArrayList<>();
    Scanner leitura = new Scanner(System.in);
    int opcaoDigitada;
    String opcaoNovaConsulta;

    boolean loopMenu = true;


    @Autowired
    private DadosRepository repositorio;
    @Autowired
    private DadosRepositoryAutor dadosAutorRepository;

    public Principal(DadosRepository repositorio,DadosRepositoryAutor dadosAutorRepository) {

        this.repositorio = repositorio;
        this.dadosAutorRepository = dadosAutorRepository;
    }


    public void Iniciar() throws JsonProcessingException {

        while (loopMenu) {
            Mensagem.menuInicial();
            opcaoDigitada = leitura.nextInt();
            leitura.nextLine();
            switch (opcaoDigitada) {
                case 0:
                    loopMenu = false;
                    System.out.println("Aplicação encerrada");
                    break;
                case 1:
                    buscaPorTitulo();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();

            }


        }


    }

    private void listarAutoresRegistrados() {
        List<DadosAutor> autores = dadosAutorRepository.findAll();

    }

    private void listarLivrosRegistrados() {
        List<DadosLivro> livros = repositorio.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            System.out.println("Livros registrados:");
            for (DadosLivro livro : livros) {
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Idioma: " + livro.getIdioma().toString().replace("[", "").replace("]", ""));
                System.out.println("Downloads: " + livro.getDownloads());
                List<DadosAutor> autores = livro.getAuthors();
            for (DadosAutor autor : autores){
                System.out.println("Autor: " + autor.getNome());
            }

                System.out.println(); // Linha em branco para separar os livros
            }
        }
    }

    private void buscaPorTitulo() throws JsonProcessingException {
        Mensagem.buscaPorTitulo();
        String complementoUrl = leitura.nextLine().replace(" ", "%20").trim();
        String urlCompleta = urlBase + complementoUrl;
        ConsumoAPI consumo = new ConsumoAPI();
        var respostaAPI = consumo.obterDados(urlCompleta);
        ConverteDados conversor = new ConverteDados();
        TodosOsDados dados = conversor.obterDados(respostaAPI, TodosOsDados.class);

        for (DadosLivro livro : dados.getResults()) {
            System.out.println("----- LIVRO -----" + "\n" + "\n" + "Título: " + livro.getTitulo());
            for (DadosAutor autor : livro.getAuthors()) {
                System.out.println("Autor:" + autor.getNome());
                dadosAutorRepository.save(autor);
            }
            repositorio.save(livro);
            System.out.println("Idioma: " + livro.getIdioma().toString().replace("[", "").replace("]", ""));
            System.out.println("Downloads: " + livro.getDownloads() + "\n" + "\n" + "---------------");

        }




    }
}
