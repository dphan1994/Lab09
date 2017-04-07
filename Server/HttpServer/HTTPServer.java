import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;

import sun.net.www.http.HttpClient;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.jndi.cosnaming.IiopUrl.Address;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;



public class HTTPServer {
	// a shared area where we get the POST data and then use it in the other
	// handler
	static String sharedResponse = "";
	static boolean gotMessageFlag = false;
	static MainDirectory s = new MainDirectory();
	static InetSocketAddress name;

	public static void main(String[] args) throws Exception {
		
		create_print_html();

		// set up a simple HTTP server on our local host
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

		// create a context to get the request to add dudes to the server
		server.createContext("/submit", new SubmitHandler());

		// create a context to get the request to print the dudes from the
		// server
		server.createContext("/print", new PrintHandler());
		server.createContext("/clear", new ClearHandler());
		server.setExecutor(null); // creates a default executor
		name = server.getAddress();
		// get it going
		System.out.println("Starting Server...");
		server.start();
	}
	
	// a method for filling in the html document and running it
	// it pulls index.html from the src folder
	// then writes the html string to it
	// then it runs the file
	static void create_print_html(){
		String url = "src/index.html";
		String html = "<div><h1>This is lab 9</h1><p>Paragraph on page"
				+ "</p></div>";
		File f = new File(url);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(html);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// run the html file:

		try {
			Desktop.getDesktop().browse(f.toURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	//send to HTML!!
	static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String root = "./wwwroot";
            URI uri = t.getRequestURI();
            System.out.println("looking for: "+ root + uri.getPath());
            String path = uri.getPath();
            File file = new File(root + path).getCanonicalFile();

            if (!file.isFile()) {
              // Object does not exist or is not a file: reject with 404 error.
              String response = "404 (Not Found)\n";
              t.sendResponseHeaders(404, response.length());
              OutputStream os = t.getResponseBody();
              os.write(response.getBytes());
              os.close();
            } else {
              // Object exists and is a file: accept with response code 200.
              String mime = "text/html";
              if(path.substring(path.length()-3).equals(".js")) mime = "application/javascript";
              if(path.substring(path.length()-3).equals("css")) mime = "text/css";            

              Headers h = t.getResponseHeaders();
              h.set("Content-Type", mime);
              t.sendResponseHeaders(200, 0);              

              OutputStream os = t.getResponseBody();
              FileInputStream fs = new FileInputStream(file);
              final byte[] buffer = new byte[0x10000];
              int count = 0;
              while ((count = fs.read(buffer)) >= 0) {
                os.write(buffer,0,count);
              }
              fs.close();
              os.close();
            }  
        }
    }
	
	static class SubmitHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {

			sharedResponse = "";
			
            // set up a stream to read the body of the request
            InputStream inputStr = t.getRequestBody();

            // set up a stream to write out the body of the response
            OutputStream outputStream = t.getResponseBody();

            // string to hold the result of reading in the request
            StringBuilder sb = new StringBuilder();

            // read the characters from the request byte by byte and build up the sharedResponse
            int nextChar = inputStr.read();
            while (nextChar > -1) {
                sb=sb.append((char)nextChar);
                nextChar=inputStr.read();
            }
            sb.insert(0, "[");
            
            sharedResponse = sharedResponse+sb.toString()+"]";

            Gson g = new Gson();
            ArrayList<Employee> incoming = (g.fromJson(sharedResponse, new TypeToken<Collection<Employee>>() {
    		}.getType()));
    		for (Employee emp : incoming) {
    			emp.print();
    		}
    		HttpExchange ex = null;
    		
    		MyHandler hand = new MyHandler();
    		hand.handle(ex);
    		
            s.reconstruct(sharedResponse);
            
            
            System.out.println("Json string: " + sharedResponse);

            String postResponse = "Json received";
            t.sendResponseHeaders(300, postResponse.length());
            // write it and return it
            outputStream.write(postResponse.getBytes());

            outputStream.close();
		}
	}
	
	static class PrintHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			sharedResponse = "";
			
            // set up a stream to read the body of the request
            InputStream inputStr = t.getRequestBody();

            // set up a stream to write out the body of the response
            OutputStream outputStream = t.getResponseBody();

            // string to hold the result of reading in the request
            StringBuilder sb = new StringBuilder();

            // read the characters from the request byte by byte and build up the sharedResponse
            int nextChar = inputStr.read();
            while (nextChar > -1) {
                sb=sb.append((char)nextChar);
                nextChar=inputStr.read();
            }
            
            
            s.print();
            
            
            

            String postResponse = "Json received";
            t.sendResponseHeaders(300, postResponse.length());
            // write it and return it
            outputStream.write(postResponse.getBytes());

            outputStream.close();
		}
	}
	
	static class ClearHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
sharedResponse = "";
			
            // set up a stream to read the body of the request
            InputStream inputStr = t.getRequestBody();

            // set up a stream to write out the body of the response
            OutputStream outputStream = t.getResponseBody();

            // string to hold the result of reading in the request
            StringBuilder sb = new StringBuilder();

            // read the characters from the request byte by byte and build up the sharedResponse
            int nextChar = inputStr.read();
            while (nextChar > -1) {
                sb=sb.append((char)nextChar);
                nextChar=inputStr.read();
            }
            
            
            s.clear();
            
            
            

            String postResponse = "Json received";
            t.sendResponseHeaders(300, postResponse.length());
            // write it and return it
            outputStream.write(postResponse.getBytes());

            outputStream.close();
		}
	}
}
