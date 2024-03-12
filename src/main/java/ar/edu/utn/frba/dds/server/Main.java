package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.db.EntityManagerHelper;
import ar.edu.utn.frba.dds.server.timers.EnviarNotificacionesTask;
import ar.edu.utn.frba.dds.server.timers.GenerarInformeRankingsTask;
import spark.Spark;
import spark.debug.DebugScreen;

import javax.persistence.Persistence;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
	public static void main(String[] args) throws Exception {
		Map<String, Object> configOverrides = generateConfigOverrides();
		EntityManagerHelper.setEntityManagerFactory(Persistence.createEntityManagerFactory("simple-persistence-unit", configOverrides));
		Spark.port(8080);

		//inicia los timers
		iniciarTimers();

		Router.init();
		//DebugScreen.enableDebugScreen();
	}

	public static void iniciarTimers() {
		TimerTask generarInformesRankingTask = new GenerarInformeRankingsTask();
		Timer timerGenerarInformesRankingTask = new Timer("GenerarInformesRankingTimer");
		//iniciar dentro de 5s, repetir cada 60s - TO-DO: sacar de configuración
		timerGenerarInformesRankingTask.scheduleAtFixedRate(generarInformesRankingTask, 5000L, 30000L);

		TimerTask enviarNotificacionesTask = new EnviarNotificacionesTask();
		Timer timerEnviarNotificacionesTask = new Timer("EnviarNotificacionesTimer");
		//iniciar dentro de 5s, repetir cada 60s - TO-DO: sacar de configuración
		timerEnviarNotificacionesTask.scheduleAtFixedRate(enviarNotificacionesTask, 5000L, 30000L);
	}

	public static Map<String, Object> generateConfigOverrides() throws Exception {
		// https://stackoverflow.com/questions/8836834/read-environment-variables-in-persistence-xml-file
		Map<String, String> env = System.getenv();
		// Leer las variables de entorno y configurar Hibernate
		Map<String, Object> configOverrides = new HashMap<>();
		/*
		configOverrides.put("javax.persistence.jdbc.driver", env.get("org.postgresql.Driver"));
		configOverrides.put("javax.persistence.jdbc.url", env.get("jdbc:postgresql://localhost:5432/postgres"));
		configOverrides.put("javax.persistence.jdbc.user", env.get("postgres"));
		configOverrides.put("javax.persistence.jdbc.password", env.get("rut"));
	*/

		String[] keys = new String[] {
				"DATABASE_URL",
				"javax.persistence.jdbc.driver",
				"javax.persistence.jdbc.password",
				"javax.persistence.jdbc.url",
				"javax.persistence.jdbc.user",
				"hibernate.hbm2ddl.auto",
				"hibernate.connection.pool_size",
				"hibernate.show_sql" };

		for (String key : keys) {

			try{
				if (key.equals("DATABASE_URL")) {


					//esto mepa que es para que guarde en  local!

					// https://devcenter.heroku.com/articles/connecting-heroku-postgres#connecting-in-java
					String value = env.get(key);
					URI dbUri = new URI(value);
					String username = dbUri.getUserInfo().split(":")[0];
					String password = dbUri.getUserInfo().split(":")[1];
					//javax.persistence.jdbc.url=jdbc:postgresql://localhost/dblibros
					value = "jdbc:mysql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();// + "?sslmode=require";
					configOverrides.put("javax.persistence.jdbc.url", value);
					configOverrides.put("javax.persistence.jdbc.user", username);
					configOverrides.put("javax.persistence.jdbc.password", password);
					configOverrides.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");

					//  configOverrides.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
				}
				// no se pueden poner variables de entorno con "." en la key
				String key2 = key.replace("__",".");
				if (env.containsKey(key)) {
					String value = env.get(key);
					configOverrides.put(key2, value);
				}
			} catch(Exception ex){
				System.out.println("Error configurando " + key);
			}
		}
		System.out.println("Config overrides ----------------------");
		for (String key : configOverrides.keySet()) {
			System.out.println(key + ": " + configOverrides.get(key));
		}
		//return Persistence.createEntityManagerFactory("simple-persistence-unit", configOverrides);
		return configOverrides;
	}

}
