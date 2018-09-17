package me.nvliu.management.repository;

import me.nvliu.management.entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.repository
 * @Description:
 * @Date: Create in 17:24 2018/9/15
 * @Modified By:
 */
@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {
}
