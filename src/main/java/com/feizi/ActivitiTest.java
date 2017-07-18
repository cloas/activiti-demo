package com.feizi;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by feizi Ruan on 2017/7/18.
 */
public class ActivitiTest {
    private static Logger logger = LoggerFactory.getLogger(ActivitiTest.class);

    public static void main(String[] args) {
        //加载配置文件
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml")
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        repositoryService.createDeployment().addClasspathResource("interview.bpmn").deploy();
        String processId = runtimeService.startProcessInstanceByKey("Interview").getId();

        TaskService taskService = processEngine.getTaskService();
        //得到笔试的流程
        logger.info("*****************1、笔试流程开始*******************");

        //查询人力资源部门的任务列表
        List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup("人力资源部").list();
        for (Task task: taskList){
            //张三领取任务
            logger.info("人力资源部的任务：任务名称name: " + task.getName() + ",任务名称id:" + task.getId());
            taskService.claim(task.getId(), "张三");
        }

        logger.info("张三领取了任务,张三的任务数量： " + taskService.createTaskQuery().taskAssignee("张三").count());
        //查询分配给张三的任务列表
        taskList = taskService.createTaskQuery().taskAssignee("张三").list();
        for (Task task: taskList){
            //张三任务完成
            logger.info("张三的任务：任务名称name: " + task.getName() + ",任务名称id:" + task.getId());
            taskService.complete(task.getId());
        }

        logger.info("张三任务完成,张三的任务数量: " + taskService.createTaskQuery().taskAssignee("张三").count());
        logger.info("**********************笔试流程结束**********************");

        logger.info("*********************2、一面流程开始********************");
        taskList = taskService.createTaskQuery().taskCandidateGroup("技术部").list();
        for (Task task : taskList) {
            //李四领取任务
            logger.info("技术部的任务：任务名称name:"+task.getName()+",任务名称id:"+task.getId());
            taskService.claim(task.getId(), "李四");
        }

        logger.info("李四领取了任务,李四的任务数量："+taskService.createTaskQuery().taskAssignee("李四").count());
        for (Task task : taskList) {
            //李四任务完成
            logger.info("李四的任务：任务名称name:"+task.getName()+",任务名称id:"+task.getId());
            taskService.complete(task.getId());
        }

        logger.info("李四任务完成,李四的任务数量："+taskService.createTaskQuery().taskAssignee("李四").count());
        logger.info("***************一面流程结束***************");

        logger.info("***************3、二面流程开始***************");
        taskList = taskService.createTaskQuery().taskCandidateGroup("技术部").list();
        for (Task task : taskList) {
            //王五领取任务
            logger.info("技术部的任务：任务名称name:"+task.getName()+",任务名称id:"+task.getId());
            taskService.claim(task.getId(), "王五");
        }

        logger.info("王五领取任务,王五的任务数量："+taskService.createTaskQuery().taskAssignee("王五").count());
        for (Task task : taskList) {
            //王五任务完成
            logger.info("王五的任务：任务名称name:"+task.getName()+",任务名称id:"+task.getId());
            taskService.complete(task.getId());
        }

        logger.info("王五任务完成,王五的任务数量："+taskService.createTaskQuery().taskAssignee("王五").count());
        logger.info("***************二面流程结束***************");

        logger.info("**************4、HR面流程开始***************");
        taskList = taskService.createTaskQuery().taskCandidateGroup("人力资源部").list();
        for (Task task : taskList) {
            //张三领取任务
            logger.info("技术部的任务：任务名称name:"+task.getName()+",任务名称id:"+task.getId());
            taskService.claim(task.getId(), "张三");
        }

        logger.info("张三领取任务,张三的任务数量："+taskService.createTaskQuery().taskAssignee("张三").count());
        for (Task task : taskList) {
            //张三任务完成
            logger.info("张三的任务：任务名称name:"+task.getName()+",任务名称id:"+task.getId());
            taskService.complete(task.getId());
        }

        logger.info("张三完成任务,张三的任务数量："+taskService.createTaskQuery().taskAssignee("张三").count());
        logger.info("***************HR面流程结束***************");

        logger.info("***************5、录用流程开始***************");
        taskList = taskService.createTaskQuery().taskCandidateGroup("人力资源部").list();
        for (Task task : taskList) {
            //朱六领取任务
            logger.info("技术部的任务：任务名称name:"+task.getName()+",任务名称id:"+task.getId());
            taskService.claim(task.getId(), "朱六");
        }

        logger.info("朱六领取任务,朱六的任务数量："+taskService.createTaskQuery().taskAssignee("朱六").count());
        for (Task task : taskList) {
            //朱六任务完成
            logger.info("朱六的任务：任务名称name:"+task.getName()+",任务名称id:"+task.getId());
            taskService.complete(task.getId());
        }

        logger.info("朱六完成任务,朱六的任务数量："+taskService.createTaskQuery().taskAssignee("朱六").count());
        logger.info("***************录用流程结束***************");

        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processId).singleResult();

        logger.info("流程结束时间：" + historicProcessInstance.getEndTime());
    }
}
