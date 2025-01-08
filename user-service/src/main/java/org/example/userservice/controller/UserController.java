package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDto;
import org.example.userservice.jpa.UserEntity;
import org.example.userservice.service.UserService;
import org.example.userservice.vo.RequestUser;
import org.example.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor // final로 선언된 필드에 대해 생성자를 만들어줌
public class UserController {

    private final Environment env;
    private final UserService userService;

    @GetMapping("/health_check")
    public String healthCheck() {
        return "UserService I am OK";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return env.getProperty("greeting.message");
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody RequestUser user) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(user, UserDto.class); // userId, createdAt 채워주기
        userService.createUser(userDto);

        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class); // 응답용 DTO

        return new ResponseEntity(responseUser, HttpStatus.CREATED); // 201 Created
    }
}