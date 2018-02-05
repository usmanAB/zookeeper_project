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

@Configuration
public class ServiceDiscoveryClientConfiguration {

    @Value("${zookeeper_hosts}")
    private String zookeeper_hosts;

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

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curator() {
        return CuratorFrameworkFactory.newClient("192.168.43.201,192.168.43.222,192.168.43.206", new ExponentialBackoffRetry(1000, 6));
    }

}
