package VoteManagement;


import fr.esipe.usman.ServiceDiscoveryConfiguration;
import fr.esipe.usman.services.VoteManagement;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;


public class VoteManagementTest{
    private final Logger logger = Logger.getLogger("VoteManagementTest");

    private CuratorFramework zkClient;
    private ServiceDiscoveryConfiguration service;

    private VoteManagement voteManagement;

    @Before
    public void init(){
        service = new ServiceDiscoveryConfiguration();
        voteManagement = new VoteManagement();
        zkClient = CuratorFrameworkFactory.newClient("192.168.43.85,192.168.43.164,192.168.43.156", new ExponentialBackoffRetry(1000, 3));
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
    }



    @Test
    public void testGetClientToZookeeper(){
        logger.info(zkClient.getNamespace());
        //TODO
    }



    @Test
    public void testDeleteAllClientToZookeeper(){
        logger.info(zkClient.getNamespace());
        //TODO
    }

}