package org.re.car.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.re.car.CarFixture;
import org.re.car.api.payload.CarCreationRequestFixture;
import org.re.car.api.payload.CarUpdateRequestFixture;
import org.re.car.domain.CarIgnitionStatus;
import org.re.car.service.CarService;
import org.re.employee.domain.EmployeeRole;
import org.re.security.access.ManagerOnlyHandler;
import org.re.tenant.TenantId;
import org.re.test.security.MockCompanyUserDetails;
import org.re.web.method.support.TenantIdArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({AopAutoConfiguration.class, ManagerOnlyHandler.class})
@WebMvcTest(CarApi.class)
@AutoConfigureMockMvc
class CarApiTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CarService carService;

    @Nested
    @DisplayName("조회 테스트")
    class ReadTest {
        @Test
        @DisplayName("차량 목록 조회")
        @MockCompanyUserDetails
        void car_list_test() throws Exception {
            // given
            var tenantId = new TenantId(111L);
            var nCars = 10;
            var cars = CarFixture.createList(nCars);
            var page = new PageImpl<>(cars);
            Mockito.when(carService.getCars(Mockito.any(), Mockito.any()))
                .thenReturn(page);

            // when
            var req = get("/api/cars")
                .param("tenantId", "111")
                .param("page", "0")
                .param("size", "10")
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId);

            // then
            mvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(nCars))
                .andExpect(jsonPath("$.data[*].id").exists());
        }

        @Test
        @DisplayName("차량 상세 조회")
        @MockCompanyUserDetails
        void car_detail_test() throws Exception {
            // given
            var tenantId = new TenantId(111L);
            var carId = 123L;
            var car = Mockito.spy(CarFixture.create());
            Mockito.when(car.getId()).thenReturn(carId);
            Mockito.when(carService.getCarById(Mockito.any(), Mockito.anyLong()))
                .thenReturn(car);

            // when
            var req = get("/api/cars/{carId}", car.getId())
                .param("tenantId", String.valueOf(tenantId))
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId);

            // then
            mvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(car.getId()))
                .andExpect(jsonPath("$.data.companyId").value(car.getCompany().getId()))
                .andExpect(jsonPath("$.data.name").value(car.getProfile().getName()))
                .andExpect(jsonPath("$.data.licenseNumber").value(car.getProfile().getLicenseNumber()))
                .andExpect(jsonPath("$.data.imageUrl").value(car.getProfile().getImageUrl()))
                .andExpect(jsonPath("$.data.latitude").value(car.getMdtStatus().getLocation().getLatitude()))
                .andExpect(jsonPath("$.data.longitude").value(car.getMdtStatus().getLocation().getLongitude()))
                .andExpect(jsonPath("$.data.mileageInitial").value(car.getMdtStatus().getMileageInitial()))
                .andExpect(jsonPath("$.data.mileageCurrent").value(car.getMdtStatus().getMileageSum()))
                .andExpect(jsonPath("$.data.batteryVoltage").value(car.getMdtStatus().getBatteryVoltage()))
                .andExpect(jsonPath("$.data.ignitionStatus").value(car.getMdtStatus().getIgnition().toString()));
        }

        @Test
        @DisplayName("시동 상태별 차량 수 조회")
        @MockCompanyUserDetails
        void count_by_ignition_status_test() throws Exception {
            // given
            var tenantId = new TenantId(111L);
            var nOnCars = 5L;
            Mockito.when(carService.countByIgnitionStatus(Mockito.any(), Mockito.eq(CarIgnitionStatus.ON)))
                .thenReturn(nOnCars);
            var nOffCars = 3L;
            Mockito.when(carService.countByIgnitionStatus(Mockito.any(), Mockito.eq(CarIgnitionStatus.OFF)))
                .thenReturn(nOffCars);

            // 시동 On 차량 수 조회
            var reqOn = get("/api/cars/count/ignition")
                .param("status", CarIgnitionStatus.ON.toString())
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId);
            mvc.perform(reqOn)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(nOnCars));

            // 시동 Off 차량 수 조회
            var reqOff = get("/api/cars/count/ignition")
                .param("status", CarIgnitionStatus.OFF.toString())
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId);
            mvc.perform(reqOff)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(nOffCars));
        }
    }

    @Nested
    @DisplayName("검색 테스트")
    class SearchTest {
        @Test
        @DisplayName("시동 상태별 목록 조회 테스트")
        @MockCompanyUserDetails
        void search_by_ignition_status_test() throws Exception {
            // given
            var tenantId = new TenantId(111L);

            var nOnCars = 5;
            var onCars = CarFixture.createList(nOnCars);
            Mockito.when(carService.searchByIgnitionStatus(Mockito.any(), Mockito.eq(CarIgnitionStatus.ON), Mockito.any()))
                .thenReturn(new PageImpl<>(onCars));

            var nOffCars = 3;
            var offCars = CarFixture.createList(nOffCars);
            Mockito.when(carService.searchByIgnitionStatus(Mockito.any(), Mockito.eq(CarIgnitionStatus.OFF), Mockito.any()))
                .thenReturn(new PageImpl<>(offCars));

            // 시동 On 차량
            var reqOn = get("/api/cars/search/ignition")
                .param("status", CarIgnitionStatus.ON.toString())
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId);
            mvc.perform(reqOn)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(nOnCars));

            // 시동 Off 차량
            var reqOff = get("/api/cars/search/ignition")
                .param("status", CarIgnitionStatus.OFF.toString())
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId);
            mvc.perform(reqOff)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(nOffCars));
        }
    }

    @Nested
    @DisplayName("수정 테스트")
    class UpdateTest {
        @Test
        @DisplayName("일반 사용자는 차량 정보를 수정할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.NORMAL)
        void testCarUpdate() throws Exception {
            var tenantId = new TenantId(111L);
            var carId = 1L;

            // when
            var body = objectMapper.writeValueAsString(
                CarUpdateRequestFixture.create()
            );
            var req = patch("/api/cars/{carId}", carId)
                .contentType("application/json")
                .content(body)
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId)
                .with(csrf());

            mvc.perform(req)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("관리자 권한을 가진 사용자는 차량 정보를 수정할 수 있다.")
        @MockCompanyUserDetails(role = EmployeeRole.ROOT)
        void testManagerCanUpdateCar() throws Exception {
            var tenantId = new TenantId(111L);
            var carId = 1L;
            var car = Mockito.spy(CarFixture.create());
            Mockito.when(car.getId()).thenReturn(carId);
            Mockito.when(carService.updateCarProfile(Mockito.any(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(car);

            // when
            var body = objectMapper.writeValueAsString(
                CarUpdateRequestFixture.create()
            );
            var req = patch("/api/cars/{carId}", carId)
                .contentType("application/json")
                .content(body)
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId)
                .with(csrf());

            mvc.perform(req)
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {
        @Test
        @DisplayName("일반 사용자는 차량을 등록할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.NORMAL)
        void testCarCreation() throws Exception {
            // given
            var tenantId = new TenantId(111L);
            var request = CarCreationRequestFixture.create();

            // when
            var body = objectMapper.writeValueAsString(request);
            var req = post("/api/cars")
                .contentType("application/json")
                .content(body)
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId)
                .with(csrf());

            // then
            mvc.perform(req)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("관리자 권한을 가진 사용자는 차량을 등록할 수 있다.")
        @MockCompanyUserDetails(role = EmployeeRole.ROOT)
        void testManagerCanCreateCar() throws Exception {
            // given
            var tenantId = new TenantId(111L);
            var request = CarCreationRequestFixture.create();
            var car = Mockito.spy(CarFixture.create());
            Mockito.when(car.getId()).thenReturn(1L);
            Mockito.when(carService.createCar(Mockito.any(), Mockito.any()))
                .thenReturn(car);

            // when
            var body = objectMapper.writeValueAsString(request);
            var req = post("/api/cars")
                .contentType("application/json")
                .content(body)
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId)
                .with(csrf());

            // then
            mvc.perform(req)
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("삭제 테스트")
    class DeletionTest {

        @Test
        @DisplayName("일반 사용자는 차량을 삭제할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.NORMAL)
        void testCarService() throws Exception {
            // given
            var tenantId = new TenantId(111L);
            var carId = 1L;

            // when
            var req = delete("/api/cars/{carId}", carId)
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId)
                .with(csrf());

            // then
            mvc.perform(req)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("관리자 권한을 가진 사용자는 차량을 삭제할 수 있다.")
        @MockCompanyUserDetails(role = EmployeeRole.ROOT)
        void testManagerCanDeleteCar() throws Exception {
            // given
            var tenantId = new TenantId(111L);
            var carId = 1L;

            // when
            var req = delete("/api/cars/{carId}", carId)
                .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId)
                .with(csrf());

            // then
            mvc.perform(req)
                .andExpect(status().isOk());
        }
    }
}
