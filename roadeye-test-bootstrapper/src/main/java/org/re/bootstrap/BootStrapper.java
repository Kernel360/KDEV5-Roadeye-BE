package org.re.bootstrap;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.car.domain.Car;
import org.re.car.dto.CarCreationCommandFixture;
import org.re.car.service.CarDomainService;
import org.re.company.CompanyQuoteFixture;
import org.re.company.CompanyQuoteInfoFixture;
import org.re.company.domain.Company;
import org.re.company.service.CompanyDomainService;
import org.re.employee.EmployeeFixture;
import org.re.employee.domain.Employee;
import org.re.employee.service.EmployeeDomainService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootStrapper {
    private final static String BOOSTRAP_TABLE_NAME = "__roadeye_boostrap__";

    private final JdbcTemplate jdbcTemplate;

    private final CompanyDomainService companyDomainService;
    private final EmployeeDomainService employeeDomainService;
    private final CarDomainService carDomainService;

    private final BootstrapperProperty bootstrapperProperty;

    @PostConstruct
    public void init() {
        log.info("BootStrapper initialized with properties: members={}, cars={}",
            bootstrapperProperty.getMembers(), bootstrapperProperty.getCars());
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void bootstrap() {
        if (alreadyBootstrapped()) {
            log.info("Bootstrapping is already done. Skipping...");
            return;
        }

        log.info("Bootstrapping starts...");

        try {
            createMarkerTable();
            doBootstrap();
            log.info("Bootstrapping finished successfully.");
        } catch (Exception e) {
            dropMarkerTable();
            log.error("Bootstrapping failed", e);
            throw e; // rethrow the exception to ensure the application fails to start
        } finally {
            log.info("Bootstrapping completed.");
        }
    }

    private void doBootstrap() {
        var company = createCompany();
        log.info("Created company: {}", company);

        var nMembers = bootstrapperProperty.getMembers();
        var members = createNormalMembers(company, nMembers);
        log.info("Created {} normal members", nMembers);
        for (int i = 0; i < nMembers; i++) {
            var m = members.get(i);
            var username = m.getCredentials().loginId();
            var password = m.getCredentials().password();
            log.info("Member-{}: username: {}, password; {}", i, username, password);
        }

        var nCars = bootstrapperProperty.getCars();
        var cars = createCars(company, nCars);
        log.info("Created {} cars", nCars);
        for (int i = 0; i < nCars; i++) {
            var car = cars.get(i);
            var carId = car.getId();
            var carName = car.getProfile().getName();
            log.info("Car-{}: id: {}, name: {}", i, carId, carName);
        }
    }

    private void createMarkerTable() {
        jdbcTemplate.execute("CREATE TABLE if NOT EXISTS " + BOOSTRAP_TABLE_NAME + " (id BIGINT AUTO_INCREMENT PRIMARY KEY)");
        jdbcTemplate.update("INSERT INTO " + BOOSTRAP_TABLE_NAME + " (id) VALUES (1)");
    }

    private void dropMarkerTable() {
        jdbcTemplate.execute("DROP TABLE if EXISTS " + BOOSTRAP_TABLE_NAME);
    }

    private boolean alreadyBootstrapped() {
        try {
            String sql = "SELECT COUNT(*) FROM " + BOOSTRAP_TABLE_NAME;
            return jdbcTemplate.queryForObject(sql, Long.class) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private Company createCompany() {
        var bisNo = UUID.randomUUID().toString();
        var quoteInfo = CompanyQuoteInfoFixture.createWithBisNo(bisNo);
        var quote = CompanyQuoteFixture.create(quoteInfo);
        return companyDomainService.createCompany(quote);
    }

    private List<Employee> createNormalMembers(Company company, int nMembers) {
        var ret = new ArrayList<Employee>(nMembers);
        for (int i = 0; i < nMembers; i++) {
            var loginId = "user" + i;
            var password = "pass" + i;
            var member = EmployeeFixture.createNormal(company, loginId, password);
            employeeDomainService.create(member);
            ret.add(member);
        }
        return ret;
    }

    private List<Car> createCars(Company company, int nCars) {
        var ret = new ArrayList<Car>(nCars);
        for (int i = 0; i < nCars; i++) {
            var carName = "Car" + i;
            var command = CarCreationCommandFixture.create(carName);
            var car = carDomainService.createCar(company, command);
            ret.add(car);
        }
        return ret;
    }
}
