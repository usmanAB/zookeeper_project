package VoteManagement;


import fr.esipe.usman.ServiceDiscoveryConfiguration;
import fr.esipe.usman.models.Client;
import fr.esipe.usman.services.VoteManagement;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class VoteManagementTest{
    private final Logger logger = Logger.getLogger("VoteManagementTest");


    private CuratorFramework zkClient;
    private VoteManagement voteManagement;
    private Client c;

    @Autowired(required = false)
    @Value("${zookeeper.hosts}")
    private String zookeeper_hosts;

   @Before
    public void init2(){
        voteManagement = mock(VoteManagement.class);
        zkClient = mock(CuratorFramework.class);
        c = mock(Client.class);

   }


    @Test
    public void testAddClientToZookeeper(){
        logger.info("testAddClientToZookeeper");

        when(voteManagement.addClientToZookeeper(c,"",zkClient)).thenReturn(c);
        Assert.assertEquals(c,voteManagement.addClientToZookeeper(c,"",zkClient));
    }

    @Test
    public void testDeleteClientToZookeeper(){
        when(voteManagement.removeClientById("","",zkClient)).thenReturn(false);

        Assert.assertFalse(voteManagement.removeClientById("","",zkClient));
    }



    @Test
    public void getClientListFromZookeeper(){
        logger.info("getClientListFromZookeeper");
        ArrayList<Client> cl = new ArrayList<>();
        when(voteManagement.getClientListFromZookeeper(zkClient,"")).thenReturn(cl);
        Assert.assertEquals(cl,voteManagement.getClientListFromZookeeper(zkClient,""));
    }





    @Test
    public void testAddVoteToZookeeper() throws Exception {
        logger.info("testAddVoteToZookeeper");
        when(voteManagement.addVoteToZookeeper(zkClient,"","")).thenReturn(c);
        Assert.assertEquals(c,voteManagement.addVoteToZookeeper(zkClient,"",""));
    }
}