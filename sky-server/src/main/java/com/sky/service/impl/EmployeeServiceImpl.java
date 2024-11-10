package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //密码MD5加密
        String pwdMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        //密码比对
        if (!pwdMd5.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    public void addNewEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //对象属性深拷贝
        BeanUtils.copyProperties(employeeDTO, employee);
        //设置其他实体类属性 账号状态、密码、创建及更新时间、创建人等
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        // 这里用ThreadLocal来获取当前操作的用户id，类似与Go中的context，不同的是这里的ThreadLocal本身被当作了一种变量去传递，它是一个变量
        //但是Go中是可以设置多个键值对进行存取多个变量值
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insertEmployee(employee);
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> employeePage = employeeMapper.pageQuery(employeePageQueryDTO);
        long total = employeePage.getTotal();
        List<Employee> employeeListResult = employeePage.getResult();
        return new PageResult(total, employeeListResult);
    }

    @Override
    public void chStatus(Integer status, Long id) {
        employeeMapper.updateStatus(status,id,LocalDateTime.now());
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Override
    public Employee getEmpById(Long id) {
        Employee emp = employeeMapper.getEmpById(id);
        return emp;
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     */
    public void updateEmp(EmployeeDTO employeeDTO) {
        Employee emp = new Employee();
        BeanUtils.copyProperties(employeeDTO, emp);
//        emp.setUpdateTime(LocalDateTime.now());
//        emp.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.updateEmp(emp);
    }

}
