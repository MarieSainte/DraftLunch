package com.draft.draftlunch;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.draft.draftlunch.Services.UserRepository;
import com.draft.draftlunch.ui.main.MainActivity;
import com.draft.draftlunch.ui.main.MainViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class MainViewModelTest {

    @Mock
    UserRepository userRepository;
    @Mock
    MainActivity activity;
    MainViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new MainViewModel(userRepository);
    }
    @Test
    public void getUserDataTest(){
        when(viewModel.getUserData()).thenReturn(null);
        assertNull(viewModel.getUserData());
    }

    @After
    public void tearDown() throws Exception {
        userRepository = null;
        activity = null;
        viewModel = null;
    }
}
