package me.nvliu.management.web.api;

import me.nvliu.management.utils.Tools;
import me.nvliu.management.web.entity.Menu;
import me.nvliu.management.web.entity.MenuTree;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.User;
import me.nvliu.management.web.security.JwtAuthenticationResponse;
import me.nvliu.management.web.service.AuthService;
import me.nvliu.management.web.service.MenuService;
import me.nvliu.management.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody User authenticationRequest) throws AuthenticationException{
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        if(Tools.notEmpty(token)){
            // 开始获取用户信息，角色以及菜单
            User user = userService.getUserByName(authenticationRequest.getUsername());
            if(Tools.notEmpty(user)){
                List<Menu> permissions = menuService.getMenuByUserId(user.getId());
                MenuTree menuTree = new MenuTree();
                return ResponseEntity.ok(new JwtAuthenticationResponse(token,String.valueOf(user.getId()), menuTree.menuList(permissions),authenticationRequest.getUsername()));
            }

        }
        // Return the token
        return ResponseEntity.badRequest().body(null);
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }
}