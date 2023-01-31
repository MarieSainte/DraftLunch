package com.draft.draftlunch;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.draft.draftlunch.Services.UserRepository;
import com.draft.draftlunch.ui.workmates.WorkmatesFragment;
import com.draft.draftlunch.ui.workmates.WorkmatesViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class WorkmatesViewModelTest {

    @Mock
    UserRepository userRepository;
    @Mock
    WorkmatesFragment activity;
    WorkmatesViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new WorkmatesViewModel(userRepository);
    }
    @Test
    public void getUsersTest(){
        when(viewModel.getUsers()).thenReturn(null);
        assertNull(viewModel.getUsers());
    }

    @After
    public void tearDown() throws Exception {
        userRepository = null;
        activity = null;
        viewModel = null;
    }
}
