package ${code.packageName}.web;

import com.ucsmy.core.controller.BasicController;
import ${code.packageName}.entity.${code.entityName};
import ${code.packageName}.service.${code.entityName}Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${code.uri}")
public class ${code.entityName}Controller extends BasicController<${code.entityName}, ${code.entityName}Service> {

}
