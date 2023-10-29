package br.com.example.estudocamel.context.route;

import br.com.example.estudocamel.context.domain.Person;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.model.dataformat.BindyDataFormat;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonRoute extends RouteBuilder {

    public static final String FIND_PERSONS = "direct://route.find.persons";
    public static final String FIND_PERSONS_BY_ID = "direct://route.find.persons.by.id";
    public static final String CREATE_PERSON = "direct://route.create.person";

    public static final String FIND_PERSONS_URI = "http://localhost:3000/person";

    public static final String PRODUCER_PERSON = "kafka:person-topic";

    @Override
    public void configure() throws Exception {

        from(CREATE_PERSON)
                .routeId("route.create.person")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process(exchange -> {
                    System.out.println(exchange);
                })
                .marshal().json(JsonLibrary.Jackson, Person.class)
                .process(exchange -> {
                    System.out.println(exchange);
                })

                .to(FIND_PERSONS_URI)
                .to(PRODUCER_PERSON)
                .removeHeaders("*")
                .unmarshal().json(JsonLibrary.Jackson, Person.class)
                .removeHeaders("*")
                .log("depois da requisição ${body}");

        from(FIND_PERSONS_BY_ID)
                .routeId("route.find.persons.by.id")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader(Exchange.HTTP_PATH, simple("/${body}"))
                .process(exchange -> {

                })
                .process(exchange -> {
                    System.out.println(exchange);
                })
                .to(FIND_PERSONS_URI)
                .unmarshal().json(JsonLibrary.Jackson, Person.class)
                .process(exchange -> {
                    System.out.println(exchange);
                })
                //.unmarshal().json(JsonLibrary.Jackson, Person.class)
                .log("----> ${body}");


        from(FIND_PERSONS)
                .routeId("route.find.persons")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to(FIND_PERSONS_URI)
                .process(exchange -> {
                    System.out.println(exchange);

                })
                .unmarshal().json(JsonLibrary.Jackson, List.class)
                .log("----> ${body}")
                .process(exchange -> {
                   System.out.println(exchange);
                });
    }
}
