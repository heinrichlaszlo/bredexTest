package com.example.bredexTest.ad.controller;

import com.example.bredexTest.ad.dto.request.CreateAdRequestDTO;
import com.example.bredexTest.ad.model.Ad;
import com.example.bredexTest.ad.service.AdService;
import com.example.bredexTest.jwt.JwtTokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AdController {

    @Autowired
    private AdService adService;

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @PostMapping("/ad")
    public ResponseEntity<Long> createAd(@Valid @RequestBody CreateAdRequestDTO adCreateRequestDTO, @RequestHeader(value = "Authorization") String token) {
        if (!jwtTokenValidator.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Ad ad = adService.createAd(adCreateRequestDTO);
        return ResponseEntity.ok(ad.getId());
    }

    @DeleteMapping("/ad/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id, @RequestHeader(value = "Authorization") String token) {
        if (!jwtTokenValidator.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            adService.deleteAd(id);
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/ad/search")
    public ResponseEntity<List<String>> searchAds(@RequestParam(required = false) String brand, @RequestParam(required = false) String type) {


        try {
            List<String> adUrls = adService.searchAds(brand, type);
            return ResponseEntity.ok(adUrls);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/ad/get/{id}")
    public ResponseEntity<Ad> getAd(@PathVariable Long id) {
        try {
            Ad ad = adService.getAd(id);
            return ResponseEntity.ok(ad);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}