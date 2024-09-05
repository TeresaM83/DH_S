package dreamhouse.app.ncontroller;

import dreamhouse.app.entity.Rol;
import dreamhouse.app.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/crud")
public class RolController {

    @Autowired
    RolService rolService;

    @ResponseBody
    @GetMapping("roles")
    public List<Rol> getRoles(){
        return rolService.listarTodo();
    }
}
