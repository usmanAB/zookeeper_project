package fr.esipe.usman.services;

import org.apache.curator.framework.CuratorFramework;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import fr.esipe.usman.models.Client;
import java.util.List;
import java.util.logging.Logger;

@Service
public class VoteManagement implements IVoteManagement{
    private final Logger logger = Logger.getLogger("VoteManagement");

    public Client addVoteToZookeeper(CuratorFramework zkClient,String path ,String id2) throws Exception {
        int count;
        logger.info("APPEL addVoteToZookeeper");
        Client clientModified =null;
        if(!pathExists(zkClient,path)){
            zkClient.create().forPath("/services/listClient", new byte[0]);
        }

//        List<Client> cl = getClientListFromZookeeper(zkClient,path);
//        for (int i = 0; i < cl.size(); i++) {
//            if (cl.get(i).getId().equals(id2)) {
//                count = cl.get(i).getVote();
//                cl.get(i).setVote(count + 1);
//                clientModified = cl.get(i);
//            }
//        }

//        List<?> cl2 = getClientListFromZookeeper(zkClient,path)
//                .stream()
//                .filter(client -> id2.equals(client.getId())).collect(Collectors.toList());



//        loadClientsToZookeeper(cl,path,zkClient);

        logger.info("Client trouvé ! : ");
        return clientModified;
    }




    public Client getClientById(String id, String path,CuratorFramework zkClient) {
        logger.info("APPEL getClientById");
        ObjectMapper mapper = new ObjectMapper();
        try {
            if(pathExists(zkClient,path+"/"+id)){
                return mapper.readValue(zkClient.getData().forPath(path+"/"+id),Client.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean removeClientById(String id, String path,CuratorFramework zkClient) {
        logger.info("APPEL remmoveClientById");
        try {
            if(pathExists(zkClient,path+"/"+id)){
                zkClient.delete().forPath(path+"/"+id);
                logger.info("SUPPRESSION CLIENT : "+id);

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public Client addClientToZookeeper(Client c, String path,CuratorFramework zkClient){
        ObjectMapper mapper = new ObjectMapper();
        Client client_added = c;

        logger.info("AJOUT CLIENT : "+client_added.getId());

        try {
            byte[] writeValueAsBytes = mapper.writeValueAsBytes(client_added);

            if(!pathExists(zkClient,path+"/"+c.getId())){
                zkClient.create().creatingParentsIfNeeded().forPath(path+"/"+c.getId());
            }
            zkClient.setData().forPath(path+"/"+c.getId(), writeValueAsBytes);
            logger.info("AJOUT CLIENT : "+client_added.getId());


            client_added =  mapper.readValue(zkClient.getData().forPath(path+"/"+c.getId()),Client.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client_added;
    }

    public List<Client> getClientListFromZookeeper(CuratorFramework zkClient,String path) {
        List<Client> outputList= null;
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            byte[] outputBytes = zkClient.getData().forPath(path);
            outputList = mapper.readValue(outputBytes, List.class);

        } catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return outputList;
    }

//    public Client getClientFromZookeeper(CuratorFramework zkClient,String path,String id) {
//        List<Client> outputList= null;
//        ObjectMapper mapper = new ObjectMapper();
//
//        try
//        {
//            byte[] outputBytes = zkClient.getData().forPath(path);
//            outputList = mapper.readValue(outputBytes, Client.class);
//
//        } catch (Exception exception)
//        {
//            exception.printStackTrace();
//        }
//
//
//
//        return outputList;
//    }



    public void loadClientsToZookeeper(Client c,String path,CuratorFramework zkClient){
        ObjectMapper mapper = new ObjectMapper();
        Client client = c;
        try {
            byte[] writeValueAsBytes = mapper.writeValueAsBytes(client);
            if(!pathExists(zkClient,path+"/"+c.getId())){
                zkClient.create().creatingParentsIfNeeded().forPath(path+"/"+c.getId(), new byte[0]);
            }
            zkClient.setData().forPath(path+"/"+c.getId(), writeValueAsBytes);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

//Check if client exists with path
    public boolean pathExists(CuratorFramework client,String path) throws Exception {
        return client.checkExists().forPath(path)!=null;
    }







}
