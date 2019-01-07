package com.ecmp.basic.api;

import com.ecmp.basic.entity.Menu;
import com.ecmp.core.api.IBaseTreeService;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：系统菜单的API服务接口
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间                  变更人                 变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/4/19 17:04              李汶强                  新建
 * 1.0.00      2017/5/10 17:58              高银军                   修改
 * <br>
 * *************************************************************************************************<br>
 */

@Path("menu")
@Api(value = "IMenuService 系统菜单的服务接口")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IMenuService extends IBaseTreeService<Menu> {
    /**
     * 获取整个菜单树
     *
     * @return 菜单树形对象集合
     */
    @GET
    @Path("getMenuTree")
    @ApiOperation(notes = "查询所有的系统菜单树", value = "查询所有的系统菜单树")
    List<Menu> getMenuTree();

    /**
     * 根据名称模糊查询
     *
     * @param name 名称
     * @return 返回的列表
     */
    @GET
    @Path("findByNameLike")
    @ApiOperation(value = "根据名称模糊查询", notes = "根据名称模糊查询")
    List<Menu> findByNameLike(@QueryParam("name") String name);
}
