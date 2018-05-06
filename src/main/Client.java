package main;

import javax.naming.*;

import com.msgServer.Authenticator;
import com.msgServer.AuthenticatorRemote;

import java.util.*;

public class Client {
	
	private boolean access = false;
	private AuthenticatorRemote bean;

	public Client() {
		// TODO Auto-generated method stub
		try {
			System.out.println("Client App Started");
			Properties props = new Properties();
			props.put("java.naming.factory.url.pkgs", "org.jboss.ejb.client.naming");
			InitialContext context = new InitialContext(props);

			String appName = "";
			String moduleName = "myEJB";
			String distinctName = "";
			String beanName = Authenticator.class.getSimpleName();
			String interfaceName = Authenticator.class.getName();
			String name = "ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!"
					+ interfaceName;
			System.out.println(name);

			bean = (AuthenticatorRemote) context.lookup(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasAccess(int id, String f) {
		return bean.hasAccess(id, f);
	}

}