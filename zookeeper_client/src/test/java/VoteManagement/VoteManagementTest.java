package VoteManagement;


import fr.esipe.usman.ServiceDiscoveryConfiguration;
import fr.esipe.usman.services.VoteManagement;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.logging.Logger;


public class VoteManagementTest{
    private final Logger logger = Logger.getLogger("VoteManagementTest");

    private CuratorFramework zkClient;
    private ServiceDiscoveryConfiguration service;

    private VoteManagement voteManagement;


    @Autowired(required = false)
    @Value("${zookeeper.hosts}")
    private String zookeeper_hosts;

/*    @Before
    public void init2(){
        service = new ServiceDiscoveryConfiguration();
        voteManagement = new VoteManagement();
        zkClient = CuratorFrameworkFactory.newClient(zookeeper_hosts, new ExponentialBackoffRetry(1000, 3));
        zkClient.start();

    }



    @Test
    public void testAddClientToZookeeper(){
        logger.info(zkClient.getNamespace());
        //TODO
    }

    @Test
    public void testDeleteClientToZookeeper(){
        logger.info(zkClient.getNamespace());
        //TODO
    }*/



/*    @Test
    public void testGetClientToZookeeper(){
        logger.info(zkClient.getNamespace());
        //TODO
    }



    @Test
    public void testDeleteAllClientToZookeeper(){
        logger.info(zkClient.getNamespace());
        //TODO
    }*/

}