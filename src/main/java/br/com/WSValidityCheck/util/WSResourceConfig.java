package br.com.WSValidityCheck.util;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("ws")
public class WSResourceConfig extends ResourceConfig{
	public WSResourceConfig(){
		packages("br.com.WSValidyCheck.service");
	}
}
