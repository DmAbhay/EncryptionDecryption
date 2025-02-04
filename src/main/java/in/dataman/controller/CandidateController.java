package in.dataman.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import in.dataman.encryptionDecryption.EncryptionDecryptionUtil;
import in.dataman.entity.Candidate;
import in.dataman.exception.JsonNodeException;
import in.dataman.exception.MissingFieldException;
import in.dataman.service.CandidateService;
import in.dataman.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Optional;


@RestController
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private EncryptionDecryptionUtil encryptionDecryptionUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/get-secret-key")
    public SecretKey getSecretKey() {
        return encryptionDecryptionUtil.getSecretKey(); // Send the secret key to the frontend
    }

    @GetMapping("/test-new")
    public String test(){
        return "Jai Shree Ram";
    }



    @PostMapping("/save-candidate-details")
    public ResponseEntity<?> saveCandidateDetail(@RequestBody JsonNode payload) throws Exception {


        String encryptedPayload = payload.get("encryptedResponse").asText();

        // Decrypt the encrypted message
        String decryptedPayload = encryptionDecryptionUtil.decryptResponse(encryptedPayload);

        // Convert the decrypted string back to a JsonNode
        JsonNode candidateData = objectMapper.readTree(decryptedPayload);


        Candidate candidate = new Candidate();
        candidate.setAge(candidateData.get("age").asInt());
        candidate.setName(candidateData.get("name").asText());
        candidate.setSalary(candidateData.get("salary").asDouble());
        System.out.println("Ashwani pandey!!!!!!!!!!!!!!!!!!!!!!!");
        return ResponseEntity.ok(candidateService.addCandidateDetails(candidate));
    }

    @GetMapping("/get-all-candidate")
    public ResponseEntity<?>  getAllCandidate(){

        //return ResponseEntity.ok(candidateService.getAllCandidateDetails());
        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the object to JsonNode
        JsonNode jsonNode = objectMapper.valueToTree(candidateService.getAllCandidateDetails());


        try {
            // Convert JsonNode to a string
            String jsonString = objectMapper.writeValueAsString(jsonNode);

            // Encrypt the JSON string
            String encryptedMessage = encryptionDecryptionUtil.encryptMessage(jsonString);

            HashMap<String, String> result = new HashMap<>();

            result.put("encryptedResponse", encryptedMessage);

            // Return the encrypted string
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error encrypting JSON: " + e.getMessage());
        }
    }

    @GetMapping("/get-candidate-by-id")
    public ResponseEntity<?> getCandidateById(@RequestParam Long id){
        Optional<Candidate> candidate = candidateService.findByCandidateId(id);
        if(candidate.isEmpty()){
            return ResponseEntity.ok("SPECIFIED CANDIDATE NOT FOUND");
        }else{
            //return ResponseEntity.ok(candidate);


            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert the object to JsonNode
            JsonNode jsonNode = objectMapper.valueToTree(candidate.get());


            try {
                // Convert JsonNode to a string
                String jsonString = objectMapper.writeValueAsString(jsonNode);

                // Encrypt the JSON string
                String encryptedMessage = encryptionDecryptionUtil.encryptMessage(jsonString);

                HashMap<String, String> result = new HashMap<>();

                result.put("encryptedResponse", encryptedMessage);

                // Return the encrypted string
                return ResponseEntity.ok(result);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error encrypting JSON: " + e.getMessage());
            }
        }
    }

    @PostMapping("/post-candidate-details-test-01")
    public ResponseEntity<?> postCandiate(@RequestBody JsonNode payload){


        ObjectNode responseNode = objectMapper.createObjectNode();

        // Validate required fields
        if (!payload.has("id") || !payload.has("name") || !payload.has("gender") || !payload.has("age")) {
            responseNode.put("error", "Missing required fields");
            responseNode.put("status", "failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseNode);
        }


        Long id = payload.get("id").asLong();
        String name = payload.get("name").asText();
        String gender = payload.get("gender").asText();
        Integer age = payload.get("age").asInt();



        responseNode.put("id", id);
        responseNode.put("Your Name", name);
        responseNode.put("gender", gender);
        responseNode.put("age", age);

        return ResponseEntity.ok(responseNode);


    }


    @PostMapping("/post-candidate-details-test-02")
    public ResponseEntity<?> postCandiateDataq(@RequestBody JsonNode payload){

        ObjectNode responseNode = objectMapper.createObjectNode();

        try{
            // Validate required fields and throw MissingFieldException if any are missing
            if (!payload.has("id") || !payload.has("name") || !payload.has("gender") || !payload.has("age")) {
                throw new MissingFieldException("Missing required fields: id, name, gender, age");
            }

            Long id = payload.get("id").asLong();
            String name = payload.get("name").asText();
            String gender = payload.get("gender").asText();
            Integer age = payload.get("age").asInt();

            responseNode.put("id", id);
            responseNode.put("Your Name", name);
            responseNode.put("gender", gender);
            responseNode.put("age", age);

            return ResponseEntity.ok(responseNode);

        }catch (MissingFieldException e){
            return ResponseEntity.ok("Some field in json is Missing");
        }
    }

    @PostMapping("/post-candidate-details-test-03")
    public ResponseEntity<?> postCandiateDatar(@RequestBody JsonNode payload) {

        ObjectNode responseNode = objectMapper.createObjectNode();

        // Validate required fields and throw MissingFieldException if any are missing
        if (!payload.has("id") || !payload.has("name") || !payload.has("gender") || !payload.has("age")) {
            throw new MissingFieldException("Missing required fields: id, name, gender, age");
        }

        Long id = payload.get("id").asLong();
        String name = payload.get("name").asText();
        String gender = payload.get("gender").asText();
        Integer age = payload.get("age").asInt();

        responseNode.put("id", id);
        responseNode.put("Your Name", name);
        responseNode.put("gender", gender);
        responseNode.put("age", age);

        return ResponseEntity.ok(responseNode);
    }

    @PostMapping("/post-candidate-details-test-04")
    public ResponseEntity<?> postCandiateDatas(@RequestBody JsonNode payload) {

        ObjectNode responseNode = objectMapper.createObjectNode();

        // Validate required fields and throw MissingFieldException if any are missing
        if (!payload.has("id") || !payload.has("name") || !payload.has("gender") || !payload.has("age")) {
            throw new JsonNodeException("missing field");
        }

        Long id = payload.get("id").asLong();
        String name = payload.get("name").asText();
        String gender = payload.get("gender").asText();
        Integer age = payload.get("age").asInt();

        responseNode.put("id", id);
        responseNode.put("Your Name", name);
        responseNode.put("gender", gender);
        responseNode.put("age", age);

        return ResponseEntity.ok(responseNode);
    }
}
