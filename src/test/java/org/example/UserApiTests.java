package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserApiTests {

    private static final String BASE_URL = "https://serverest.dev";
    private static String createdUserId;

    private static String jwtToken;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    private static String getJwtToken() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", "fulano@qa.com");
        loginData.put("password", "senha123");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("authorization", notNullValue())
                .extract().response();

        if (response.getStatusCode() != 200) {
            System.err.println("Erro ao obter token JWT. Status Code: " + response.getStatusCode());
            System.err.println("Corpo da Resposta: " + response.getBody().asString());
        }

        return response.jsonPath().getString("authorization");
    }
    @Test
    @Order(1)
    void testPostUser() {
        Random rn = new Random();
        jwtToken = getJwtToken();
        Map<String, String> user = new HashMap<>();
        user.put("nome", "Usuário de Teste");
        user.put("email", rn.nextInt()+"@example.com");
        user.put("password", "senha123");
        user.put("administrador", "true");

        Response response = given()
                .header("Authorization", jwtToken)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .body("_id", notNullValue())
                .extract().response();

        createdUserId = response.jsonPath().getString("_id");
        assertNotNull(createdUserId, "O ID do usuário não deve ser nulo");
    }

    @Test
    @Order(2)
    void testGetUsers() {
        testPostUser();
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThanOrEqualTo(1)); // Espera pelo menos o usuário criado
    }

    @Test
    @Order(3)
    void testGetUserById() {
        testPostUser();
        given()
                .pathParam("_id", createdUserId)
                .when()
                .get("/usuarios/{_id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("_id", equalTo(createdUserId));
    }

    @Test
    @Order(4)
    void testPutUser() {
        testPostUser();
        Map<String, String> updatedUser = new HashMap<>();
        updatedUser.put("nome", "Usuário Atualizado");
        updatedUser.put("email", "atualizado" + System.currentTimeMillis() + "@example.com"); // Email único
        updatedUser.put("password", "novaSenha");
        updatedUser.put("administrador", "false");

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .pathParam("_id", createdUserId)
                .body(updatedUser)
                .when()
                .put("/usuarios/{_id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Registro alterado com sucesso"));

    }

    @Test
    @Order(5)
    void testDeleteUser() {
        testPostUser();
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .pathParam("_id", createdUserId)
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @Test
    @Order(6)
    void testGetUserByIdNotFound() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .pathParam("_id", "ID_INEXISTENTE")
                .when()
                .get("/usuarios/{_id}")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", containsString("Usuário não encontrado"));
    }

}