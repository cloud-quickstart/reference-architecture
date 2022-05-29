// Copyright 2022 Google LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     https://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package ca.cloudlift.reference.sbi;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// change to RestController
@Controller
@RequestMapping("/api")
// http://localhost:8080/sbi/api
public class ApiController {
	
	private final static Logger LOG = Logger.getLogger(ApiController.class.getName());

    private final AtomicLong counter = new AtomicLong();
	private static final CharSequence PASS = "PASS";
    
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Api process(
    		@RequestParam(value="action", required=true, defaultValue="undefined") String action,
    		HttpServletRequest request) {
    	StringBuffer message = new StringBuffer(String.valueOf(counter.incrementAndGet()));
    	// expecting non-base64 query string from the filter chain before this call
    	String queryString = request.getQueryString();
    	String decodedQueryString = (String)request.getAttribute("qs");
    	
    	LOG.info("queryString decoded: " + decodedQueryString);
    	Map<Object, Object> parameterMap = parseParameters(decodedQueryString);
    	String message2 = " : ";
    	message2 = message2 +  " remoteAddr: " +
            	request.getRemoteAddr() + " localAddr: " + 
        		request.getLocalAddr() + " remoteHost: " +
            	request.getRemoteHost() + " serverName: " + 
        		request.getServerName();
    	Enumeration<String> heads = request.getSession().getAttributeNames();
    	String headers = " session attributes: ";
    	while(heads.hasMoreElements()) {
    		headers = headers + "," + heads.nextElement();
    	}
    	String reqUrl = request.getRequestURL().toString();
    	String reqUri = request.getRequestURI();
    	
    	String referer = request.getHeader("referer");
    	String caller = request.getHeader("caller");
    	String path = request.getHeader("path");
    	String host = request.getHeader("Host");
    	
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                              envName,
                              env.get(envName));
        }
        
    	String secret = System.getenv("spring.user.password");
    	//String secret2 = System.getenv("HOME");  
    	String username = System.getenv("DB_USERNAME");
    	//String forwarded = request.getAttribute("X-Forwarded-For").toString();
    	message.append(" ").append(PASS.toString());
    	message.append(" ").append(this.getClass().getCanonicalName())
    			.append(" URL: ").append(reqUrl)
    			.append(" URI: ").append(reqUri)
    			.append(" path: ").append(path)
    			.append(" referer: ").append(referer)
    			.append(" caller: ").append(caller)
    			.append(" Host: ").append(host)
    			//.append(" X-Forwarded-For: ").append(forwarded)
    			.append(" queryString: ").append(queryString)
    			.append(" decodedQueryString: ").append(decodedQueryString)
    			.append(" secret1: ").append(secret)
    			//.append(" secret2: ").append(secret2)  
    			.append(" username: ").append(username)
    			.append(headers)
    			.append(message2);
    	
     	Api api = new Api(counter, message.toString());
     	LOG.info("secret: " + secret);
     	//LOG.info("secret2: " + secret2);     	
    	LOG.info(this.getClass().getCanonicalName() + " " + message);
    	return api;
    } 
    
    /**
     * MapReduce: split a string on & into a list and then each entry on the = delimiter into a map
     * execution=e1s1&action=test = ZXhlY3V0aW9uPWUxczEmYWN0aW9uPXRlc3Q=
     */
    private Map<Object, Object> parseParameters(String decoded) {
    	Map<Object, Object> map = null;
    	if(null != decoded && decoded.length() > 0) {
    		map = Stream.of(decoded
    			.split("&"))
				.map(elem -> new String(elem))
				.collect(Collectors.toList())
				.stream()
    			.map(s -> s.split("="))
    			.collect(Collectors.toMap(a -> a[0], a -> a[1]));
    		map.entrySet().stream()
    			.forEach(e -> LOG.info("Attribute: " + e.getKey() + "=" + e.getValue()));	
    	}
    	return map;
    }
}
