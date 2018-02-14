package fr.esipe.usman;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class ServiceDiscoveryConfiguration implements CommandLineRunner, LeaderLatchListener {
    private final Logger logger = java.util.logging.Logger.getLogger("ServiceDiscoveryConfiguration");

    @Autowired
    ServiceDiscovery<String> discovery;

    /**
     * serverPort est fourni au démarrage en ligne de commande
     */
//
    @Value("${server.port}")
    private int serverPort;

    @Value("${zookeeper.hosts}")
    private String zookeeper_hosts;

    public static CuratorFramework curatorFrameworkFactory;

    public void run(String... args) throws Exception {

        ServiceInstance<String> instance =
                ServiceInstance.<String>builder()
                        .name("zookeeper_client")
                        .payload("1.0")
                        .address("localhost")
                        .port(serverPort)
                        .uriSpec(new UriSpec("{scheme}://{address}:{port}/votes"))
                        .build();

        discovery.registerService(instance);
    }



    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curator() {
        logger.warning("------------------------------------");
        logger.warning("--------ZOOKEEPER CONNECTION DISCOVERY--------");
        logger.warning("------------------------------------");
        logger.warning("Open connection... ");
        logger.warning("------------------------------------");
        logger.warning("------------CALL CURATOR-------------");

        logger.warning("CALL curator !");
        try{
            curatorFrameworkFactory = CuratorFrameworkFactory.newClient("192.168.43.201,192.168.43.222,192.168.43.206", new ExponentialBackoffRetry(1000, 3));
        logger.warning("Zookeeper client started ! ... "+curatorFrameworkFactory.getState().toString());
        logger.warning("------------------------------------");

        }catch (Exception e){
            logger.warning("ERREUR !"+e);

        }
        return curatorFrameworkFactory;
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public ServiceDiscovery<String> discovery() {
        JsonInstanceSerializer<String> serializer =
                new JsonInstanceSerializer<String>(String.class);

        return ServiceDiscoveryBuilder.builder(String.class)
                .client(curator())
                .basePath("services")
                .serializer(serializer)
                .build();
    }

    @Override
    public void isLeader() {
        logger.info("Je suis sur le leader");
    }

    @Override
    public void notLeader() {

    }
}
