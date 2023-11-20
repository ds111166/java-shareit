# java-shareit
Template repository for Shareit project.

## **✓** Добавляем запрос вещи
Нужно добавить четыре новых эндпоинта:
* **✓✓✓** `POST /requests` — добавить новый запрос вещи. Основная часть запроса — текст запроса, где пользователь описывает, какая именно вещь ему нужна.
* **✓✓✓** `GET /requests` — получить список своих запросов вместе с данными об ответах на них. Для каждого запроса должны указываться описание, дата и время создания и список ответов в формате: id вещи, название, её описание description, а также requestId запроса и признак доступности вещи available. Так в дальнейшем, используя указанные id вещей, можно будет получить подробную информацию о каждой вещи. Запросы должны возвращаться в отсортированном порядке от более новых к более старым.
* **✓✓✓** `GET /requests/all?from={from}&size={size}` — получить список запросов, созданных другими пользователями. С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить. Запросы сортируются по дате создания: от более новых к более старым. Результаты должны возвращаться постранично. Для этого нужно передать два параметра: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
* **✓✓✓** `GET /requests/{requestId}` — получить данные об одном конкретном запросе вместе с данными об ответах на него в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.
## **✓**Добавляем опцию ответа на запрос
* **✓✓✓** Добавьте поле `requestId` в тело запроса `POST /items`. Обратите внимание, что должна сохраниться возможность добавить вещь и без указания `requestId`.
## **✓**Добавляем пагинацию к существующим эндпоинтам
Что бы приложение было комфортным для пользователей, а также быстро работало, добавить пагинацию в эндпоинты
Параметры будут такими же, как и для эндпоинта на получение запросов вещей: номер первой записи и желаемое количество элементов для отображения:
* **✓✓✓** `GET /items` -> `GET /items?from={from}&size={size}`
* **✓✓✓** `GET /items/search` -> `GET /items/search?from={from}&size={size}`
* **✓✓✓** `GET /bookings` -> `GET /bookings?from={from}&size={size}`
* **✓✓✓** `GET /bookings/owner` -> `GET /bookings/owner?from={from}&size={size}`
## Добавить тесты
* BookingControllerTest
* ItemControllerTest
* UserControllerTest
* ItemRequestControllerTest
```java
/*@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Test
    void getUsers() throws Exception {
        List<UserResponseDto> users = userService.getUsers();
        when(userService.getUsers())
                .thenReturn(users);
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    void getUserById() throws Exception {
        final UserResponseDto user = shouldCreateUser(UserRequestDto.builder()
                .name("name1").email("email1@mail.com").build());
        final Long userId = user.getId();
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
        mockMvc.perform(get("/users/{id}", 99))
                .andExpect(status().isNotFound());
    }
    @Test
    void createUserTest() throws Exception {
        shouldCreateUser(UserRequestDto.builder().name("name1").email("email1@mail.com").build());
        shouldNotCreateUserWithNullName();
        shouldNotCreateUserWithWithAnIncorrectEmail();
        shouldNotCreateUserWithNonUniqueEmail();
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    private void shouldNotCreateUserWithNonUniqueEmail() throws Exception {
        UserRequestDto newUser = UserRequestDto.builder().name("1name3").email("email1@mail.com").build();
        final ResultActions resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isConflict());
    }

    private void shouldNotCreateUserWithNullName() throws Exception {
        UserRequestDto newUser = UserRequestDto.builder().email("email2@mail.com").build();
        final ResultActions resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    private void shouldNotCreateUserWithWithAnIncorrectEmail() throws Exception {
        UserRequestDto newUser = UserRequestDto.builder().name("name2").email("email1.mail.com").build();
        final ResultActions resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());

    }

    private UserResponseDto shouldCreateUser(UserRequestDto newUser) throws Exception {

        final ResultActions resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.email").value("email1@mail.com"));
        final MvcResult mvcResult = resultActions.andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        final UserResponseDto createdUser = objectMapper.readValue(response, UserResponseDto.class);
        resultActions.andExpect(jsonPath("$.id").value(createdUser.getId()));
        return createdUser;
    }
}*/
```
