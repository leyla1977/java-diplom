package ru.netology.filestorage.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileStorageIntegrationTest {

    private static PostgreSQLContainer<?> postgres;

    @BeforeAll
    static void setUp() {
        // Проверяем доступность Docker
        try {
            assumeTrue(DockerClientFactory.instance().isDockerAvailable(),
                    "Docker is not available, skipping integration tests");
        } catch (Throwable t) {
            // Если даже проверка доступности падает (например, нет библиотеки), тоже пропускаем
            assumeTrue(false, "Docker environment check failed, skipping integration tests");
            return;
        }

        // Запускаем PostgreSQL контейнер
        postgres = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
                .waitingFor(Wait.forListeningPort());
        postgres.start();

        // Переопределяем настройки DataSource для использования контейнера
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }

    @AfterAll
    static void tearDown() {
        if (postgres != null && postgres.isRunning()) {
            postgres.stop();
        }
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testLogin() {
        // Проверяем, что контейнер действительно запущен (если нет — тест будет пропущен ранее)
        assertNotNull(postgres, "PostgreSQL container should be started");

        // Выполняем тестовый запрос к /login
        LoginRequest request = new LoginRequest();
        request.setLogin("user");
        request.setPassword("password");

        ResponseEntity<String> response = restTemplate.postForEntity("/login", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Дополнительно  проверить наличие токена в ответе
        assertTrue(response.getBody().contains("authToken") || response.getBody().contains("auth-token"));
    }

    // Вспомогательный класс для запроса
    static class LoginRequest {
        private String login;
        private String password;
        // getters/setters
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}