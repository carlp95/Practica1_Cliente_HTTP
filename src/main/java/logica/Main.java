package logica;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
            if(status >= 200 && status < 400){
                Document documento = Jsoup.connect(url).get();

                //Punto A:
                System.out.println("Cantidad de lineas del recurso retornado: " + documento.outerHtml().length());

                //Punto B:
                System.out.println("Cantidad de párrafos<p>: "+ documento.getElementsByTag("p").size());

                //Punto C:
                System.out.println("Cantidad de imágenes dentro de párrafos: " + documento.select("p img").size());

                //Punto D:
                Elements formularios = documento.getElementsByTag("form");
                int cantidad_get = 0;
                int cantidad_post = 0;
                for (Element form : formularios ) {
                    if(form.getElementsByAttributeValue("method","get").size() > 0){
                        cantidad_get = form.getElementsByAttributeValue("method","get").size();
                    }else if(form.getElementsByAttributeValue("method","post").size() > 0){
                        cantidad_post = form.getElementsByAttributeValue("method","post").size();
                    }
                    //System.out.println(form);
                }
                System.out.println("Cantidad de formularios con el méthod POST: " + cantidad_post);
                System.out.println("Cantidad de formularios con el méthod GET: " + cantidad_get);

                //Punto E:
                for (Element form2 : formularios) {
                    for(int i = 0; i < form2.getElementsByTag("input").size(); i++){
                        System.out.println("Tipo de input: " + form2.getElementsByTag("input").get(i).attributes().get("type"));
                    }
                }

                //Punto F:
                for (Element form3 : formularios ) {
                    if(form3.attributes().get("method").equals("post")){

                        try {
                            //Se utiliza el atributo action porque es en donde se contiene la url hacia donde apunta se mandará la data.
                            Document document_post = Jsoup.connect(form3.absUrl("action")).data("asignatura","practica1").header("matricula","20130212").post();
                            System.out.println("Respuesta del servidor:" + document_post.body());
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        }catch (UnknownHostException e){
            System.err.println("URL no válida");
            //e.printStackTrace();
        } catch (MalformedURLException e) {
            System.err.println("URL mal estructurada");
            //e.printStackTrace();
        } catch (ClientProtocolException e){
            System.err.println("Debe indicar un protocolo correcto (https, http)");
        }catch (IOException e) {
            System.err.println("No se pudo abrir el archivo");
            e.printStackTrace();
        }


    }
}
