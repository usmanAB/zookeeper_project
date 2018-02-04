package fr.esipe.usman.controller;

import fr.esipe.usman.models.Client;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;


@RestController
@RequestMapping(value = "/server")
public class HomeController {
    private final Logger logger = java.util.logging.Logger.getLogger("HomeController");
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    ServiceDiscovery<String> discovery;



    @RequestMapping(value = "/ServerStatus", method = RequestMethod.GET)
    public String getStatus() throws Exception {
        logger.info("Appel getStatus !");

        Collection<ServiceInstance<String>> services = discovery.queryForInstances("zookeeper_client");

        logger.info("Le service 'simple-vote-app-server' est fourni par "+ services.size() +" instance(s) dans Zookeeper");

        if (services.iterator().hasNext()) {
            // Nous utilisons par défaut le premier dans la liste
            ServiceInstance<String> serviceInstance = services.iterator().next();

            logger.info("version du service: "+ serviceInstance.getPayload());

            String serviceUrl = serviceInstance.buildUriSpec() + "/count/{number}";
            logger.info("appel de l'url: "+ serviceUrl);
           // score = restTemplate.getForObject(serviceUrl, Integer.class,number);

            return "Le service 'simple-vote-app-server' est fourni par "+ services.size() +" instance(s)";
        }

        return "Aucune instance disponible !";
    }

    @RequestMapping(value = "/addVote/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addVoteById(@PathVariable("id") String number) throws Exception {
        logger.info("APPEL METHODE count : "+number);
        Client c = new Client();
        Collection<ServiceInstance<String>> services = discovery.queryForInstances("zookeeper_client");
        logger.info("Score recu du client : "+ number);
        RestTemplate restTemplate = new RestTemplate();
        logger.info("Le service 'simple-vote-app-server' est fourni par "+ services.size()+" instance(s)" );

        if (services.iterator().hasNext()) {
            // Nous utilisons par défaut le premier dans la liste
            ServiceInstance<String> serviceInstance = services.iterator().next();

            logger.info("version du service: {}" + serviceInstance.getPayload());

            String serviceUrl = serviceInstance.buildUriSpec() + "/addVote/{id}";
            logger.info("appel de l'url: {}"+ serviceUrl);
            c = restTemplate.postForObject(serviceUrl,null,Client.class,number);
        }
        return new ResponseEntity<>(c, HttpStatus.OK);

    }

//build uri with destination path (see doc curator for discovery)
    public String buildUriByInstance(String dest_path) {
        Collection<ServiceInstance<String>> services = null;
        try {
            services = discovery.queryForInstances("zookeeper_client");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (services.iterator().hasNext()) {
            ServiceInstance<String> serviceInstance = services.iterator().next();
            return serviceInstance.buildUriSpec() + dest_path;
        }

        return "";

    }



    @RequestMapping(value = "/addVoteZoo/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addVoteZoo(@PathVariable("id") String id) {
        String serviceUrl = buildUriByInstance("/addVoteZoo/{id}");

        logger.info("Appel addVoteZoo ! PARAM _ID : "+id);

        return new ResponseEntity<>(restTemplate.postForObject(serviceUrl,null,Client.class,id), HttpStatus.OK);
    }

    @RequestMapping(value = "/displayVotes", method = RequestMethod.GET)
    public ResponseEntity<?> displayVotes() {
        logger.info("Appel displayVotes ! ");
    //TODO
        return null;

    }

    @RequestMapping(value = "/displayClient/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> displayClient(@PathVariable("id") String id) {
        logger.info("Appel displayClient ! ");
        String serviceUrl = buildUriByInstance("/displayClient/{id}");

        final Optional<Client> client = Optional.ofNullable(restTemplate.getForObject(serviceUrl,Client.class,id));
        logger.info("Appel displayClient ! "+client.get().toString());

        return (client!=null) ?
                new ResponseEntity<>(client.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value = "/deleteAllClients", method = RequestMethod.DELETE)
    public HttpStatus deleteClients() throws Exception {
        logger.info("Appel deleteClients ! ");
        String serviceUrl = buildUriByInstance("/deleteAllClients");

        HttpStatus status = restTemplate.getForObject(serviceUrl,HttpStatus.class);
        return status;
    }


    @RequestMapping(value = "/removeClient/{id}", method = RequestMethod.GET)
    public HttpStatus removeClientById(@PathVariable("id") String id) {
        logger.info("Appel removeClientById ! ");
        String serviceUrl = buildUriByInstance("/removeClient/{id}");
        HttpStatus status = restTemplate.getForObject(serviceUrl,HttpStatus.class,id);
        return status;
    }

    @RequestMapping(value = "/addClient", method = RequestMethod.POST)
    public ResponseEntity<Client> addClient(@RequestBody Client c) {
        logger.info("Appel addClient ! ");
        String serviceUrl = buildUriByInstance("/addClient");

        Client client = restTemplate.postForObject(serviceUrl,c,Client.class);


        return (client!=null) ?
                new ResponseEntity<>(client, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }














    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public void erreur_server() {
        logger.info("Erreur sur l'appel ! "+Throwable.class.toString());
    }
}
