package am.ysu.encryptorsa.controller;

import lombok.RequiredArgsConstructor;
import am.ysu.encryptorsa.model.Base64RSAKey;
import am.ysu.encryptorsa.service.MainService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/algo")
@RequiredArgsConstructor
public class DataController {
    private final MainService mainService;

    @ResponseBody
    @GetMapping("/generate-keys")
    public Base64RSAKey generateKeyPair(@RequestParam("bitLength") int bitLength) {
        return mainService.generateKeyPair(bitLength);
    }

    @ResponseBody
    @GetMapping("/encrypt-text")
    public Map<String, String> encryptText(@RequestParam String message, @RequestParam String publicKey) {
        return mainService.encryptText(message, publicKey);
    }

    @ResponseBody
    @GetMapping("/decrypt-text")
    public Map<String, String> decryptText(@RequestParam String cipher, @RequestParam String privateKey) {
        return mainService.decryptText(cipher, privateKey);
    }

    @ResponseBody
    @PostMapping("/download-key")
    public ResponseEntity<byte[]> downloadKey(@RequestParam Boolean isPublic, @RequestBody String key) {
        String pemContent = mainService.convertToPEM(isPublic, key);
        byte[] pemBytes = pemContent.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", (isPublic ? "public-" : "private-") + "key.pem");
        return ResponseEntity.ok().headers(headers).body(pemBytes);
    }


}
