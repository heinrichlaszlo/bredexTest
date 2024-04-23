package com.example.bredexTest.ad.service;

import com.example.bredexTest.ad.dto.request.CreateAdRequestDTO;
import com.example.bredexTest.ad.model.Ad;
import com.example.bredexTest.ad.repository.AdRepository;
import com.example.bredexTest.user.model.User;
import com.example.bredexTest.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Ad entities

 */
@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;
    /**
     * This method is used to create a new Ad.
     * It first retrieves the current authenticated user's email from the SecurityContext.
     * Then, it builds a new Ad object using the provided CreateAdRequestDTO object and the retrieved email.
     * Finally, it saves the new Ad object to the database using the AdRepository and returns the saved Ad.
     *
     * @param createAdRequestDTO This is a DTO object that contains the data for the new Ad.
     * @return Ad This returns the newly created Ad object that has been saved to the database.
     */
    @Transactional
    public Ad createAd(CreateAdRequestDTO createAdRequestDTO) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        return adRepository.save(Ad.builder().type(createAdRequestDTO.getType()).brand(createAdRequestDTO.getBrand()).description(createAdRequestDTO.getDescription()).price(createAdRequestDTO.getPrice()).owner(currentEmail).build());
    }
    /**
     * This method is used to delete an existing Ad.
     * It first retrieves the Ad from the database using the provided adId. If the Ad does not exist, it throws an IllegalArgumentException.
     * Then, it retrieves the current authenticated user's email from the SecurityContext and finds the User in the database. If the User does not exist, it throws an IllegalArgumentException.
     * It checks if the owner of the Ad is the same as the current user's email. If not, it throws a SecurityException.
     * Finally, it deletes the Ad from the database using the AdRepository.
     *
     * @param adId This is the id of the Ad to be deleted.
     * @throws IllegalArgumentException if the Ad or the User does not exist.
     * @throws SecurityException if the current user is not the owner of the Ad.
     */
    @Transactional
    public void deleteAd(Long adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found"));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!ad.getOwner().equals(currentUser.getEmail())) {
            throw new SecurityException("User not authorized to delete this ad");
        }

        adRepository.delete(ad);
    }

    /**
     * This method is used to search for Ads based on the provided brand and type.
     * It first retrieves all Ads from the database.
     * Then, it filters the Ads based on the provided brand and type. If the brand or type is null, it does not filter based on that parameter.
     * It maps each Ad to a URL string that can be used to retrieve the Ad.
     * Finally, it returns a list of the URL strings.
     *
     * @param brand This is the brand to filter the Ads by. If it is null, the method does not filter by brand.
     * @param type This is the type to filter the Ads by. If it is null, the method does not filter by type.
     * @return List<String> This returns a list of URL strings that can be used to retrieve the Ads that match the provided brand and type.
     */
    public List<String> searchAds(String brand, String type) {
        List<Ad> ads = adRepository.findAll();

        return ads.stream()
                .filter(ad -> brand == null || ad.getBrand().toLowerCase().contains(brand.toLowerCase()))
                .filter(ad -> type == null || ad.getType().toLowerCase().contains(type.toLowerCase()))
                .map(ad -> "http://localhost:8080/ad/get/" + ad.getId())
                .collect(Collectors.toList());
    }

    /**
     * This method is used to retrieve an Ad from the database.
     * It uses the provided adId to find the Ad in the database.
     * If the Ad does not exist, it throws an IllegalArgumentException.
     *
     * @param adId This is the id of the Ad to be retrieved.
     * @return Ad This returns the Ad object that matches the provided id.
     * @throws IllegalArgumentException if the Ad does not exist.
     */
    public Ad getAd(Long adId) {
        return adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found"));
    }
}
