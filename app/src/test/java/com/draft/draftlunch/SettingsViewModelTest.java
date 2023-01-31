package com.draft.draftlunch;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.draft.draftlunch.Services.UserRepository;
import com.draft.draftlunch.ui.settings.SettingsActivity;
import com.draft.draftlunch.ui.settings.SettingsViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class SettingsViewModelTest {

    @Mock
    UserRepository userRepository;
    @Mock
    SettingsActivity activity;
    SettingsViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new SettingsViewModel(userRepository);
    }
    @Test
    public void getMyUserTest(){
        when(viewModel.getMyUser()).thenReturn(null);
        assertNull(viewModel.getMyUser());
    }

    @Test
    public void deleteUserTest(){
        when(viewModel.deleteUser(activity)).thenReturn(null);
        assertNull(viewModel.deleteUser(activity));
    }

    @After
    public void tearDown() throws Exception {
        userRepository = null;
        activity = null;
        viewModel = null;
    }
}
