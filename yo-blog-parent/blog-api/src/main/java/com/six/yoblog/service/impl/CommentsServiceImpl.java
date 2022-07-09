package com.six.yoblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.six.yoblog.dao.mapper.CommentsMapper;
import com.six.yoblog.dao.pojo.Comment;
import com.six.yoblog.service.CommentsService;
import com.six.yoblog.service.SysUserService;
import com.six.yoblog.vo.CommentVo;
import com.six.yoblog.vo.Result;
import com.six.yoblog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl  implements CommentsService {

    @Autowired(required = false)
    private CommentsMapper commentsMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CommentsService commentsService;

    @Override
    public Result commentsByarticleId(Long articleid) {
        /**
         * 1.通过articleid 查询 commentvo 评论列表
         * 2.通过 作者id 查询作者信息
         * 3.判断 level 等级  level = 1 要去查询他有没有子评论
         * 4。如果有 根据评论id 进行查询（parent_id）
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Comment::getArticleId,articleid);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentsMapper.selectList(queryWrapper);
        return Result.success(copylist(comments));
    }

    /**
     * 根据评论的id 查询所有父级id 为该id 的所有评论
     * 所有 parentId 等于该 评论id 且等级为2 的所有评论
     * @param id
     * @return
     */
    @Override
    public List<CommentVo> findcommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        return copylist(commentsMapper.selectList(queryWrapper));
    }

    /**
     * copy 一个commentvo的list集合
     * @param comments
     * @return
     */
    private List<CommentVo> copylist(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments){
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    /**
     * copy 复制数组
     * @param comment
     * @return
     */
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        // 作者信息
        Long authorid = comment.getAuthorId();
        UserVo userVo = this.sysUserService.findUservcVoById(authorid);
        commentVo.setAuthor(userVo);
        // 子评论 判断评论的等级
        Integer level = comment.getLevel();
        if (1 == level){
            Long id = comment.getToUid();
            List<CommentVo> commentVoList = commentsService.findcommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        // to user 给谁评论
        if (level > 1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUservcVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }


}
