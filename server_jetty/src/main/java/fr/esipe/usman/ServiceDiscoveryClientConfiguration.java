package fr.esipe.usman;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class ServiceDiscoveryClientConfiguration {

    @Value("${zookeeper_hosts}")
    private String zookeeper_hosts;
    private final Logger logger = java.util.logging.Logger.getLogger("ServiceDiscoveryClientConfiguration");
    private CuratorFramework curatorFrameworkFactory;

    @Bean(initMethod = "start", destroyMethod = "close")
    public ServiceDiscovery<String> discovery() {
        //JsonInstanceSerializer nous permet d’annoncer que nous allons stocker des informations supplémentaires
        // grâce à l’utilisation de JSON
        JsonInstanceSerializer<String> serializer =
                new JsonInstanceSerializer<String>(String.class);


        //ServiceDiscoveryBuilder pour préciser dans quel rangement le service veut s’enregistrer
        // ici dans rangement "services"
        return ServiceDiscoveryBuilder.builder(String.class)
                .client(curator())
                .basePath("services")
                .serializer(serializer)
                .build();
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curator() {
        logger.warning("------------------------------------");
        logger.warning("--------ZOOKEEPER CONNECTION--------");
        logger.warning("------------------------------------");
        logger.warning("Open connection... ");
        logger.warning("------------------------------------");
        logger.warning("------------CALL CURATOR-------------");

        logger.warning("CALL curator !");
        try{
            curatorFrameworkFactory = CuratorFrameworkFactory.newClient("192.168.43.201,192.168.43.222,192.168.43.206", new ExponentialBackoffRetry(1000, 3));
            logger.warning("Zookeeper client started ! ... "+curatorFrameworkFactory.getState().toString());
            logger.warning("-------------------------------------");


        }catch (Exception e){
            logger.warning("ERREUR !"+e);

        }
        return curatorFrameworkFactory;
    }

}
