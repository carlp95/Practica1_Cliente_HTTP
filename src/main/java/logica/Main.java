package logica;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("Introduzca la url a analizar: ");
        Scanner scanner = new Scanner(System.in);
        String url = scanner.nextLine();

        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(new HttpGet(url));
            int status = response.getStatusLine().getStatusCode();
            System.out.println(status);
            if(status >= 200 && status < 400){
                Document documento = Jsoup.connect(url).get();

                //Cantidad de lineas que contiene el documento o recurso requerido.
                System.out.println("Cantidad de lineas del recurso retornado: " + documento.outerHtml().length());

                System.out.println("Cantidad de párrafos<p>: "+ documento.getElementsByTag("p").size());

                /*Elements parrafos = documento.getElementsByTag("p");
                int cantidad = 0;
                for (Element parra:parrafos) {
                    System.out.println(parra);
                    cantidad = parra.getElementsByTag("a").size();
                }*/
                System.out.println("Cantidad de imágenes dentro de párrafos: " + documento.select("p img").size());
            }
        }catch (UnknownHostException e){
            System.err.println("URL no válida");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.err.println("URL mal formada");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
