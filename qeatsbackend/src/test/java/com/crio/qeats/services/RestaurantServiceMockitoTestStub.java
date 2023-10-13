
package com.crio.qeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import com.crio.qeats.utils.FixtureHelpers;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)  // Annotation that must be used when you use Mockito
public class RestaurantServiceMockitoTestStub {

  protected static final String FIXTURES = "fixtures/exchanges";

  protected ObjectMapper objectMapper = new ObjectMapper();

  protected Restaurant restaurant1;
  protected Restaurant restaurant2;
  protected Restaurant restaurant3;
  protected Restaurant restaurant4;
  protected Restaurant restaurant5;

  @InjectMocks                       
  protected RestaurantServiceImpl restaurantService;

  //@InjectMocks  //Cannot instantiate @InjectMocks field named 'restaurantRepositoryServiceMock'! Cause: the type 'RestaurantRepositoryService' is an interface.
  @Mock
  protected RestaurantRepositoryService restaurantRepositoryServiceMock;

  @BeforeEach
  public void initializeRestaurantObjects() throws IOException {
    String fixture =
        FixtureHelpers.fixture(FIXTURES + "/mocking_list_of_restaurants.json");
    Restaurant[] restaurants = objectMapper.readValue(fixture, Restaurant[].class);
    // TODO CRIO_TASK_MODULE_MOCKITO
    //  What to do with this Restaurant[] ? Looks unused?
    //  Look for the "assert" statements in the tests
    //  following and find out what to do with the array.

    restaurant1 = restaurants[0];
    restaurant2 = restaurants[1];
    restaurant3 = restaurants[2];
    restaurant4 = restaurants[3];
    restaurant5 = restaurants[4];
  }



  @Test
  public void  testFindNearbyWithin5km() throws IOException {
    //TODO: CRIO_TASK_MODULE_MOCKITO
    // Following test case is failing, you have to
    // debug it, find out whats going wrong and fix it.
    // Notes - You can create additional mocks, setup the same and try out.


     when(restaurantRepositoryServiceMock
            .findAllRestaurantsCloseBy(any(Double.class), any(Double.class),
                eq(LocalTime.of(3, 0)),
                eq(5.0)))                          // here it is eq(1.0)))
            .thenReturn(Arrays.asList(restaurant1, restaurant2));
          // we mocked that we want 0th and 1st index in response when we hit below service method
    GetRestaurantsResponse allRestaurantsCloseBy = restaurantService
        .findAllRestaurantsCloseBy(new GetRestaurantsRequest(20.0, 30.0),
            LocalTime.of(3, 0));

    assertEquals(2, allRestaurantsCloseBy.getRestaurants().size());
    assertEquals("11", allRestaurantsCloseBy.getRestaurants().get(0).getRestaurantId());
    assertEquals("12", allRestaurantsCloseBy.getRestaurants().get(1).getRestaurantId());

    ArgumentCaptor<Double> servingRadiusInKms = ArgumentCaptor.forClass(Double.class);
    verify(restaurantRepositoryServiceMock, times(1))
        .findAllRestaurantsCloseBy(any(Double.class), any(Double.class), any(LocalTime.class),
            servingRadiusInKms.capture());

  }


  @Test
  public void  testFindNearbyWithin3km() throws IOException {

    //List<Restaurant> restaurant_mock_list1 = new ArrayList<>();
    List<String> attributes_list = new ArrayList<>();
    attributes_list.add("demo");
    List<Restaurant> restaurantList1 = new ArrayList<>();
    restaurantList1.add(new Restaurant("11", "11", "A2B", "Btm Layout", "www.google.com", 20.0269, 30.00, "123", "456", attributes_list));
    restaurantList1.add(new Restaurant("14", "14", "A2B", "Btm Layout", "www.google.com", 20.122, 30.11, "123", "456", attributes_list));
    // restaurantList1 = [
    //   {
    //     "id": "11",
    //     "restaurantId": "11",
    //     "name": "A2B",
    //     "city": "Btm Layout",
    //     "imageUrl": "www.google.com",
    //     "latitude": 20.0269,
    //     "longitude": 30.00,
    //     "attributes": [
    //       "Tamil",
    //       "South Indian"
    //     ]
    //   },
    //   {
    //     "id": "14",
    //     "restaurantId": "14",
    //     "name": "A2B",
    //     "city": "Btm Layout",
    //     "imageUrl": "www.google.com",
    //     "latitude": 20.122,
    //     "longitude": 30.11,
    //     "attributes": [
    //       "Tamil",
    //       "South Indian"
    //     ]
    //   }];


    List<Restaurant> restaurantList2 = new ArrayList<>();
    restaurantList2.add(new Restaurant("12", "12", "A2B", "Electronic City", "www.google.com", 20.0269, 30.00, "123", "456", attributes_list));
    restaurantList2.add(new Restaurant("14", "14", "A2B", "Btm Layout", "www.google.com", 20.122, 30.11, "123", "456", attributes_list));
    // restaurantList2 = [
    //   {
    //     "id": "11",
    //     "restaurantId": "11",
    //     "name": "A2B",
    //     "city": "Btm Layout",
    //     "imageUrl": "www.google.com",
    //     "latitude": 20.0269,
    //     "longitude": 30.00,
    //     "attributes": [
    //       "Tamil",
    //       "South Indian"
    //     ]
    //   },
    //   {
    //     "id": "12",
    //     "restaurantId": "12",
    //     "name": "A2B",
    //     "city": "Electronic City",
    //     "imageUrl": "www.google.com",
    //     "latitude": 20.015,
    //     "longitude": 30.015,
    //     "attributes": [
    //       "Tamil",
    //       "South Indian"
    //     ]
    //   }];

    // TODO: CRIO_TASK_MODULE_MOCKITO
    //  Initialize these two lists above such that I will match with the assert statements
    //  defined below.


    lenient().doReturn(restaurantList1)
        .when(restaurantRepositoryServiceMock)
        .findAllRestaurantsCloseBy(eq(20.0), eq(30.2), eq(LocalTime.of(3, 0)),
            eq(5.0));

    lenient().doReturn(restaurantList2)
        .when(restaurantRepositoryServiceMock)
        .findAllRestaurantsCloseBy(eq(21.0), eq(31.1), eq(LocalTime.of(19, 0)),
            eq(3.0));

    GetRestaurantsResponse allRestaurantsCloseByOffPeakHours;
    GetRestaurantsResponse allRestaurantsCloseByPeakHours;

    // TODO: CRIO_TASK_MODULE_MOCKITO
    //  Call restaurantService.findAllRestaurantsCloseBy with appropriate parameters such that
    //  Both of the mocks created above are called.
    //  Our assessment will verify whether these mocks are called as per the definition.
    //  Refer to the assertions below in order to understand the requirements better.

    //Done by me
    allRestaurantsCloseByOffPeakHours = restaurantService.findAllRestaurantsCloseBy(new GetRestaurantsRequest(20.0,30.2), LocalTime.of(3,0));
    allRestaurantsCloseByPeakHours = restaurantService.findAllRestaurantsCloseBy(new GetRestaurantsRequest(21.0,31.1), LocalTime.of(19,0));
    //Ends

    assertEquals(2, allRestaurantsCloseByOffPeakHours.getRestaurants().size());
    assertEquals("11", allRestaurantsCloseByOffPeakHours.getRestaurants().get(0).getRestaurantId());
    assertEquals("14", allRestaurantsCloseByOffPeakHours.getRestaurants().get(1).getRestaurantId());


    assertEquals(2, allRestaurantsCloseByPeakHours.getRestaurants().size());
    assertEquals("12", allRestaurantsCloseByPeakHours.getRestaurants().get(0).getRestaurantId());
    assertEquals("14", allRestaurantsCloseByPeakHours.getRestaurants().get(1).getRestaurantId());
  }

}

