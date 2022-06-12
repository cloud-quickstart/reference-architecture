package ca.cloudlift.reference.nbi.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * 20210926
 * Stats
 * - 2 3610 MacMini on 1g - 2 vCores sat, 65/sec
 * @author michaelobrien
 *
 */
@Service
public class ForwardingServiceImpl implements ForwardingService {

	private String dnsFrom;
	private String dnsTo;
	private String delay;
	private String portTo;
	private String portFrom;
	private AtomicLong counter = new AtomicLong(1);
	private HttpClient httpClient = HttpClient.newBuilder().build();
	//private static boolean stopForwarding = false;
    private AtomicLong iterations = new AtomicLong(100);
	
	
	static Logger logger = Logger.getLogger(ForwardingServiceImpl.class.getName());
	
	private Thread thread;
	private AtomicBoolean running = new AtomicBoolean(false);
	private AtomicBoolean stopped = new AtomicBoolean(true);	

    private static final String PROTOCOL = "https";

//  private static final String URL_RETURN = "http://host.docker.internal:8888";
//  private static final String URL_RETURN = "http://host.docker.internal:";//8080/nbi/forward/packet";
	private static final String URL_POSTFIX_REFLECTOR = "/nbi/forward/packet";
	private static final String URL_POSTFIX_TRAFFIC = "/nbi/forward/traffic";
    private static final String URL_POSTFIX_API = "/nbi/api";
	private Runnable runnable = () -> { sendMessage(); };
    private Runnable runnableTraffic = () -> { sendMessage(false); };
    
    // Random string to avoid http caching
    Random randomGenerator = new Random();
    
    private static TrustManager[] trustAllCerts = new TrustManager[]{
    	    new X509TrustManager() {
    	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    	            return null;
    	        }
    	        public void checkClientTrusted(
    	            java.security.cert.X509Certificate[] certs, String authType) {
    	        }
    	        public void checkServerTrusted(
    	            java.security.cert.X509Certificate[] certs, String authType) {
    	        }
    	    }
    	};
  
	@Override
	public String reset() {
		String ret = "";
		if(running.get()) {
			running.set(false);
			stopped.set(true);
			logger.info("Stopping thread");
			//thread.interrupt();
		} else {
			running.set(true);
			stopped.set(false);
			logger.info("resuming thread on next request");
		}
		return ret;
	}

	@Override
	public String forward(String dnsFrom, String dnsTo, String from, String to, String delay) {
		try { 
			Thread.sleep(Long.parseLong(delay));
			} catch (Exception e) {};
			
		portFrom = from;
		portTo = to;
		this.delay = delay;
		this.dnsFrom = dnsFrom;
		this.dnsTo = dnsTo;
		//Runnable aRunnable = runnable;
		// 1 thread at a time
		//if(!stopped.get())
			thread = new Thread(runnable);
			thread.start();	
			//running.set(true);
			//stopped.set(false);		
		return Long.toString(counter.addAndGet(1));
	}

    private void sendMessage() {
        sendMessage(true);
    }

    private void sendMessage(boolean isReflector) {
        String url = PROTOCOL + "://";
        if(isReflector) {
            url = url + dnsTo + 
				":" + portTo + URL_POSTFIX_REFLECTOR + 
				"?dnsFrom=" + dnsTo + 
				"&dnsTo=" + dnsFrom + 
				"&from=" + portTo + 
				"&to=" + portFrom + 
				"&delay=" + delay +
				"&random=" + Integer.toString(randomGenerator.nextInt());
        } else {
            url = url + dnsTo + 
            ":" + portTo + URL_POSTFIX_API + 
            "?dns=" + dnsTo + 
            "&to=" + portTo + 
            "&delay=" + delay + 
            "&iterations=" + iterations.get() +
			"&random=" + Integer.toString(randomGenerator.nextInt());
        }

        logger.info("Ms: " + System.currentTimeMillis());
        logger.info("Ns: " + System.nanoTime());
        sendMessage(url, isReflector);
    }

	private void sendMessage(String url, boolean isReflector) {
        SSLContext sslContext = null;
        try {
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        } catch (Exception e) {
            
        }
        if(isReflector) {
    		if(!stopped.get()) {
	    		logger.info("Request reflection: " + url);
		    	HttpRequest request = HttpRequest.newBuilder()
		    		//.sslContext(sslContext)
			    	.uri(URI.create(url))
				    .GET()
				    .build();
		    	try {
			    	HttpResponse<String> response =
						httpClient.send(request, BodyHandlers.ofString());
				    String body = response.body();
				    //logger.info("Response: " + body);
			    } catch (IOException ioe) {
		          	logger.info(ioe.getMessage());
			    	ioe.printStackTrace();
			    } catch (InterruptedException ie) {
		          	logger.info(ie.getMessage());
				    ie.printStackTrace();
		    	}
		    } else {
			    logger.info("Skipping reflection");
 
	    	}
        } else {
            if(!stopped.get()) {
            // traffic generation
            //long iters = iterations.get();
            //IntStream.range(0, iters).forEach()
            for(long i=0; i<iterations.get(); i++) {
                try { 
                    Thread.sleep(Long.parseLong(delay));
                    } catch (Exception e) {};
        
            //logger.info("traffic generation: " + i + " of " + iterations.get() + " : " + url);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
            try {
            	HttpClient httpClient2 = HttpClient.newBuilder().sslContext(sslContext).build();
                HttpResponse<String> response =
                    httpClient2.send(request, BodyHandlers.ofString());
                String body = response.body();
                //logger.info("Response: " + body);
            } catch (IOException ioe) {
            	logger.info(ioe.getMessage());
                ioe.printStackTrace();
            } catch (InterruptedException ie) {
              	logger.info(ie.getMessage());
                ie.printStackTrace();
            }
        }

        }
    }
	}


    @Override
	public String traffic(String dns, String to, String delay, String iterationsString) {
		try { 
			Thread.sleep(Long.parseLong(delay));
			} catch (Exception e) {};
			
		portTo = to;
		this.delay = delay;
		this.dnsTo = dns;
        this.iterations = new AtomicLong(Long.parseLong(iterationsString));
        running.set(true);
        stopped.set(false);
		//Runnable aRunnable = runnable;
		// 1 thread at a time
		//if(!stopped.get())
			thread = new Thread(runnableTraffic);
			thread.start();	
			//running.set(true);
			//stopped.set(false);		
		return Long.toString(counter.addAndGet(1));
	}

}
