package net.vanlew.mediamanager.api.domain.common;

public class Ensure {

    /**
     * Checks whether an object named {@systemProperty variableName} with a value of {@systemProperty variableValue } is null
     *
     * @param variableValue The value of the variable
     * @param variableName  The name of the variable
     */
    public static void notNull(Object variableValue, String variableName) {
        if (variableValue == null || (variableValue instanceof String str && str.isBlank())) {
            throw new IllegalArgumentException("The variable/parameter '%s' cannot be null, empty, or blank.".formatted(variableName));
        }
    }

    /**
     * Checks whether an object named {@systemProperty variableName} with a value of {@systemProperty variableValue } is null
     *
     * @param enumValue      The value of the enum
     * @param valueToCompare The enum to compare {@systemProperty enumValue} to
     */
    public static <E extends Enum<E>> void areNotEqual(E enumValue, E valueToCompare) {
        if (enumValue.compareTo(valueToCompare) == 0) {
            throw new IllegalArgumentException(String.format("The enum value '%s' cannot be equal to '%s'.".formatted(enumValue, valueToCompare)));
        }
    }
}