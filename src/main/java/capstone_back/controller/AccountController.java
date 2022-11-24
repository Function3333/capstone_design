package capstone_back.controller;

import capstone_back.service.AccountService;
import capstone_back.utils.dto.LoginDto;
import capstone_back.utils.dto.RegisterDto;
import capstone_back.jwt.JwtService;
import capstone_back.service.LoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AccountController {
    private final AccountService accountService;
    private final LoginService loginService;
    private final JwtService jwtService;

    /*회원가입*/
    @PostMapping("/user/register")
    public ResponseData register(@RequestBody RegisterDto registerDto) throws JsonProcessingException {
        boolean isEmailDuplicate = accountService.findEmailIsDuplicate(registerDto.getEmail());
        boolean isUsernameDuplicate = accountService.findUsernameIsDuplicate(registerDto.getUsername());

        if(isEmailDuplicate) {
            return new ResponseData("fail", "duplicate email");
        }

        if(isUsernameDuplicate) {
            return new ResponseData("fail", "duplicate username");
        }

        Long id = accountService.join(registerDto);

        return new ResponseData("success", id);
    }

    @PostMapping("/user/login")
    public ResponseData login(@RequestBody LoginDto loginDto) {
        if(loginService.login(loginDto)) {
            return new ResponseData("success", jwtService.createAccessToken(loginDto.getEmail()));
        }

        return new ResponseData("fail", "invalid email or password");
    }

    //이미 만료되어 헤더에 토큰이 없는 경우 fail
    @GetMapping("/user/refresh")
    public ResponseData refreshToken(HttpServletRequest request) {
        String email;
        try {
            email = jwtService.getEmail(request);
        } catch (IllegalArgumentException e) {
            return new ResponseData("fail", "no token in header");
        }

        String refreshToken = jwtService.refreshToken(email);
        return new ResponseData("success", refreshToken);
    }

    @Data
    public class ResponseData {
        String result;
        Object data;

        public ResponseData(String result, Object data) {
            this.result = result;
            this.data = data;
        }
    }


}
