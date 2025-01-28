package in.dataman.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.dataman.encryptionDecryption.EncryptionDecryptionUtil;
import in.dataman.entity.Candidate;
import in.dataman.service.CandidateService;
import in.dataman.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
}
