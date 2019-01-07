package com.ecmp.basic.api;

import com.ecmp.basic.entity.DataRoleGroup;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IFindAllService;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据角色组API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 13:48      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Path("dataRoleGroup")
@Api(value = "IDataRoleGroupService 数据角色组")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IDataRoleGroupService extends IBaseEntityService<DataRoleGroup>,IFindAllService<DataRoleGroup> {
}
