package me.nvliu.management.repository;

import me.nvliu.management.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

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
    /**
     * 通过微博的ID获取微博信息
     * @param blogId 微博的id
     * @return
     */
    Blog findByBlogId(String blogId);

    /**
     * 通过时间段和用户名获取微博信息
     * @param timeBegin  开始时间
     * @param tiemEnd 结束时间
     * @param blogUsername 用户名
     * @param pageable 分页
     * @return
     */
    Page<Blog> findAllByBlogTimeBetweenAndBlogUserNameLikeOrderByBlogTimeDesc(Date timeBegin, Date tiemEnd, String blogUsername, Pageable pageable);

    /**
     *通过用户名获取分页
     * @param blogUsername 用户名
     * @param pageable 分页
     * @return
     */
    Page<Blog> findAllByBlogUserNameLike(String blogUsername,Pageable pageable);

    /**
     * 通过时间段获取微博分页
     * @param timeBegin 开始时间
     * @param timeEnd 结束时间
     * @param pageable 分页信息
     * @return
     */
    Page<Blog> findAllByBlogTimeBetween(Date timeBegin,Date timeEnd,Pageable pageable);
}
