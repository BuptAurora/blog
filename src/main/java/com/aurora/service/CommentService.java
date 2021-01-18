package com.aurora.service;

import com.aurora.entity.Comment;

import java.util.List;

/**
 * Created by limi on 2017/10/22.
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    int saveComment(Comment comment);

    //删除评论
    void deleteComment(Comment comment,Long id);
}
