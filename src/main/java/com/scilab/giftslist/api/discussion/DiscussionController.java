package com.scilab.giftslist.api.discussion;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scilab.giftslist.api.ApiConstants;
import com.scilab.giftslist.api.GiftListRootController;
import com.scilab.giftslist.api.PageInputs;
import com.scilab.giftslist.api.discussion.model.DiscussionCommentModel;
import com.scilab.giftslist.api.discussion.model.DiscussionModel;
import com.scilab.giftslist.api.shared.ApiResponse;
import com.scilab.giftslist.domain.discussion.DiscussionComment;
import com.scilab.giftslist.domain.discussion.services.DiscussionService;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.security.AuthenticationFacade;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(ApiConstants.API_V1_PREFIX+"/discussion")
public class DiscussionController extends GiftListRootController {

    @Autowired
    private DiscussionService discussionService;

    @GetMapping("/{giftId}")
    public ResponseEntity<DiscussionModel> getGiftsComments(@PathVariable("giftId") String giftId, 
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value ="pageSize", defaultValue = "20") int pageSize){
        PageInputs pageSettings = new PageInputs(page, pageSize);
        Page<DiscussionComment> commentsPage =discussionService.giftDiscussionComments(giftId, pageSettings);
        DiscussionModel response = DiscussionModel.builder()
            .comments(commentsPage.getContent().stream().map(DiscussionCommentModel::fromDiscussionComment).toList())
            .total(commentsPage.getTotalElements())
            .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{giftId}")
    public ResponseEntity<DiscussionCommentModel> addGiftComment(@RequestBody DiscussionCommentModel model, @PathVariable("giftId") String giftId) {
        if(StringUtils.isAnyBlank(model.getComment(), giftId)){
            throw new BadRequestException("Cannot add a comment without content or giftId");
        }
        return ResponseEntity.ok(DiscussionCommentModel.fromDiscussionComment(discussionService.addDiscussionComment(AuthenticationFacade.userName(), model.getComment(), giftId)));                
    }

    @PutMapping("comments/{commentId}")
    public ResponseEntity<DiscussionCommentModel> updateDiscussionComment(@RequestBody DiscussionCommentModel model, @PathVariable("commentId") String commentId){
        if(StringUtils.isAnyBlank(model.getComment(), commentId)){
            throw new BadRequestException("Cannot update a comment without content or commentId");
        }
        return ResponseEntity.ok(DiscussionCommentModel.fromDiscussionComment(discussionService.updateComment(AuthenticationFacade.userName(), model.getComment(), commentId)));     
    }

    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteDiscussionComment(@RequestBody DiscussionCommentModel model, @PathVariable("commentId") String commentId){
        if(StringUtils.isAnyBlank(model.getComment(), commentId)){
            throw new BadRequestException("Cannot delete a comment without content or commentId");
        }
        discussionService.deleteComment(AuthenticationFacade.userName(), commentId); 
        return ResponseEntity.ok(new ApiResponse(true, StringUtils.EMPTY));
    }
    
}
