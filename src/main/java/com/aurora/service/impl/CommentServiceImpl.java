package com.aurora.service.impl;

import com.aurora.dao.BlogDao;
import com.aurora.dao.CommentDao;
import com.aurora.entity.Comment;
import com.aurora.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by limi on 2017/10/22.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private BlogDao blogDao;

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        List<Comment> comments = commentDao.findByBlogIdParentIdNull(blogId, Long.parseLong("-1"));
        for(Comment comment : comments){
            Long id = comment.getId();
            String parentNickname1 = comment.getNickname();
            List<Comment> childComments = commentDao.findByBlogIdParentIdNotNull(blogId,id);
//            查询出子评论
            combineChildren(blogId, childComments, parentNickname1);
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return comments;
    }

    private void combineChildren(Long blogId, List<Comment> childComments, String parentNickname1) {
//        判断是否有一级子评论
        if(childComments.size() > 0){
//                循环找出子评论的id
            for(Comment childComment : childComments){
                String parentNickname = childComment.getNickname();
                childComment.setParentNickname(parentNickname1);
                tempReplys.add(childComment);
                Long childId = childComment.getId();
//                    查询出子二级评论
                recursively(blogId, childId, parentNickname);
            }
        }
    }

    private void recursively(Long blogId, Long childId, String parentNickname1) {
//        根据子一级评论的id找到子二级评论
        List<Comment> replayComments = commentDao.findByBlogIdAndReplayId(blogId,childId);

        if(replayComments.size() > 0){
            for(Comment replayComment : replayComments){
                String parentNickname = replayComment.getNickname();
                replayComment.setParentNickname(parentNickname1);
                Long replayId = replayComment.getId();
                tempReplys.add(replayComment);
                recursively(blogId,replayId,parentNickname);
            }
        }
    }

    //    新增评论
    @Override
    public int saveComment(Comment comment) {
        comment.setCreateTime(new Date());
        int comments = commentDao.saveComment(comment);
//        文章评论计数
        blogDao.getCommentCountById(comment.getBlogId());
        return comments;
    }

    //    删除评论
    @Override
    public void deleteComment(Comment comment,Long id) {
        commentDao.deleteComment(id);
        blogDao.getCommentCountById(comment.getBlogId());
    }

//    @Transactional
//    @Override
//    public Comment saveComment(Comment comment) {
//        Long parentCommentId = comment.getParentComment().getId();
//        if (parentCommentId != -1) {
//            comment.setParentComment(commentRepository.findById(parentCommentId).get());
//        } else {
//            comment.setParentComment(null);
//        }
//        comment.setCreateTime(new Date());
//        return commentRepository.save(comment);
//    }


//    /**
//     * 循环每个顶级的评论节点
//     * @param comments
//     * @return
//     */
//    private List<Comment> eachComment(List<Comment> comments) {
//        List<Comment> commentsView = new ArrayList<>();
//        for (Comment comment : comments) {
//            Comment c = new Comment();
//            BeanUtils.copyProperties(comment,c);
//            commentsView.add(c);
//        }
//        //合并评论的各层子代到第一级子代集合中
//        combineChildren(commentsView);
//        return commentsView;
//    }

//    /**
//     *
//     * @param comments root根节点，blog不为空的对象集合
//     * @return
//     */
//    private void combineChildren(List<Comment> comments) {
//
//        for (Comment comment : comments) {
//            List<Comment> replys1 = comment.getReplyComments();
//            for(Comment reply1 : replys1) {
//                //循环迭代，找出子代，存放在tempReplys中
//                recursively(reply1);
//            }
//            //修改顶级节点的reply集合为迭代处理后的集合
//            comment.setReplyComments(tempReplys);
//            //清除临时存放区
//            tempReplys = new ArrayList<>();
//        }
//    }


//    /**
//     * 递归迭代，剥洋葱
//     * @param comment 被迭代的对象
//     * @return
//     */
//    private void recursively(Comment comment) {
//        tempReplys.add(comment);//顶节点添加到临时存放集合
//        if (comment.getReplyComments().size()>0) {
//            List<Comment> replys = comment.getReplyComments();
//            for (Comment reply : replys) {
//                tempReplys.add(reply);
//                if (reply.getReplyComments().size()>0) {
//                    recursively(reply);
//                }
//            }
//        }
//    }
}
