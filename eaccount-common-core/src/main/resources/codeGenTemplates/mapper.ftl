<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${code.packageName}.dao.${code.entityName}Dao">

    <sql id="query">
        SELECT
        <#list code.attrs as attr>
            a.${attr.jdbcName} as '${attr.javaName}'<#if attr_has_next>,</#if>
        </#list>
        FROM ${code.dataTable} a
    </sql>

    <insert id="save" parameterType="${code.packageName}.entity.${code.entityName}">
        insert into ${code.dataTable}(
        <#list insert as attr>
            ${attr.jdbcName}<#if attr_has_next>,</#if>
        </#list>
        )
        values(
        <#list insert as attr>
            ${_jj}{${attr.javaName}}<#if attr_has_next>,</#if>
        </#list>
        )
    </insert>

    <update id="update" parameterType="${code.packageName}.entity.${code.entityName}">
        update ${code.dataTable}
        set
        <#list update as attr>
            ${attr.jdbcName} = ${_jj}{${attr.javaName}}<#if attr_has_next>,</#if>
        </#list>
        where id = ${_jj}{id}
    </update>

    <delete id="delete" parameterType="String">
        delete from ${code.dataTable}
        where id = ${_jj}{id}
    </delete>

    <select id="findById" parameterType="string" resultType="${code.packageName}.entity.${code.entityName}">
        <include refid="query"/>
        where a.id = ${_jj}{id}
    </select>

    <select id="find" parameterType="string" resultType="${code.packageName}.entity.${code.entityName}">
        <include refid="query"/>
        <where>
            <if test="entity != null">
            <#list query as attr>
                <if test="!(entity.${attr.javaName} == null || entity.${attr.javaName} == '')">
                    and ${attr.jdbcName} = ${_jj}{entity.${attr.javaName}}
                </if>
            </#list>
            </if>
        </where>
    </select>

</mapper>