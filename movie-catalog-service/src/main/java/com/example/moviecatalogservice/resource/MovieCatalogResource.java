package com.example.moviecatalogservice.resource;

import com.example.moviecatalogservice.models.CatalogItems;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItems> getCatalog(@PathVariable String userId){
        //get all rated movies
        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+ userId, UserRating.class);

       return ratings.getUserRating().stream().map(rating ->{
        // for each movie id, call movie info service and get details
            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
//          Movie movie =  webClientBuilder.build()
//                   .get()
//                   .uri("http://localhost:8082/movies/" + rating.getMovieId())
//                   .retrieve()
//                   .bodyToMono(Movie.class)
//                   .block();

//          put them all together
          return new CatalogItems(movie.getName(), "description", rating.getRating());
               })
               .collect(Collectors.toList());

    }
}
