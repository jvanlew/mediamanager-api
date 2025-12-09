package net.vanlew.mediamanager.library.common;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.aggregator.ArgumentAccessException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ExceptionHelperTests {

    @Test
    void getFlattenedMessageFriendly_ShouldReturnExpectedMessage() {
        var exceptionMessage = "Test Exception";
        var msg = ExceptionHelper.getFlattenedMessageFriendly(new ArgumentAccessException("Test Exception"));
        assertThat(msg).isNotNull();
        assertThat(msg).isEqualTo("Test Exception");
    }
}
