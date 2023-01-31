package com.draft.draftlunch;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.draft.draftlunch.Services.UserRepository;
import com.draft.draftlunch.ui.list.ListViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
@RunWith(JUnit4.class)
public class ListViewModelTest {

    @Mock
    UserRepository userRepository;
    ListViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new ListViewModel(userRepository);
    }
    @Test
    public void getLocationTest(){
        when(viewModel.getLocation()).thenReturn(null);
        assertNull(viewModel.getLocation());
    }
    @Test
    public void getChatMessagesTest() {
        when(viewModel.getRestaurants()).thenReturn(null);
        assertNull(viewModel.getRestaurants());
    }

    @After
    public void tearDown() throws Exception {
        userRepository = null;
        viewModel = null;
    }
}
