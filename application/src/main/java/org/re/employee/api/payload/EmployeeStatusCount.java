package org.re.employee.api.payload;

public record EmployeeStatusCount(
    int totalEmployee,
    int activeEmployee,
    int inactiveEmployee,
    int adminEmployee,
    int normalEmployee
) {
    public static EmployeeStatusCount of(int activeEmployee, int inactiveEmployee, int adminEmployee) {
        return new EmployeeStatusCount(activeEmployee + inactiveEmployee, activeEmployee, inactiveEmployee,
            adminEmployee, (activeEmployee + inactiveEmployee) - adminEmployee);
    }
}
