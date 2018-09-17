package me.nvliu.management.repository;

import me.nvliu.management.entity.TaskConf;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.repository
 * @Description:
 * @Date: Create in 11:28 2018/9/17
 * @Modified By:
 */
@Repository
public interface TaskRepository extends MongoRepository<TaskConf,String>{
}
