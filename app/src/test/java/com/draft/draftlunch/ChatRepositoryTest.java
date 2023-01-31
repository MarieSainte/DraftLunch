package com.draft.draftlunch;

import static org.mockito.Mockito.mock;

import com.draft.draftlunch.Services.ChatRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Date;

public class ChatRepositoryTest {

    ChatRepository mockedChatRepository;

    @Before
    public void testinit(){
        MockitoAnnotations.initMocks(this);
        mockedChatRepository = mock(ChatRepository.class);
    }

    @Test
    public void getReadableTimeTest(){
        Date date = new Date();

        mockedChatRepository.getReadableTime(date);
    }

    @After
    public void tearDown() throws Exception {
        mockedChatRepository = null;
    }
}
