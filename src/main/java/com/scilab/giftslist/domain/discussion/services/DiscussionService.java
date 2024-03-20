package com.scilab.giftslist.domain.discussion.services;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.api.PageInputs;
import com.scilab.giftslist.domain.discussion.Discussion;
import com.scilab.giftslist.domain.discussion.DiscussionComment;
import com.scilab.giftslist.domain.discussion.repo.DiscussionCommentRepository;
import com.scilab.giftslist.domain.discussion.repo.DiscussionRepository;
import com.scilab.giftslist.domain.gift.Gift;
import com.scilab.giftslist.domain.gift.repo.GiftRepository;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.errors.NotFoundException;

@Service
public class DiscussionService {

    @Autowired
    private GiftRepository giftRepository;
    @Autowired
    private UserRepository userRepository;    
    @Autowired
    private DiscussionRepository discussionRepository;
    @Autowired
    private DiscussionCommentRepository discussionCommentRepository;
    @Autowired
    private DiscussionNotificationService discussionNotificationService;

    public DiscussionComment addDiscussionComment(String username, String comment, String giftId){
        User author = userRepository.findUserByUsernameIgnoreCase(username).orElseThrow(()-> new NotFoundException("No user with username :"+username));
        Gift gift = giftRepository.findById(giftId).orElseThrow(()-> new NotFoundException("No gift with ID "+giftId));
        Discussion discussion = Optional.ofNullable(gift.getDiscussion()).orElse(Discussion.builder().build());
        DiscussionComment aCommnent = DiscussionComment.builder()
            .comment(comment)
            .date(LocalDateTime.now())
            .speaker(author)
            .build();
        DiscussionComment insertedComment=discussionCommentRepository.insert(aCommnent);
        discussion.getComments().add(insertedComment);
        Discussion currentGiftDiscussion=discussionRepository.save(discussion);
        if(gift.getDiscussion()==null){
            gift.setDiscussion(currentGiftDiscussion);
            giftRepository.save(gift);
        }
        discussionNotificationService.notifyForDiscussion(currentGiftDiscussion, insertedComment, gift);
        return insertedComment;
    }

    public DiscussionComment updateComment(String username, String updatedComment, String commentId){
        User author = userRepository.findUserByUsernameIgnoreCase(username).orElseThrow(()-> new NotFoundException("No user with username :"+username));
        DiscussionComment comment=discussionCommentRepository.findById(commentId).orElseThrow(()-> new NotFoundException("No comment with ID "+commentId));
        if(!comment.getSpeaker().getId().equals(author.getId())){
            throw new BadRequestException("Only the author can update a comment");
        }
        comment.setComment(updatedComment);
        return discussionCommentRepository.save(comment);
    }

    public void deleteComment(String username, String commentId){
        User author = userRepository.findUserByUsernameIgnoreCase(username).orElseThrow(()-> new NotFoundException("No user with username :"+username));
        DiscussionComment comment=discussionCommentRepository.findById(commentId).orElseThrow(()-> new NotFoundException("No comment with ID "+commentId));
        if(!comment.getSpeaker().getId().equals(author.getId())){
            throw new BadRequestException("Only the author can delete a comment");
        }
        discussionCommentRepository.delete(comment);
    }

    public Page<DiscussionComment> giftDiscussionComments(String giftId, PageInputs pageSetting){
        Gift gift = giftRepository.findById(giftId).orElseThrow(()-> new NotFoundException("No gift with ID "+giftId));
        List<String> ids= gift.getDiscussion()==null ? new ArrayList<>() : gift.getDiscussion().getComments().stream().map(aComment-> aComment.getId()).toList();
        PageRequest paging = PageRequest.of(pageSetting.page(), pageSetting.pageSize(), Sort.by(Direction.ASC, "date"));
        return discussionCommentRepository.findAllByIdIn(ids, paging);
    }
}
