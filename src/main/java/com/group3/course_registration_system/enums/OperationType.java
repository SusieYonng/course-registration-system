package com.group3.course_registration_system.enums;

/**
 * Enum representing the types of operations that can be performed when updating a course.
 */
public enum OperationType {
    /**
     * Add a student to the course.
     */
    ADD_STUDENT(1),

    /**
     * Remove a student from the course.
     */
    REMOVE_STUDENT(2),

    /**
     * No operation on the course's student list.
     */
    NO_OPERATION(0);

    private final int value;

    OperationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Get the OperationType from an integer value.
     * 
     * @param value The integer value of the operation.
     * @return The corresponding OperationType.
     */
    public static OperationType fromValue(int value) {
        for (OperationType type : OperationType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid operation type value: " + value);
    }
}
