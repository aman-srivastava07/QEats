
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;


  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  // @Override
  // public GetRestaurantsResponse findAllRestaurantsCloseBy(GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) 
  // {
  //   Integer hour = currentTime.getHour();
  //   Integer min = currentTime.getMinute();
  //   List<Restaurant> available_restaurants;

  //   if((hour >=8 && hour <=9) || (hour == 10 && min == 0) || (hour == 13) || (hour == 14 && min == 0) || (hour >=19 && hour <=20) || (hour == 21 && min == 0))
  //   {
  //     //peak hours
  //     available_restaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), currentTime, peakHoursServingRadiusInKms);
  //     //GetRestaurantsResponse getRestaurantsResponse = new GetRestaurantsResponse();
  //   }
  //   else
  //   {
  //     available_restaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), currentTime, normalHoursServingRadiusInKms);
  //     //GetRestaurantsResponse getRestaurantsResponse = new GetRestaurantsResponse();
  //   }

  //   GetRestaurantsResponse getRestaurantsResponse = new GetRestaurantsResponse(available_restaurants);

  //   return getRestaurantsResponse;
  // }

  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) 
  {
      List <Restaurant> storedRestaurants;
    
      if(currentTime.isAfter(LocalTime.of(7, 59, 59)) && currentTime.isBefore(LocalTime.of(10, 0, 1)) ||
      currentTime.isAfter(LocalTime.of(12, 59, 59)) && currentTime.isBefore(LocalTime.of(14, 0, 1)) ||
      currentTime.isAfter(LocalTime.of(18,59,59)) && currentTime.isBefore(LocalTime.of(21,0,1)))
      {
        storedRestaurants=restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), currentTime,peakHoursServingRadiusInKms);
      }
     else
     {
      storedRestaurants=restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), currentTime,normalHoursServingRadiusInKms);
     }

      return new GetRestaurantsResponse(storedRestaurants);
  }

  private boolean isTimeWithInRange(LocalTime timeNow,LocalTime startTime, LocalTime endTime) 
  {
    return timeNow.isAfter(startTime) && timeNow.isBefore(endTime);
  }

  public boolean isPeakHour(LocalTime timeNow) 
  {
    return isTimeWithInRange(timeNow, LocalTime.of(7, 59, 59), LocalTime.of(10, 00, 01)) || isTimeWithInRange(timeNow, LocalTime.of(12, 59, 59), LocalTime.of(14, 00, 01))
        || isTimeWithInRange(timeNow, LocalTime.of(18, 59, 59), LocalTime.of(21, 00, 01));
  }


 


  // TODO: CRIO_TASK_MODULE_RESTAURANTSEARCH
  // Implement findRestaurantsBySearchQuery. The request object has the search string.
  // We have to combine results from multiple sources:
  // 1. Restaurants by name (exact and inexact)
  // 2. Restaurants by cuisines (also called attributes)
  // 3. Restaurants by food items it serves
  // 4. Restaurants by food item attributes (spicy, sweet, etc)
  // Remember, a restaurant must be present only once in the resulting list.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findRestaurantsBySearchQuery(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {

      List<List<Restaurant>> restaurantAnsList=new ArrayList<>();  

      Double servingRadiusInKms = isPeakHour(currentTime) ? peakHoursServingRadiusInKms : normalHoursServingRadiusInKms;

      String searchFor = getRestaurantsRequest.getSearchFor();

      Set<String> restaurantSet = new HashSet<>();

      List<Restaurant> restaurantList = new ArrayList<>();
      
      if(!searchFor.isEmpty())
      {
        restaurantAnsList.add(restaurantRepositoryService.findRestaurantsByName(getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), searchFor, currentTime, servingRadiusInKms));

        //By Attributes
        restaurantAnsList.add(restaurantRepositoryService.findRestaurantsByAttributes(getRestaurantsRequest.getLatitude(),
              getRestaurantsRequest.getLongitude(), searchFor, currentTime, servingRadiusInKms));

        //By ItemName
        restaurantAnsList.add(restaurantRepositoryService.findRestaurantsByItemName(getRestaurantsRequest.getLatitude(),
            getRestaurantsRequest.getLongitude(), searchFor, currentTime, servingRadiusInKms));

        //By ItemAttributes
        restaurantAnsList.add(restaurantRepositoryService.findRestaurantsByItemAttributes(getRestaurantsRequest.getLatitude(),
            getRestaurantsRequest.getLongitude(), searchFor, currentTime, servingRadiusInKms));
        
          for(List<Restaurant> ListRestIter:restaurantAnsList)
          {

              for(Restaurant restListRestIterIter: ListRestIter)
              {

                  if(!restaurantSet.contains(restListRestIterIter.getRestaurantId()))
                  {
                    restaurantSet.add(restListRestIterIter.getName());
                    restaurantList.add(restListRestIterIter);
                  }
              }
          }

        return new GetRestaurantsResponse(restaurantList);

      }
      else
      {

        return new GetRestaurantsResponse(new ArrayList<>());
      
      }
  }


  // @Override
  // public GetRestaurantsResponse findRestaurantsBySearchQueryMt(
  //     GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
  //   // TODO Auto-generated method stub
  //   return null;
  // }

  //}

  // TODO: CRIO_TASK_MODULE_MULTITHREADING
  // Implement multi-threaded version of RestaurantSearch.
  // Implement variant of findRestaurantsBySearchQuery which is at least 1.5x time faster than
  // findRestaurantsBySearchQuery.
  @Override
  public GetRestaurantsResponse findRestaurantsBySearchQueryMt(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) 
  {
    Double servingRadiusInKms = isPeakHour(currentTime) ? peakHoursServingRadiusInKms : normalHoursServingRadiusInKms;
     
        String searchFor = getRestaurantsRequest.getSearchFor();
        List<Restaurant> restaurantsByNameHolder=new ArrayList<>();
        List<Restaurant> restaurantsByAttriHolder=new ArrayList<>(); 
        List<Restaurant> restaurantList = new ArrayList<>();
  
        if(!searchFor.isEmpty())
        {
  
        Future<List<Restaurant>> futureByName= restaurantRepositoryService.findRestaurantsByNameAsync(
        getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(),
        searchFor, currentTime, servingRadiusInKms);
  
        Future<List<Restaurant>> futureByAttributeList = restaurantRepositoryService.findRestaurantsByAttributesAsync(getRestaurantsRequest.getLatitude(),
        getRestaurantsRequest.getLongitude(), searchFor, currentTime, servingRadiusInKms);
  
         while(futureByName.isDone() && futureByAttributeList.isDone())
         {
  
          try 
          {
            
            restaurantsByNameHolder=futureByName.get();
            restaurantsByAttriHolder=futureByAttributeList.get();
  
          } catch (InterruptedException e) 
          {
  
            return new GetRestaurantsResponse(new ArrayList<>());
  
          } catch (ExecutionException e) 
          {
            
            return new GetRestaurantsResponse(new ArrayList<>());
  
          }
  
         }
  
        restaurantList= Stream.concat(restaurantsByNameHolder.stream(), restaurantsByAttriHolder.stream()).collect(Collectors.toList());
  
      }
      else
  
        restaurantList=new ArrayList<>();
  
        return new GetRestaurantsResponse(restaurantList);
  }
}

