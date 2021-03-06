package org.hzero.scheduler.api.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.scheduler.app.service.ExecutorConfigService;
import org.hzero.scheduler.config.SchedulerSwaggerApiConfig;
import org.hzero.scheduler.domain.entity.ExecutorConfig;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

/**
 * 执行器配置 管理 API
 *
 * @author shuangfei.zhu@hand-china.com 2019-03-19 20:38:59
 */
@Api(tags = SchedulerSwaggerApiConfig.EXECUTOR_CONFIG)
@RestController("executorConfigController.v1")
@RequestMapping("/v1/{organizationId}/executor-configs")
public class ExecutorConfigController extends BaseController {

    private final ExecutorConfigService configService;

    @Autowired
    public ExecutorConfigController(ExecutorConfigService configService) {
        this.configService = configService;
    }

    @ApiOperation(value = "执行器配置列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{executorId}/config")
    public ResponseEntity<List<ExecutorConfig>> listConfig(@PathVariable Long organizationId, @PathVariable @Encrypt Long executorId) {
        return Results.success(configService.listConfig(executorId));
    }

    @ApiOperation(value = "新建任务前，校验执行器")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{executorId}/check")
    public ResponseEntity<Void> checkConfig(@PathVariable Long organizationId, @PathVariable @Encrypt Long executorId) {
        configService.checkConfig(executorId);
        return Results.success();
    }

    @ApiOperation(value = "更新及新建执行器配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<ExecutorConfig>> insertOrUpdateConfig(@PathVariable Long organizationId, @RequestBody @Encrypt List<ExecutorConfig> executorConfigs) {
        validList(executorConfigs);
        return Results.success(configService.insertOrUpdateConfig(executorConfigs, organizationId));
    }

    @ApiOperation(value = "删除执行器配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> batchDelete(@PathVariable Long organizationId, @RequestBody @Encrypt List<ExecutorConfig> executorConfigs) {
        configService.batchDelete(executorConfigs);
        return Results.success();
    }

    @ApiOperation(value = "任务级执行器配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{executorId}/by-job")
    public ResponseEntity<Map<String, Integer>> jobExecutorConfig(@PathVariable Long organizationId, @Encrypt Long jobId, @PathVariable @Encrypt Long executorId) throws IOException {
        return Results.success(configService.jobExecutorConfig(organizationId, jobId, executorId));
    }

    @ApiOperation(value = "任务级执行器配置(可执行)")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{executorId}/by-executable")
    public ResponseEntity<Map<String, Integer>> executableExecutor(@PathVariable Long organizationId, @Encrypt Long executableId,
                                                                   @PathVariable @Encrypt Long executorId) throws IOException {
        return Results.success(configService.executableExecutor(organizationId, executableId, executorId));
    }
}