package com.draft.draftlunch;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.draft.draftlunch.Services.ChatRepository;
import com.draft.draftlunch.Services.UserRepository;
import com.draft.draftlunch.ui.message.ChatViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class ChatViewModelTest {

    @Mock
    UserRepository userRepository;
    @Mock
    ChatRepository chatRepository;
    ChatViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new ChatViewModel(userRepository, chatRepository);
    }
    @Test
    public void getCurrentUserUIDTest(){
        when(viewModel.getCurrentUserUID()).thenReturn(null);
        assertNull(viewModel.getCurrentUserUID());
    }
    @Test
    public void getChatMessagesTest() {
        when(viewModel.getChatMessages()).thenReturn(null);
        assertNull(viewModel.getChatMessages());
    }

    @After
    public void tearDown() throws Exception {
        userRepository = null;
        chatRepository = null;
        viewModel = null;
    }
}
