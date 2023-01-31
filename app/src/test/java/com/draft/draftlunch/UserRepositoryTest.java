package com.draft.draftlunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.draft.draftlunch.Services.UserRepository;

import org.junit.Test;
import org.mockito.Mock;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserRepositoryTest {

    // mock creation
    @Mock
    UserRepository mockedUserRepository = mock(UserRepository.class);

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    UserRepository userRepository = UserRepository.getInstance();


}