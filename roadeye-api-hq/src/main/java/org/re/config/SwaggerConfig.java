package org.re.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.re.tenant.TenantId;
import org.re.web.filter.TenantIdContextFilter;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
public class SwaggerConfig {
    static {
        SpringDocUtils.getConfig()
            .addRequestWrapperToIgnore(TenantId.class)
            .addRequestWrapperToIgnore(UserDetails.class);
    }

    @Bean
    public OpenAPI roadeyeOpenAPI() {
        var info = new Info();
        info.setTitle("Roadeye HQ API 문서");
        info.setDescription("Roadeye HQ API 문서입니다.");
        info.setVersion("0.0.1");

        var openAPI = new OpenAPI();
        openAPI.setInfo(info);

        Paths paths = new Paths();
        paths.addPathItem("/api/auth/sign-in", loginPathItem());

        openAPI.setPaths(paths);

        return openAPI;
    }

    private PathItem loginPathItem() {
        var ops = new Operation()
            .addTagsItem("업체 사용자 로그인")
            .summary("로그인")
            .description("업체 사용자 로그인")
            .addParametersItem(new Parameter()
                .name(TenantIdContextFilter.TENANT_ID_HEADER_NAME)
                .in("header")
                .description("업체 코드")
                .required(true)
                .schema(new Schema<>().type("integer"))
            )
            .requestBody(new RequestBody()
                .description("로그인 폼 데이터")
                .required(true)
                .content(new Content().addMediaType("application/json",
                    new MediaType().schema(
                        new Schema<>()
                            .addProperty("username", new Schema<>().type("string"))
                            .addProperty("password", new Schema<>().type("string"))
                    )
                ))
            )
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("로그인 성공"))
                .addApiResponse("401", new ApiResponse().description("인증 실패"))
            );
        return new PathItem().post(ops);
    }
}
