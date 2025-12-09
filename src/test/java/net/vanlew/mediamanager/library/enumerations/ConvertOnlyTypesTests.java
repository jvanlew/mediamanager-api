package net.vanlew.mediamanager.library.enumerations;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ConvertOnlyTypesTests {

    @Test
    public void convertOnlyTypes_ShouldReturnExpectedDescriptions() {
        for (ConvertOnlyTypes type : ConvertOnlyTypes.values()) {
            var description = type.toString();
            ConvertOnlyTypes actual = ConvertOnlyTypes.valueOf(description.toUpperCase());
            assertThat(actual).isEqualTo(type);
            assertThat(description).isEqualTo(actual.toString());
        }
    }
}
