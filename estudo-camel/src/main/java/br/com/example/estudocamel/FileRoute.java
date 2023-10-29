package br.com.example.estudocamel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileRoute extends RouteBuilder {

    public static final String PATH_ORIGIN = "file:src/main/resources/entrada?delete=true";
    public static final String PAH_DESTINY = "file:src/main/resources/saida";

    @Override
    public void configure() {

        from(PATH_ORIGIN)
                .log("Arquivo lido: ${header.CamelFileName}") // exibe o nome do arquivo lido
                .split(body().tokenize("\n"))
                .convertBodyTo(String.class)
                .process(exchange -> {
                   String linha = exchange.getIn().getBody(String.class);

                   System.out.println("Linha do aquivo:" + linha);
                })
                .to(PAH_DESTINY);
    }
}
