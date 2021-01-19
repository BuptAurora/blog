package com.aurora.service;

import com.aurora.entity.Comment;

import java.util.List;

/*
 *   评论业务层
 * */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    int saveComment(Comment comment);

    //删除评论
    void deleteComment(Comment comment,Long id);
}
