package ${code.packageName}.service.impl;

import ${code.packageName}.dao.${code.entityName}Dao;
import ${code.packageName}.entity.${code.entityName};
import ${code.packageName}.service.${code.entityName}Service;
import org.springframework.stereotype.Service;

@Service
public class ${code.entityName}ServiceImpl extends BasicServiceImpl<${code.entityName}, ${code.entityName}Dao> implements ${code.entityName}Service {

}