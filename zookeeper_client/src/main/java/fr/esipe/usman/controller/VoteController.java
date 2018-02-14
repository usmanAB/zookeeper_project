package fr.esipe.usman.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esipe.usman.ServiceDiscoveryConfiguration;
import fr.esipe.usman.services.VoteManagement;
import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.esipe.usman.models.Client;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;



@RestController
@RequestMapping(value = "/votes")
public class VoteController {
    private final Logger logger = Logger.getLogger("VoteController");
    private CuratorFramework zkClient;

    @Value("${zookeeper.hosts}")
    private String zookeeper_hosts;


    private static final String LIST_CLIENT_PATH = "/services/listClient";
    private final VoteManagement voteManagement;

    @Autowired
    public VoteController(VoteManagement voteManagement){
        this.voteManagement=voteManagement;
        this.connect();
    }

    private void connect() {
        try{
            logger.warning("Open connection... ");

            zkClient = CuratorFrameworkFactory.newClient("192.168.43.201,192.168.43.222,192.168.43.206", new ExponentialBackoffRetry(1000, 3));
            zkClient.start();
            logger.warning("Zookeeper client started ! ... "+zkClient.getState().toString());


        }catch (Exception e){
            zkClient.close();
            logger.warning("Handle exception "+e);
        }
    }


    @RequestMapping(value = "/addVoteZoo/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addVoteZoo(@PathVariable("id") String id) throws Exception {
        logger.info("Appel addVoteZoo ! PARAM _ID : "+id);

        return new ResponseEntity<>(voteManagement.addVoteToZookeeper(zkClient,LIST_CLIENT_PATH,id), HttpStatus.OK);
    }

    @RequestMapping(value = "/displayVotes", method = RequestMethod.GET)
    public ResponseEntity<?> displayVotes() {
        logger.info("Appel displayVotes ! ");

        final Optional<List<Client>> clientsList = Optional.ofNullable(voteManagement.getClientListFromZookeeper(zkClient,LIST_CLIENT_PATH));

        return (clientsList.isPresent()) ?
                new ResponseEntity<>(clientsList.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @RequestMapping(value = "/displayClient/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> displayClient(@PathVariable("id") String id) {
        logger.info("Appel displayClient ! ");

        final Optional<Client> client = Optional.ofNullable(voteManagement.getClientById(id,LIST_CLIENT_PATH,zkClient));

        return (client!=null) ?
                new ResponseEntity<>(client.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/loadClients", method = RequestMethod.GET)
    public HttpStatus loadClients() throws Exception {
        logger.info("Appel loadClients ! ");
        Client c = new Client(0,"papa");
        voteManagement.loadClientsToZookeeper(c, LIST_CLIENT_PATH,zkClient);

        return voteManagement.pathExists(zkClient,LIST_CLIENT_PATH+"/"+c.getId()) ? HttpStatus.OK : HttpStatus.NO_CONTENT;

    }

    @RequestMapping(value = "/deleteAllClients", method = RequestMethod.GET)
    public HttpStatus deleteClients() throws Exception {
        logger.info("Appel deleteClients ! ");
        zkClient.delete().deletingChildrenIfNeeded().forPath("/services/listClient");
        return !voteManagement.pathExists(zkClient,LIST_CLIENT_PATH) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }


    @RequestMapping(value = "/removeClient/{id}", method = RequestMethod.GET)
    public HttpStatus removeClientById(@PathVariable("id") String id) {
        logger.info("Appel removeClientById ! ");
        return voteManagement.removeClientById(id,LIST_CLIENT_PATH,zkClient) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

    @RequestMapping(value = "/addClient", method = RequestMethod.POST)
    public ResponseEntity<Client> addClient(@RequestBody Client c) {
        logger.info("Appel addClient ! ");
        Client client = new Client(c.getVote(),c.getLogin());

        return new ResponseEntity<Client>(voteManagement.addClientToZookeeper(client, LIST_CLIENT_PATH,zkClient),HttpStatus.OK);

    }






    @RequestMapping(value = "/count/{number}", method = RequestMethod.GET)
    public int score(@PathVariable("number") int num) throws Exception {
        int temp = 0;

        // zk = new ZooKeeper(3000,3000);

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.43.85,192.168.43.164,192.168.43.96", retryPolicy);
        client.start();


        ObjectMapper mapper = new ObjectMapper();
        int temp2=0;

        if(client.checkExists().forPath("/data2") instanceof Stat){
            System.out.println("NODE EST REMPLIT !");

            byte[] outputBytes = client.getData().forPath("/data2");
            List<String> outputList = mapper.readValue(outputBytes, ArrayList.class);
            temp2=Integer.valueOf(outputList.get(0));
            temp2++;
            outputList.add(String.valueOf(temp2));

            byte[] writeValueAsBytes = mapper.writeValueAsBytes(outputList);
            client.setData().forPath("/data2", writeValueAsBytes);

        }
        else {
            System.out.println("NODE EST VIDE !");
            client.create()
                    .creatingParentsIfNeeded()
                    .forPath("/data2");

            List<String> list = new ArrayList<>();
            list.add("0");
            try
            {
                byte[] writeValueAsBytes = mapper.writeValueAsBytes(list);
                client.setData().forPath("/data2", writeValueAsBytes);

                ObjectMapper mapper2 = new ObjectMapper();



            } catch (Exception exception)
            {
                exception.printStackTrace();
            }


        }
        ObjectMapper mapper2 = new ObjectMapper();

        byte[] outputBytes = client.getData().forPath("/data2");
        List<String> outputList = mapper2.readValue(outputBytes, ArrayList.class);
        System.out.println(outputList);
        return temp;
    }
}
