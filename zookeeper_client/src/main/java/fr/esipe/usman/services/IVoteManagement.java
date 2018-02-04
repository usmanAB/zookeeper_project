package fr.esipe.usman.services;

import org.apache.curator.framework.CuratorFramework;
import fr.esipe.usman.models.Client;

import java.util.List;

public interface IVoteManagement {

    Client addVoteToZookeeper(CuratorFramework zkClient, String path, String id) throws Exception;

    Client getClientById(String id,String path,CuratorFramework zkClient);

    boolean removeClientById(String id,String path,CuratorFramework zkClient);

    void loadClientsToZookeeper(Client c, String path,CuratorFramework zkClient);

    List<Client> getClientListFromZookeeper(CuratorFramework zkClient,String path);

//    boolean clientExists(CuratorFramework zkClient,String path);
}
