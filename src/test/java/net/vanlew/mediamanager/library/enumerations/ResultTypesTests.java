package net.vanlew.mediamanager.library.enumerations;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ResultTypesTests {

    @Test
    public void resultTypes_ShouldReturnExpectedDescriptions() {
        for (ResultTypes type : ResultTypes.values()) {
            var description = type.toString();
            ResultTypes actual = ResultTypes.valueOf(description.toUpperCase());
            assertThat(actual).isEqualTo(type);
            AssertionsForClassTypes.assertThat(description).isEqualTo(actual.toString());
        }
    }
}
