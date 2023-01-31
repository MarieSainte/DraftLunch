package com.draft.draftlunch;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.draft.draftlunch.Services.UserRepository;
import com.draft.draftlunch.ui.lunch.LunchActivity;
import com.draft.draftlunch.ui.lunch.LunchViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class LunchViewModelTest {

    @Mock
    UserRepository userRepository;
    @Mock
    LunchActivity activity;
    LunchViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new LunchViewModel(userRepository);
    }
    @Test
    public void signOutTest(){
        when(viewModel.signOut(activity)).thenReturn(null);
        assertNull(viewModel.signOut(activity));
    }
    @Test
    public void getLocationTest() {
        when(viewModel.getLocation()).thenReturn(null);
        assertNull(viewModel.getLocation());
    }

    @Test
    public void getCurrentUserTest() {
        when(viewModel.getCurrentUser()).thenReturn(null);
        assertNull(viewModel.getCurrentUser());
    }

    @Test
    public void getSpecificRestaurantTest() {
        when(viewModel.getSpecificRestaurant("Le Calife")).thenReturn(null);
        assertNull(viewModel.getSpecificRestaurant("Le Calife"));
    }

    @After
    public void tearDown() throws Exception {
        userRepository = null;
        activity = null;
        viewModel = null;
    }
}
