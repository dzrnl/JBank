package ru.dzrnl.bank.business.test;

import org.junit.jupiter.api.Test;
import ru.dzrnl.bank.business.contracts.FriendshipService;
import ru.dzrnl.bank.business.repositories.FriendshipRepository;
import ru.dzrnl.bank.business.services.FriendshipServiceImpl;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FriendshipServiceTest {
    private final String defaultUserLogin = "ivanov";
    private final String secondDefaultUserLogin = "petrov";
    private final String thirdDefaultUserLogin = "sidorov";

    @Test
    public void shouldAddFriend() {
        FriendshipRepository mockRepo = mock(FriendshipRepository.class);
        doNothing().when(mockRepo).addFriend(defaultUserLogin, secondDefaultUserLogin);

        FriendshipService friendshipService = new FriendshipServiceImpl(mockRepo);

        friendshipService.addFriend(defaultUserLogin, secondDefaultUserLogin);

        verify(mockRepo, times(1)).addFriend(defaultUserLogin, secondDefaultUserLogin);
    }

    @Test
    public void shouldRemoveFriend() {
        FriendshipRepository mockRepo = mock(FriendshipRepository.class);
        doNothing().when(mockRepo).removeFriend(defaultUserLogin, secondDefaultUserLogin);

        FriendshipService friendshipService = new FriendshipServiceImpl(mockRepo);

        friendshipService.removeFriend(defaultUserLogin, secondDefaultUserLogin);

        verify(mockRepo, times(1)).removeFriend(defaultUserLogin, secondDefaultUserLogin);
    }

    @Test
    public void areUsersFriends() {
        FriendshipRepository mockRepo = mock(FriendshipRepository.class);

        FriendshipService friendshipService = new FriendshipServiceImpl(mockRepo);

        when(mockRepo.areFriends(defaultUserLogin, secondDefaultUserLogin)).thenReturn(true);

        assertTrue(friendshipService.areFriends(defaultUserLogin, secondDefaultUserLogin));

        when(mockRepo.areFriends(defaultUserLogin, secondDefaultUserLogin)).thenReturn(false);

        assertFalse(friendshipService.areFriends(defaultUserLogin, secondDefaultUserLogin));
    }

    @Test
    public void shouldGetAllFriends() {
        var friendLogins = Set.of(secondDefaultUserLogin, thirdDefaultUserLogin);

        FriendshipRepository mockRepo = mock(FriendshipRepository.class);
        when(mockRepo.findFriendLogins(defaultUserLogin)).thenReturn(friendLogins);

        FriendshipService friendshipService = new FriendshipServiceImpl(mockRepo);

        assertEquals(friendshipService.getFriendLogins(defaultUserLogin), friendLogins);
    }
}
