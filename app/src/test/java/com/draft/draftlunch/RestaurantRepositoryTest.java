package com.draft.draftlunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.Services.RestaurantRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class RestaurantRepositoryTest {


    // mock creation

    RestaurantRepository mockedRestaurantRepository ;
    Result result = new Result();
    List<Result> listTest = new ArrayList<Result>();

    @Before
    @Test
    public void setup(){

        mockedRestaurantRepository = mock(RestaurantRepository.class);
        listTest.add(result);
        mockedRestaurantRepository.setMyRestaurants(listTest);
        assertEquals(mockedRestaurantRepository.getMyRestaurants().getValue().size(), listTest.size()); ;
    }

    @Test
    public void testFetchRestaurants(){
        mockedRestaurantRepository.FetchRestaurants("48.8630,2.3320");
        assertEquals(result.getTypes(), mockedRestaurantRepository.getMyRestaurants().getValue().get(0).getTypes());
    }

    @Test
    public void testFetchDetail(){
        mockedRestaurantRepository.FetchDetail("ChIJL4UVCSdu5kcRZP_k3fkFhoY");

        assertEquals("01 43 54 50 04", RestaurantRepository.getDetailRestaurant().getFormattedPhoneNumber());
        assertEquals("https://calife.com/", RestaurantRepository.getDetailRestaurant().getWebsite());
    }

    @Test
    public void testGetAddressRestaurant(){
        mockedRestaurantRepository.FetchRestaurants("48.8630,2.3320");
        assertEquals("Port des Saints-Pères, 75006 Paris", RestaurantRepository.getAddressRestaurant("Le Calife"));
    }
}
